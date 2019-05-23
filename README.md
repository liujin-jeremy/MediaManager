
## MediaPlayer 封装

```
implementation 'tech.liujin:MediaPlayerManager:1.0.6'
```

### 创建

```
MediaPlayerManager mMediaPlayerManager = new MediaPlayerManager();
```

### 播放

```
// 播放,参数是音乐路径可以是file可以是url
mMediaPlayerManager.play(songPath);
```

```
// 测试正在播放
boolean playing = mMediaPlayerManager.isPlaying();
```

```
// 播放总时长
int duration = mMediaPlayerManager.getDuration(404);
```

```
// 播放当前时长
int position = mMediaPlayerManager.getCurrentPosition();
```

### 准备播放监听

```
private class PreparedListener implements OnPreparedListener{
    @Override
    public void onPrepared (MediaPlayerManager manager) {
        
        // 准备完成马上开始播放
        Log.e(TAG, "onPrepared : ");
    }
}

mMediaPlayerManager.setOnPreparedListener(new PreparedListener());
```

### 播放完成监听

```
// 实现监听,完成后播放下一首
private class CompleteListener implements MediaPlayerManager.OnCompletionListener {
    @Override
    public void onCompletion(MediaPlayerManager manager) {
        Log.i(TAG, "onCompletion:" + "");
        Toast.makeText(MainActivity.this, "播放完成", Toast.LENGTH_SHORT).show();
        manager.play(mSongs.get(++mIndex).path);
    }
}

// 设置给manager
mMediaPlayerManager.setOnCompletionListener(listener);
```

### 暂停/恢复

```
mMediaPlayerManager.pause();
```

```
mMediaPlayerManager.resume();
```

### 跳转播放位置

```
mMediaPlayerManager.seekTo(0);
```

```
// 监听跳转完成
mMediaPlayerManager.setOnSeekCompleteListener(
        (manager, position) -> {
            Log.i(TAG, "onSeekComplete:" + position);
        }
);
```

### 数据错误监听

```
private class DataSourceErrorListener implements OnDataSourceErrorListener{
    @Override
    public void onError (MediaPlayerManager manager, int what, int extra) {
        
        Log.e(TAG, "onError : data");
    }
}

mMediaPlayerManager.setOnDataSourceErrorListener(new DataSourceErrorListener());
```

### 释放资源

```
mMediaPlayerManager.release();
```