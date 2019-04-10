package tech.threekilogram.mediaplayertest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import java.util.List;
import tech.threekilogram.mediaplayertest.action.QueryImages;
import tech.threekilogram.mediaplayertest.action.QueryImages.Image;

/**
 * @author liujin
 */
public class Main2Activity extends AppCompatActivity {

      private static final String TAG = Main2Activity.class.getSimpleName();

      @Override
      protected void onCreate ( Bundle savedInstanceState ) {

            super.onCreate( savedInstanceState );
            setContentView( R.layout.activity_main2 );
      }

      public void queryImages ( View view ) {

            List<Image> images = QueryImages.queryImagesExcludeGif( this );
            for( Image image : images ) {
                  Log.e( TAG, "queryImages : " + image );
            }
      }
}
