package tech.threekilogram.mediaplayertest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.objectbus.BusConfig;

import java.util.List;
import java.util.Locale;

import tech.threekilogram.mediaplayertest.action.QueryLocalSongsAction;
import tech.threekilogram.mediaplayertest.bean.Song;

/**
 * @author wuxio
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private List< Song >       mSongs;
    private MediaPlayerManager mMediaPlayerManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BusConfig.init();
        mMediaPlayerManager = new MediaPlayerManager();
        mMediaPlayerManager.setOnCompletionListener(new CompleteListener());
    }


    private void queryLocalSongs() {

        if (mSongs == null || mSongs.size() == 0) {
            mSongs = QueryLocalSongsAction.queryLocalSongs(this);
        }
    }


    public void getCurrentMediaPlayerState(View view) {

        int state = mMediaPlayerManager.getCurrentMediaPlayerState();
        String s = MediaPlayerManager.mediaStateString(state);

        ((TextView) findViewById(R.id.tv01)).setText(s);
    }


    public void play(View view) {

        queryLocalSongs();

        mMediaPlayerManager.play(mSongs.get(0).path);
    }


    public void isPlaying(View view) {

        boolean playing = mMediaPlayerManager.isPlaying();
        String result = String.format(Locale.CHINA, "isPaying : %b", playing);
        ((TextView) findViewById(R.id.tv00)).setText(result);
    }


    public void pause(View view) {

        mMediaPlayerManager.pause();
    }


    public void resume(View view) {

        mMediaPlayerManager.resume();
    }


    public void getDuration(View view) {

        int duration = mMediaPlayerManager.getDuration(404);
        String result = String.format(Locale.CHINA, ": %f.3", duration / 1000f);
        ((TextView) findViewById(R.id.tv02)).setText(result);
    }


    public void currentPosition(View view) {

        int position = mMediaPlayerManager.getCurrentPosition();
        String result = String.format(Locale.CHINA, ": %f.3", position / 1000f);
        ((TextView) findViewById(R.id.tv03)).setText(result);
    }


    public void seekToStart(View view) {

        mMediaPlayerManager.setOnSeekCompleteListener(
                (manager, position) -> {
                    Log.i(TAG, "onSeekComplete:" + position);
                }
        );
        mMediaPlayerManager.seekTo(0);
    }


    private class CompleteListener implements MediaPlayerManager.OnCompletionListener {

        private int index = 0;


        @Override
        public void onCompletion(MediaPlayerManager manager) {

            Log.i(TAG, "onCompletion:" + "");
            Toast.makeText(MainActivity.this, "播放完成", Toast.LENGTH_SHORT).show();

            manager.play(mSongs.get(++index).path);
        }
    }
}
