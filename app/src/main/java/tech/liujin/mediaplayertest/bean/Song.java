package tech.liujin.mediaplayertest.bean;

/**
 * 歌曲 bean 对象
 *
 * @author wuxio
 */
public class Song {


    /**
     * 对应mediaStore _id
     */
    public final long   id;
    /**
     * album_id 专辑ID
     */
    public final long   albumId;
    /**
     * artist_id 作者ID
     */
    public final long   artistId;
    /**
     * album 专辑
     */
    public final String albumName;
    /**
     * artist 作者
     */
    public final String artistName;
    /**
     * duration 歌曲时长
     */
    public final int    duration;
    /**
     * title 歌曲名字
     */
    public final String title;
    /**
     * track 追踪号
     */
    public final int    trackNumber;
    /**
     * 得分 ?
     */
    public       float  playCountScore;
    /**
     * 歌曲路径
     */
    public final String path;


    public Song() {

        this.id = -1;
        this.albumId = -1;
        this.artistId = -1;
        this.title = "";
        this.artistName = "";
        this.albumName = "";
        this.duration = -1;
        this.trackNumber = -1;
        this.path = "";
    }


    public Song(
            long _id,
            long _albumId,
            long _artistId,
            String _title,
            String _artistName,
            String _albumName,
            int _duration,
            int _trackNumber) {

        this.id = _id;
        this.albumId = _albumId;
        this.artistId = _artistId;
        this.title = _title;
        this.artistName = _artistName;
        this.albumName = _albumName;
        this.duration = _duration;
        this.trackNumber = _trackNumber;
        this.path = "";
    }


    public Song(
            long _id,
            long _albumId,
            long _artistId,
            String _title,
            String _artistName,
            String _albumName,
            int _duration,
            int _trackNumber,
            String _path) {

        this.id = _id;
        this.albumId = _albumId;
        this.artistId = _artistId;
        this.title = _title;
        this.artistName = _artistName;
        this.albumName = _albumName;
        this.duration = _duration;
        this.trackNumber = _trackNumber;
        this.path = _path;
    }


    public void setPlayCountScore(float playCountScore) {

        this.playCountScore = playCountScore;
    }


    public float getPlayCountScore() {

        return playCountScore;
    }


    @Override
    public String toString() {

        return "Song{" +
                "albumId=" + albumId +
                ", albumName='" + albumName + '\'' +
                ", artistId=" + artistId +
                ", artistName='" + artistName + '\'' +
                ", duration=" + duration +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", trackNumber=" + trackNumber +
                ", playCountScore=" + playCountScore +
                ", path='" + path + '\'' +
                '}';
    }
}
