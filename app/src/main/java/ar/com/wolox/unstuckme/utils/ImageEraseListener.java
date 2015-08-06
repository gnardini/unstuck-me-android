package ar.com.wolox.unstuckme.utils;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.fragment.create_question.CreateQuestionsFragment;


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


        /* Erasing a picture should perform the following:
            - Erase the pic
            - Remove the delete button
            - Set again the load image button
        */
        @Override
        public void onClick(View view) {
            ImageView iv = ((ImageView) globalView.findViewById(imageId));
            CreateQuestionsFragment.togglePlaceholderVisibility(iv, View.VISIBLE);
            iv.setImageDrawable(null);
            mEraseView.setVisibility(View.GONE);
        }
}
