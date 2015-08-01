package ar.com.wolox.unstuckme.utils;

import android.content.Intent;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import ar.com.wolox.unstuckme.R;

/**
 * Created by agustinpagnoni on 8/1/15.
 */
public class ImageEraseListener implements View.OnClickListener{

        private int imageId;
        private View globalView;

        public ImageEraseListener(View v, int imageId) {
            this.imageId = imageId;
            this.globalView = v;
        }

        @Override
        public void onClick(View view) {
            ((ImageView) globalView.findViewById(imageId)).setImageDrawable(null);

        }
    }
