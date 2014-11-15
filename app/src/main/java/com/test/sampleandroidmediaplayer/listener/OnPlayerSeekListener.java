package com.test.sampleandroidmediaplayer.listener;

import com.test.sampleandroidmediaplayer.media.PodcastPlayer;

import android.widget.SeekBar;


public class OnPlayerSeekListener implements SeekBar.OnSeekBarChangeListener {

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (!PodcastPlayer.getInstance().isPlaying()) {
            return;
        }
        PodcastPlayer.getInstance().seekTo(seekBar.getProgress());
    }
}
