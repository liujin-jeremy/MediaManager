package tech.liujin.mediaplayertest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import tech.liujin.media.MediaPlayerManager;
import tech.liujin.mediaplayertest.action.QueryLocalSongsAction;
import tech.liujin.mediaplayertest.bean.Song;

/**
 * @author wuxio
 */
public class MainActivity extends AppCompatActivity {

      private static final String TAG = "MainActivity";

      private List<Song>         mSongs;
      private MediaPlayerManager mMediaPlayerManager;
      private int                mIndex = 0;

      @Override
      protected void onCreate ( Bundle savedInstanceState ) {

            super.onCreate( savedInstanceState );
            setContentView( R.layout.activity_main );

            mMediaPlayerManager = new MediaPlayerManager();
            CompleteListener listener = new CompleteListener();
            mMediaPlayerManager.setOnCompletionListener( listener );
            mMediaPlayerManager.setOnErrorListener( new DataSourceErrorListener() );
            mMediaPlayerManager.setOnPreparedListener( new PreparedListener() );

            queryLocalSongs();
      }

      private void queryLocalSongs ( ) {

            if( mSongs == null || mSongs.size() == 0 ) {
                  try {
                        mSongs = QueryLocalSongsAction.queryLocalSongs( this );
                  } catch(Exception e) {
                        e.printStackTrace();
                        requestSDPermission();
                  }
            }
      }

      private void requestSDPermission ( ) {

            int permission = ContextCompat.checkSelfPermission( this, Manifest.permission.WRITE_EXTERNAL_STORAGE );
            if( permission == PackageManager.PERMISSION_DENIED ) {
                  ActivityCompat.requestPermissions( this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1002 );
            }
      }

      @Override
      public void onRequestPermissionsResult (
          int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults ) {

            super.onRequestPermissionsResult( requestCode, permissions, grantResults );
            if( requestCode == 1002 ) {
                  int result = grantResults[ 0 ];
                  if( result != PackageManager.PERMISSION_GRANTED ) {
                        mSongs = new ArrayList<>();
                  }
            }
      }

      @Override
      protected void onDestroy ( ) {

            super.onDestroy();
      }

      public void getCurrentMediaPlayerState ( View view ) {

            int state = mMediaPlayerManager.getCurrentMediaPlayerState();
            String s = MediaPlayerManager.mediaStateString( state );

            ( (TextView) findViewById( R.id.tv01 ) ).setText( s );
      }

      public void play ( View view ) {

            mMediaPlayerManager.reCreateIfReleased();
            mMediaPlayerManager.play( mSongs.get( 0 ).path );
      }

      public void isPlaying ( View view ) {

            boolean playing = mMediaPlayerManager.isPlaying();
            String result = String.format( Locale.CHINA, "isPaying : %b", playing );
            ( (TextView) findViewById( R.id.tv00 ) ).setText( result );
      }

      public void pause ( View view ) {

            mMediaPlayerManager.pause();
      }

      public void resume ( View view ) {

            mMediaPlayerManager.resume();
      }

      public void getDuration ( View view ) {

            int duration = mMediaPlayerManager.getDuration( 404 );
            String result = String.format( Locale.CHINA, ": %f.3", duration / 1000f );
            ( (TextView) findViewById( R.id.tv02 ) ).setText( result );
      }

      public void currentPosition ( View view ) {

            int position = mMediaPlayerManager.getCurrentPosition();
            String result = String.format( Locale.CHINA, ": %f.3", position / 1000f );
            ( (TextView) findViewById( R.id.tv03 ) ).setText( result );
      }

      public void seekToStart ( View view ) {

            mMediaPlayerManager.setOnSeekCompleteListener(
                ( manager, position ) -> {
                      Log.i( TAG, "onSeekComplete:" + position );
                }
            );
            mMediaPlayerManager.seekTo( 0 );
      }

      public void prev ( View view ) {

            queryLocalSongs();

            int index = mIndex - 1;

            if( index < 0 ) {
                  index = 0;
            }

            mMediaPlayerManager.play( mSongs.get( index ).path );
            mIndex = index;
      }

      public void next ( View view ) {

            queryLocalSongs();

            int index = mIndex + 1;

            if( index >= mSongs.size() ) {
                  index = mSongs.size() - 1;
            }

            mMediaPlayerManager.play( mSongs.get( index ).path );
            mIndex = index;
      }

      public void release ( View view ) {
            /* 记得释放 */

            mMediaPlayerManager.release();
      }

      public void playFromNet ( View view ) {

            mMediaPlayerManager.reCreateIfReleased();
            mMediaPlayerManager.play(
                "https://m7.music.126.net/20190410193722/d1e2fa98feadde669afa07d5754b01e5/ymusic/MTE5MzI0NDYzMA==/75df6504f90a41d0adafc304b04089ad.mp3" );
      }

      private class CompleteListener implements OnCompletionListener {

            @Override
            public void onCompletion ( MediaPlayer manager ) {

                  Log.i( TAG, "onCompletion:" + "" );
                  Toast.makeText( MainActivity.this, "播放完成", Toast.LENGTH_SHORT ).show();

                  mMediaPlayerManager.play( mSongs.get( ++mIndex ).path );
            }
      }

      private class DataSourceErrorListener implements OnErrorListener {

            @Override
            public boolean onError ( MediaPlayer mp, int what, int extra ) {

                  Log.e( TAG, "onError : data" + what + " " + extra );

                  return false;
            }
      }

      private class PreparedListener implements OnPreparedListener {

            @Override
            public void onPrepared ( MediaPlayer mp ) {

                  Log.e( TAG, "onPrepared : " );
            }
      }

      private class OnSeekFinishListener implements MediaPlayerManager.OnSeekCompleteListener {

            @Override
            public void onSeekComplete ( MediaPlayer mp, int position ) {

                  Log.e( TAG, "onSeekComplete : " + position );
            }
      }
}
