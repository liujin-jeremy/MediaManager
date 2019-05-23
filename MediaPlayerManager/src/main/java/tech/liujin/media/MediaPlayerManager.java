package tech.liujin.media;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Build.VERSION_CODES;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.io.FileDescriptor;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.HttpCookie;
import java.util.List;
import java.util.Map;

/**
 * 封装 {@link MediaPlayer} 提供基本功能
 *
 * @author wuxio 2018-06-07:10:56
 */
public class MediaPlayerManager {

      /**
       * 闲置播放状态
       */
      public static final int                         MEDIA_STATE_IDLE               = 0;
      /**
       * 播放器已经初始化
       */
      public static final int                         MEDIA_STATE_INITIALIZED        = 1;
      /**
       * 播放器准备中
       */
      public static final int                         MEDIA_STATE_PREPARING          = 2;
      /**
       * 播放器准备完成
       */
      public static final int                         MEDIA_STATE_PREPARED           = 3;
      /**
       * 开始播放
       */
      public static final int                         MEDIA_STATE_STARTED            = 4;
      /**
       * 暂停
       */
      public static final int                         MEDIA_STATE_PAUSED             = 5;
      /**
       * 停止
       */
      public static final int                         MEDIA_STATE_STOPPED            = 6;
      /**
       * 播放完成
       */
      public static final int                         MEDIA_STATE_PLAYBACK_COMPLETED = 7;
      /**
       * 异常
       */
      public static final int                         MEDIA_STATE_ERROR              = 99;
      /**
       * 释放播放器
       */
      public static final int                         MEDIA_STATE_END                = 100;
      /**
       * 播放器
       */
      private             MediaPlayer                 mMediaPlayer;
      /**
       * 播放器异常处理类
       */
      private             MediaOnErrorListener        mMediaOnErrorListener;
      /**
       * 准备播放
       */
      private             MediaOnPreparedListener     mMediaOnPreparedListener;
      /**
       * 播放完成
       */
      private             MediaOnCompletionListener   mMediaOnCompletionListener;
      /**
       * 调整播放进度
       */
      private             MediaOnSeekCompleteListener mMediaOnSeekCompleteListener;
      private             int                         mCurrentMediaPlayerState;

      public MediaPlayerManager ( ) {

            createMediaPlayer();
      }

      /**
       * 初始化播放器
       */
      private void createMediaPlayer ( ) {

            mMediaPlayer = new MediaPlayer();
            mCurrentMediaPlayerState = MEDIA_STATE_IDLE;

            mMediaPlayer.setAudioStreamType( AudioManager.STREAM_MUSIC );

            makeSureListenerNotNull();
            mMediaPlayer.setOnErrorListener( mMediaOnErrorListener );
            mMediaPlayer.setOnPreparedListener( mMediaOnPreparedListener );
            mMediaPlayer.setOnCompletionListener( mMediaOnCompletionListener );
            mMediaPlayer.setOnSeekCompleteListener( mMediaOnSeekCompleteListener );
      }

      /**
       * 初始化listener
       */
      private void makeSureListenerNotNull ( ) {

            if( mMediaOnErrorListener == null ) {

                  mMediaOnErrorListener = new MediaOnErrorListener();
            }

            if( mMediaOnCompletionListener == null ) {

                  mMediaOnCompletionListener = new MediaOnCompletionListener();
            }

            if( mMediaOnSeekCompleteListener == null ) {

                  mMediaOnSeekCompleteListener = new MediaOnSeekCompleteListener();
            }

            if( mMediaOnPreparedListener == null ) {

                  mMediaOnPreparedListener = new MediaOnPreparedListener();
            }
      }

      public MediaPlayer getMediaPlayer ( ) {

            return mMediaPlayer;
      }

      public static String mediaStateString ( int currentMediaState ) {

            String result = "";

            if( currentMediaState == MEDIA_STATE_IDLE ) {

                  result = "media state : IDLE";
            } else if( currentMediaState == MEDIA_STATE_INITIALIZED ) {

                  result = "media state : INITIALIZED";
            } else if( currentMediaState == MEDIA_STATE_PREPARING ) {

                  result = "media state : PREPARING";
            } else if( currentMediaState == MEDIA_STATE_PREPARED ) {

                  result = "media state : PREPARED";
            } else if( currentMediaState == MEDIA_STATE_STARTED ) {

                  result = "media state : STARTED";
            } else if( currentMediaState == MEDIA_STATE_PAUSED ) {

                  result = "media state : PAUSED";
            } else if( currentMediaState == MEDIA_STATE_STOPPED ) {

                  result = "media state : STOPPED";
            } else if( currentMediaState == MEDIA_STATE_PLAYBACK_COMPLETED ) {

                  result = "media state : PLAYBACK_COMPLETED";
            } else if( currentMediaState == MEDIA_STATE_END ) {

                  result = "media state : released " + currentMediaState;
            } else if( currentMediaState == MEDIA_STATE_ERROR ) {

                  result = "media state : data error " + currentMediaState;
            } else {

                  result = "media state : unknown " + currentMediaState;
            }

            return result;
      }

      /**
       * @return 当前 {@link #mMediaPlayer} 状态
       */
      @MediaPlayerState
      public int getCurrentMediaPlayerState ( ) {

            return mCurrentMediaPlayerState;
      }

      /**
       * @param path filePath or a url
       *
       * @see MediaPlayer#setDataSource(String)
       */
      public void play ( String path ) {

            try {

                  reset();

                  mMediaPlayer.setDataSource( path );
                  mCurrentMediaPlayerState = MEDIA_STATE_INITIALIZED;

                  prepareAsync();
            } catch(Exception e) {

                  e.printStackTrace();
            }
      }

      /**
       * @param afd FileDescriptor
       *
       * @see MediaPlayer#setDataSource(AssetFileDescriptor)
       */
      @RequiresApi(api = VERSION_CODES.N)
      public void play ( @NonNull AssetFileDescriptor afd ) {

            try {

                  reset();

                  mMediaPlayer.setDataSource( afd );
                  mCurrentMediaPlayerState = MEDIA_STATE_INITIALIZED;

                  prepareAsync();
            } catch(Exception e) {

                  e.printStackTrace();
            }
      }

      /**
       * @param context context
       * @param uri uri resource uri
       *
       * @see MediaPlayer#setDataSource(Context, Uri)
       */
      public void play ( @NonNull Context context, @NonNull Uri uri ) {

            try {

                  reset();

                  mMediaPlayer.setDataSource( context, uri );
                  mCurrentMediaPlayerState = MEDIA_STATE_INITIALIZED;

                  prepareAsync();
            } catch(Exception e) {

                  e.printStackTrace();
            }
      }

      /**
       * @param context context
       * @param uri uri resource uri
       * @param headers headers
       *
       * @see MediaPlayer#setDataSource(Context, Uri, Map)
       */
      public void play ( @NonNull Context context, @NonNull Uri uri, @Nullable Map<String, String> headers ) {

            try {

                  reset();

                  mMediaPlayer.setDataSource( context, uri, headers );
                  mCurrentMediaPlayerState = MEDIA_STATE_INITIALIZED;

                  prepareAsync();
            } catch(Exception e) {

                  e.printStackTrace();
            }
      }

      /**
       * @param context context
       * @param uri uri resource uri
       *
       * @see MediaPlayer#setDataSource(Context, Uri, Map, List)
       */
      @RequiresApi(api = VERSION_CODES.O)
      public void play (
          @NonNull Context context,
          @NonNull Uri uri,
          @Nullable Map<String, String> headers,
          @Nullable List<HttpCookie> cookies ) {

            try {

                  reset();

                  mMediaPlayer.setDataSource( context, uri, headers, cookies );
                  mCurrentMediaPlayerState = MEDIA_STATE_INITIALIZED;

                  prepareAsync();
            } catch(Exception e) {

                  e.printStackTrace();
            }
      }

      /**
       * @param fd fd
       *
       * @see MediaPlayer#setDataSource(FileDescriptor)
       */
      public void play ( FileDescriptor fd ) {

            try {

                  reset();

                  mMediaPlayer.setDataSource( fd );
                  mCurrentMediaPlayerState = MEDIA_STATE_INITIALIZED;

                  prepareAsync();
            } catch(Exception e) {

                  e.printStackTrace();
            }
      }

      /**
       * @param fd fd
       * @param offset offset
       * @param length length
       *
       * @see MediaPlayer#setDataSource(FileDescriptor, long, long)
       */
      public void play ( FileDescriptor fd, long offset, long length ) {

            try {

                  reset();

                  mMediaPlayer.setDataSource( fd );
                  mCurrentMediaPlayerState = MEDIA_STATE_INITIALIZED;

                  prepareAsync();
            } catch(Exception e) {

                  e.printStackTrace();
            }
      }

      /**
       * @param dataSource source
       *
       * @see MediaPlayer#setDataSource(MediaDataSource)
       */
      @RequiresApi(api = VERSION_CODES.M)
      public void play ( MediaDataSource dataSource ) {

            try {

                  reset();

                  mMediaPlayer.setDataSource( dataSource );
                  mCurrentMediaPlayerState = MEDIA_STATE_INITIALIZED;

                  prepareAsync();
            } catch(Exception e) {

                  e.printStackTrace();
            }
      }

      private void prepareAsync ( ) {

            mCurrentMediaPlayerState = MEDIA_STATE_PREPARING;
            mMediaPlayer.prepareAsync();
      }

      /**
       * 测试是否正在播放
       *
       * @return true mediaPlayer is playing
       */
      public boolean isPlaying ( ) {

            return mMediaPlayer.isPlaying();
      }

      /**
       * 暂停
       */
      public void pause ( ) {

            if( mCurrentMediaPlayerState == MEDIA_STATE_STARTED ) {
                  mMediaPlayer.pause();
                  mCurrentMediaPlayerState = MEDIA_STATE_PAUSED;
            }
      }

      /**
       * 从暂停恢复到播放状态
       */
      public void resume ( ) {

            if( mCurrentMediaPlayerState == MEDIA_STATE_PAUSED ) {
                  mMediaPlayer.start();
                  mCurrentMediaPlayerState = MEDIA_STATE_STARTED;
            }
      }

      /**
       * 重置状态,将会停止播放
       */
      public void reset ( ) {

            mMediaPlayer.reset();
            mCurrentMediaPlayerState = MEDIA_STATE_IDLE;
      }

      /**
       * same as {@link MediaPlayer#getDuration()}
       *
       * @param errorCode 自己定义的error code,如果获取不到时长,返回该值,注意如果直播的类型的内容获取到的时长是-1
       *
       * @return 总时长
       */
      public int getDuration ( int errorCode ) {

            int state = mCurrentMediaPlayerState;
            if( state == MEDIA_STATE_STARTED ||
                state == MEDIA_STATE_PAUSED ||
                state == MEDIA_STATE_PLAYBACK_COMPLETED ) {
                  return mMediaPlayer.getDuration();
            }

            return errorCode;
      }

      /**
       * same as {@link MediaPlayer#getCurrentPosition()}
       *
       * @return 当前播放位置
       */
      public int getCurrentPosition ( ) {

            return mMediaPlayer.getCurrentPosition();
      }

      /**
       * @param position new position
       */
      public void seekTo ( int position ) {

            int state = mCurrentMediaPlayerState;
            if( state == MEDIA_STATE_STARTED ||
                state == MEDIA_STATE_PAUSED ||
                state == MEDIA_STATE_PLAYBACK_COMPLETED ) {

                  mMediaOnSeekCompleteListener.setSeekPosition( position );
                  mMediaPlayer.seekTo( position );
            }
      }

      /**
       * 为{@link #mMediaPlayer}设置准备监听
       *
       * @param userOnPreparedListener 准备完毕监听,每首歌播放之前回调
       */
      public void setOnPreparedListener ( MediaPlayer.OnPreparedListener userOnPreparedListener ) {

            mMediaOnPreparedListener.setUserOnPreparedListener( userOnPreparedListener );
      }

      /**
       * 设置 {@link #seekTo(int)} 完成监听
       */
      public void setOnSeekCompleteListener ( OnSeekCompleteListener onSeekCompleteListener ) {

            mMediaOnSeekCompleteListener.setOnSeekCompleteListener( onSeekCompleteListener );
      }

      /**
       * 为 {@link #mMediaPlayer} 设置播放完成监听
       */
      public void setOnCompletionListener ( MediaPlayer.OnCompletionListener onCompletionListener ) {

            mMediaOnCompletionListener.setOnCompletionListener( onCompletionListener );
      }

      /**
       * 释放资源
       */
      public void release ( ) {

            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mCurrentMediaPlayerState = MEDIA_STATE_END;
      }

      /**
       * @return true released cant play anymore, you need to new {@link MediaPlayerManager}
       */
      public boolean isReleased ( ) {

            return mCurrentMediaPlayerState == MEDIA_STATE_END;
      }

      /**
       * 重启,如果已经释放了
       */
      public void reCreateIfReleased ( ) {

            if( isReleased() ) {
                  createMediaPlayer();
            }
      }

      /**
       * 设置数据数据错误监听
       */
      public void setOnErrorListener (
          MediaPlayer.OnErrorListener onDataSourceErrorListener ) {

            mMediaOnErrorListener.setOnErrorListener( onDataSourceErrorListener );
      }

      /**
       * 定义状态范围
       */
      @IntDef({
          MEDIA_STATE_IDLE,
          MEDIA_STATE_INITIALIZED,
          MEDIA_STATE_PREPARED,
          MEDIA_STATE_PREPARING,
          MEDIA_STATE_STARTED,
          MEDIA_STATE_PAUSED,
          MEDIA_STATE_STOPPED,
          MEDIA_STATE_PLAYBACK_COMPLETED,
      })
      @Retention(RetentionPolicy.SOURCE)
      public @interface MediaPlayerState { }

      /**
       * {@link #seekTo(int)}完成后的回调
       */
      public interface OnSeekCompleteListener {

            /**
             * Called to indicate the completion of a seek operation.
             *
             * @param mp mediaPlayer
             * @param position set to position
             */
            void onSeekComplete ( MediaPlayer mp, int position );
      }

      /**
       * 异步准备,准备好之后开始
       */
      private class MediaOnPreparedListener implements MediaPlayer.OnPreparedListener {

            /**
             * 用户设置的准备完毕监听
             */
            private MediaPlayer.OnPreparedListener mUserOnPreparedListener;

            @Override
            public void onPrepared ( MediaPlayer mp ) {

                  mCurrentMediaPlayerState = MEDIA_STATE_PREPARED;

                  if( mUserOnPreparedListener != null ) {
                        mUserOnPreparedListener.onPrepared( mp );
                  }

                  mMediaPlayer.start();
                  mCurrentMediaPlayerState = MEDIA_STATE_STARTED;
            }

            public void setUserOnPreparedListener ( MediaPlayer.OnPreparedListener onPreparedListener ) {

                  mUserOnPreparedListener = onPreparedListener;
            }
      }

      /**
       * 代理{@link MediaPlayer.OnSeekCompleteListener}功能
       */
      private class MediaOnSeekCompleteListener implements MediaPlayer.OnSeekCompleteListener {

            private OnSeekCompleteListener mOnSeekCompleteListener;

            /**
             * come from {@link #seekTo(int)} params
             */
            private int mSeekPosition;

            @Override
            public void onSeekComplete ( MediaPlayer mp ) {

                  if( mOnSeekCompleteListener != null ) {

                        mOnSeekCompleteListener.onSeekComplete(
                            mp,
                            mSeekPosition
                        );
                  }
            }

            void setSeekPosition ( int seekPosition ) {

                  mSeekPosition = seekPosition;
            }

            void setOnSeekCompleteListener ( OnSeekCompleteListener onSeekCompleteListener ) {

                  mOnSeekCompleteListener = onSeekCompleteListener;
            }
      }

      /**
       * 完成的监听
       */
      private class MediaOnCompletionListener implements MediaPlayer.OnCompletionListener {

            private MediaPlayer.OnCompletionListener mOnCompletionListener;

            @Override
            public void onCompletion ( MediaPlayer mp ) {

                  mCurrentMediaPlayerState = MEDIA_STATE_PLAYBACK_COMPLETED;
                  if( mOnCompletionListener != null ) {
                        mOnCompletionListener.onCompletion( mp );
                  }
            }

            void setOnCompletionListener ( MediaPlayer.OnCompletionListener onCompletionListener ) {

                  mOnCompletionListener = onCompletionListener;
            }
      }

      /**
       * 处理异常状态,
       * {@link MediaPlayer#prepare()}和{@link MediaPlayer#prepareAsync()}可能会触发异常,
       * {@link MediaPlayer#setDataSource(String)} ()}这一系列方法同样可能触发异常
       */
      private class MediaOnErrorListener implements MediaPlayer.OnErrorListener {

            private static final String TAG = "Media_Error_State:";

            private MediaPlayer.OnErrorListener mOnErrorListener;

            @Override
            public boolean onError ( MediaPlayer mp, int what, int extra ) {

                  mCurrentMediaPlayerState = MEDIA_STATE_ERROR;
                  mMediaPlayer = handleErrorState( what, extra, mp );
                  mCurrentMediaPlayerState = MEDIA_STATE_IDLE;

                  if( mOnErrorListener != null ) {
                        mOnErrorListener.onError( mp, what, extra );
                  }

                  return true;
            }

            public void setOnErrorListener ( OnErrorListener onErrorListener ) {

                  mOnErrorListener = onErrorListener;
            }

            /**
             * 处理media error 状态
             *
             * @param what 标记
             * @param mediaPlayer 发生error的mediaPlayer
             *
             * @return 新 new 的 {@link MediaPlayer}
             */
            private MediaPlayer handleErrorState (
                int what, int extra, MediaPlayer mediaPlayer ) {

                  printErrorState( what, extra );

                  if( what == MediaPlayer.MEDIA_ERROR_UNKNOWN
                      || what == MediaPlayer.MEDIA_ERROR_SERVER_DIED ) {

                        mediaPlayer.release();
                        createMediaPlayer();
                        return mMediaPlayer;
                  }

                  return mediaPlayer;
            }

            /**
             * 打印状态
             *
             * @param what error state
             */
            private void printErrorState ( int what, int extra ) {

                  String whatMsg = "";

                  if( what == MediaPlayer.MEDIA_ERROR_UNKNOWN ) {

                        whatMsg = "MEDIA_ERROR_UNKNOWN";
                  } else if( what == MediaPlayer.MEDIA_ERROR_SERVER_DIED ) {

                        whatMsg = "MEDIA_ERROR_SERVER_DIED";
                  }

                  String extraMsg = "";

                  if( extra == MediaPlayer.MEDIA_ERROR_IO ) {

                        extraMsg = "MEDIA_ERROR_IO";
                  } else if( extra == MediaPlayer.MEDIA_ERROR_MALFORMED ) {

                        extraMsg = "MEDIA_ERROR_MALFORMED";
                  } else if( extra == MediaPlayer.MEDIA_ERROR_TIMED_OUT ) {

                        extraMsg = "MEDIA_ERROR_MALFORMED";
                  } else if( extra == MediaPlayer.MEDIA_ERROR_UNSUPPORTED ) {

                        extraMsg = "MEDIA_ERROR_UNSUPPORTED";
                  } else if( extra == -2147483648 ) {

                        extraMsg = "low-level system error";
                  }

                  Log.i( TAG, "printErrorState:" + whatMsg + " " + extraMsg );
            }
      }
}
