package com.test.sampleandroidmediaplayer.ui;

import com.test.sampleandroidmediaplayer.R;
import com.test.sampleandroidmediaplayer.models.MockEpisode;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MediaFragment mediaFragment = (MediaFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_media);
        mediaFragment.setup(new MockEpisode());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
