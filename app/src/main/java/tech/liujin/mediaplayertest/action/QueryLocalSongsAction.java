package tech.liujin.mediaplayertest.action;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import tech.liujin.mediaplayertest.bean.Song;

/**
 * @author wuxio 2018-05-28:13:45
 */
public class QueryLocalSongsAction {

    private static final String TAG = "QueryLocalSongsAction";


    /**
     * 查询本地数据库所有音乐
     *
     * @param context context to query
     * @return list of local songs
     */
    public static List< Song > queryLocalSongs(Context context) {

        String selectionStatement = "is_music=1 AND title != ''";

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,

                /* title */

                new String[]{
                        "_id",
                        "title",
                        "artist",
                        "album",
                        "duration",
                        "track",
                        "artist_id",
                        "album_id",
                        MediaStore.Audio.Media.DATA
                },
                selectionStatement, null, null);

        if (cursor == null) {

            Log.i(TAG, "queryLocalSongs:" + "cursor null");
            return null;
        }

        List< Song > songs = new ArrayList<>();

        while (cursor.moveToNext()) {

            /* _id */
            long id = cursor.getLong(0);
            /* title 歌曲名字 */
            String title = cursor.getString(1);
            /* artist 作者/歌曲演唱者 */
            String artist = cursor.getString(2);
            /* album 专辑 */
            String album = cursor.getString(3);
            /* duration 时长 */
            int duration = cursor.getInt(4);
            /* track 追踪号 */
            int trackNumber = cursor.getInt(5);
            /* artist_id 作者ID */
            long artistId = cursor.getInt(6);
            /* album_id 专辑ID */
            long albumId = cursor.getLong(7);
            /* _data 歌曲文件路径 */
            String path = cursor.getString(8);

            songs.add(new Song(id, albumId, artistId, title, artist, album, duration, trackNumber, path));
        }

        cursor.close();

        return songs;
    }
}
