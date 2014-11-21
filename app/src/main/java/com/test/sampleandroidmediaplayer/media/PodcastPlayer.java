package com.test.sampleandroidmediaplayer.media;

import com.test.sampleandroidmediaplayer.models.Episode;
import com.test.sampleandroidmediaplayer.utils.NetworkUtils;
import com.test.sampleandroidmediaplayer.utils.Timer;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

public class PodcastPlayer extends MediaPlayer implements MediaPlayer.OnPreparedListener {

    public static final String TAG = PodcastPlayer.class.getSimpleName();

    private static PodcastPlayer instance;

    private Timer timer;
    private Episode episode;
    private StateChangedListener stateChangedListener;
    private PlayerStatus playerStatus = PlayerStatus.STOPPED;
    private CurrentTimeListener currentTimeListener;

    @Override
    public boolean isPlaying() {
        return (playerStatus == PlayerStatus.PLAYING);
    }

    public boolean isPaused() {
        return (playerStatus == PlayerStatus.PAUSED);
    }

    public boolean isStopped() {
        return (playerStatus == PlayerStatus.STOPPED);
    }

    private PodcastPlayer() {
        super();
    }

    public static PodcastPlayer getInstance() {
        if (instance == null) {
            instance = new PodcastPlayer();
        }
        return instance;
    }

    public Episode getEpisode() {
        return episode;
    }

    public boolean isPlayingEpisode(Episode other) {
        if (other == null) {
            return false;
        }
        return other.isSameEpisode(episode);
    }

    private void setCurrentTimeListener() {
        if (currentTimeListener == null) {
            return;
        }

        setCurrentTimeListener(currentTimeListener);
    }

    public void setCurrentTimeListener(final CurrentTimeListener currentTimeListener) {
        this.currentTimeListener = currentTimeListener;

        timer = new Timer(new Timer.Callback() {
            @Override
            public void tick(long timeMillis) {
                if (isPlaying() || isPaused()) {
                    currentTimeListener.onTick(getCurrentPosition(), getDuration());
                } else {
                    currentTimeListener.onTick(0, 0);
                }
            }
        });
        timer.start();
    }

    public void start(Context context, Episode episode, StateChangedListener stateChangedListener) {
        Uri enclosure = episode.getEnclosure();
        if (enclosure == null) {
            return;
        }

        playerStatus = playerStatus.PREPARING;

        this.episode = episode;
        this.stateChangedListener = stateChangedListener;

        reset();
        try {
            Context applicationContext = context.getApplicationContext();
            setDataSource(
                    applicationContext,
                    episode.getEnclosure(),
                    NetworkUtils.createUserAgentHeader(applicationContext));

            prepareAsync();
            setOnPreparedListener(this);
        } catch (IOException e) {
            Log.e(TAG, "An error occurred while preparing data source: "
                    + (enclosure == null ? "null" : enclosure.toString()));
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void start() {
        restart();
    }

    public void restart() {
        super.start();
        playerStatus = PlayerStatus.PLAYING;
    }

    @Override
    public void pause() {
        super.pause();
        playerStatus = PlayerStatus.PAUSED;
    }

    @Override
    public void stop() {
        super.pause();
        super.seekTo(0);
        playerStatus = PlayerStatus.STOPPED;
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        playerStatus = PlayerStatus.PREPARED;
        start();
        stateChangedListener.onStart();
        playerStatus = PlayerStatus.PLAYING;
    }

    public static interface StateChangedListener {

        public void onStart();
    }

    public static interface CurrentTimeListener {

        public void onTick(int currentPosition, int duration);
    }

    public enum PlayerStatus {
        PREPARING,
        PAUSED,
        PLAYING,
        STOPPED,
        PREPARED,
    }
}
