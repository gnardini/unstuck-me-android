package ar.com.wolox.unstuckme.utils;

import android.view.View;
import android.widget.ImageView;


public class ImageEraseListener implements View.OnClickListener{

        public interface EraseListenerCallback {
            void onImageErased();
    }

        private int imageId;
        private View globalView;
        private View mEraseView;

        public ImageEraseListener(View v,
                                  int imageId,
                                  View eraseView) {
            this.imageId = imageId;
            this.globalView = v;
            this.mEraseView = eraseView;
        }

        @Override
        public void onClick(View view) {
            ((ImageView) globalView.findViewById(imageId)).setImageDrawable(null);
            mEraseView.setVisibility(View.GONE);
        }
    }
