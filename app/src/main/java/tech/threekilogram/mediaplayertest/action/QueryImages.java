package tech.threekilogram.mediaplayertest.action;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Liujin 2018-11-05:12:19
 */
public class QueryImages {

      public static final String[] IMAGES_PROJECTION = new String[]{
          Media.BUCKET_ID,
          Media.BUCKET_DISPLAY_NAME,
          Media.MIME_TYPE,
          Media.DATA };

      public static final String[] THUMBNAILS_PROJECTION = new String[]{
          Thumbnails.IMAGE_ID,
          Thumbnails.DATA
      };

      private QueryImages ( ) { }

      /**
       * 读取本地图片
       * <p>
       * 需要权限 permission.READ_EXTERNAL_STORAGE
       *
       * @param context context
       *
       * @return cursor
       */
      public static Cursor getImagesCursor ( Context context ) {

            context = context.getApplicationContext();
            Cursor cursor = context.getContentResolver()
                                   .query(
                                       MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                       IMAGES_PROJECTION,
                                       null, null,
                                       MediaStore.Images.Media.DATE_ADDED
                                   );
            return cursor;
      }

      /**
       * 读取本地图片
       * <p>
       * 需要权限 permission.READ_EXTERNAL_STORAGE
       *
       * @param context context
       *
       * @return cursor
       */
      public static Cursor getImagesCursorExcludeGif ( Context context ) {

            context = context.getApplicationContext();
            Cursor cursor = context.getContentResolver()
                                   .query(
                                       MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                       IMAGES_PROJECTION,
                                       Media.MIME_TYPE + " is not ?", new String[]{ "image/gif" },
                                       MediaStore.Images.Media.DATE_ADDED
                                   );
            return cursor;
      }

      /**
       * 读取本地图片
       * <p>
       * 需要权限 permission.READ_EXTERNAL_STORAGE
       *
       * @param context context
       *
       * @return cursor
       */
      @SuppressWarnings("TryFinallyCanBeTryWithResources")
      public static List<Image> queryImages ( Context context ) {

            ArrayList<Image> images = new ArrayList<>();
            queryImages( context, images );
            return images;
      }

      /**
       * 读取本地图片
       * <p>
       * 需要权限 permission.READ_EXTERNAL_STORAGE
       *
       * @param context context
       *
       * @return cursor
       */
      @SuppressWarnings("TryFinallyCanBeTryWithResources")
      public static void queryImages ( Context context, ArrayList<Image> images ) {

            context = context.getApplicationContext();
            Cursor cursor = getImagesCursor( context );

            try {
                  int idIndex = cursor.getColumnIndex( IMAGES_PROJECTION[ 0 ] );
                  int nameIndex = cursor.getColumnIndex( IMAGES_PROJECTION[ 1 ] );
                  int mimeIndex = cursor.getColumnIndex( IMAGES_PROJECTION[ 2 ] );
                  int dataIndex = cursor.getColumnIndex( IMAGES_PROJECTION[ 3 ] );

                  while( cursor.moveToNext() ) {

                        String data = cursor.getString( dataIndex );
                        File file = new File( data );
                        if( file.exists() ) {
                              Image image = new Image();
                              image.setBucketId( cursor.getString( idIndex ) );
                              image.setBucketDisplayName( cursor.getString( nameIndex ) );
                              image.setData( data );
                              image.setMime( cursor.getString( mimeIndex ) );
                              images.add( image );
                        }
                  }
            } catch(Exception e) {
                  e.printStackTrace();
            } finally {
                  if( cursor != null ) {
                        cursor.close();
                  }
            }
      }

      /**
       * 读取本地图片
       * <p>
       * 需要权限 permission.READ_EXTERNAL_STORAGE
       *
       * @param context context
       *
       * @return cursor
       */
      @SuppressWarnings("TryFinallyCanBeTryWithResources")
      public static List<Image> queryImagesExcludeGif ( Context context ) {

            ArrayList<Image> images = new ArrayList<>();
            queryImagesExcludeGif( context, images );
            return images;
      }

      /**
       * 读取本地图片
       * <p>
       * 需要权限 permission.READ_EXTERNAL_STORAGE
       *
       * @param context context
       *
       * @return cursor
       */
      @SuppressWarnings("TryFinallyCanBeTryWithResources")
      public static void queryImagesExcludeGif ( Context context, ArrayList<Image> images ) {

            context = context.getApplicationContext();
            Cursor cursor = getImagesCursorExcludeGif( context );

            try {
                  int idIndex = cursor.getColumnIndex( IMAGES_PROJECTION[ 0 ] );
                  int nameIndex = cursor.getColumnIndex( IMAGES_PROJECTION[ 1 ] );
                  int mimeIndex = cursor.getColumnIndex( IMAGES_PROJECTION[ 2 ] );
                  int dataIndex = cursor.getColumnIndex( IMAGES_PROJECTION[ 3 ] );

                  while( cursor.moveToNext() ) {

                        String data = cursor.getString( dataIndex );
                        File file = new File( data );
                        if( file.exists() ) {
                              Image image = new Image();
                              image.setBucketId( cursor.getString( idIndex ) );
                              image.setBucketDisplayName( cursor.getString( nameIndex ) );
                              image.setData( data );
                              image.setMime( cursor.getString( mimeIndex ) );
                              images.add( image );
                        }
                  }
            } catch(Exception e) {
                  e.printStackTrace();
            } finally {
                  if( cursor != null ) {
                        cursor.close();
                  }
            }
      }

      public static Cursor getThumbnailsCursor ( Context context ) {

            context = context.getApplicationContext();
            Cursor cursor = context.getContentResolver().query(
                Thumbnails.EXTERNAL_CONTENT_URI,
                THUMBNAILS_PROJECTION,
                null,
                null,
                null
            );
            return cursor;
      }

      public static List<Thumbnail> queryThumbnails ( Context context ) {

            ArrayList<Thumbnail> thumbnails = new ArrayList<>();
            queryThumbnails( context, thumbnails );
            return thumbnails;
      }

      @SuppressWarnings("TryFinallyCanBeTryWithResources")
      public static void queryThumbnails ( Context context, ArrayList<Thumbnail> thumbnails ) {

            context = context.getApplicationContext();
            Cursor cursor = context.getContentResolver().query(
                Thumbnails.EXTERNAL_CONTENT_URI,
                THUMBNAILS_PROJECTION,
                null,
                null,
                null
            );

            try {
                  int idIndex = cursor.getColumnIndex( THUMBNAILS_PROJECTION[ 0 ] );
                  int dataIndex = cursor.getColumnIndex( THUMBNAILS_PROJECTION[ 1 ] );

                  while( cursor.moveToNext() ) {
                        Thumbnail thumbnail = new Thumbnail();
                        thumbnail.setImageId( cursor.getLong( idIndex ) );
                        String data = cursor.getString( dataIndex );
                        thumbnail.setData( data );
                        thumbnails.add( thumbnail );
                  }
            } catch(Exception e) {
                  e.printStackTrace();
            } finally {
                  if( cursor != null ) {
                        cursor.close();
                  }
            }
      }

      public static Bitmap getThumbnailBitmap ( Context context, long id ) {

            return Thumbnails.getThumbnail( context.getContentResolver(), id, 1, null );
      }

      public static class Image {

            protected String mBucketId;
            protected String mBucketDisplayName;
            protected String mData;
            protected String mMime;

            public String getMime ( ) {

                  return mMime;
            }

            public void setMime ( String mime ) {

                  mMime = mime;
            }

            public String getBucketId ( ) {

                  return mBucketId;
            }

            public void setBucketId ( String bucketId ) {

                  mBucketId = bucketId;
            }

            public String getBucketDisplayName ( ) {

                  return mBucketDisplayName;
            }

            public void setBucketDisplayName ( String bucketDisplayName ) {

                  mBucketDisplayName = bucketDisplayName;
            }

            public String getData ( ) {

                  return mData;
            }

            public void setData ( String data ) {

                  mData = data;
            }

            @Override
            public String toString ( ) {

                  return "Image{" +
                      "mBucketId='" + mBucketId + '\'' +
                      ", mBucketDisplayName='" + mBucketDisplayName + '\'' +
                      ", mData='" + mData + '\'' +
                      ", mMime='" + mMime + '\'' +
                      '}';
            }
      }

      public static class Thumbnail {

            protected long mImageId;
            protected String mData;

            public long getImageId ( ) {

                  return mImageId;
            }

            public void setImageId ( long imageId ) {

                  mImageId = imageId;
            }

            public String getData ( ) {

                  return mData;
            }

            public void setData ( String data ) {

                  mData = data;
            }

            @Override
            public String toString ( ) {

                  return "Thumbnail{" +
                      "mImageId='" + mImageId + '\'' +
                      ", mData='" + mData + '\'' +
                      '}';
            }
      }
}
