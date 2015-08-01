package ar.com.wolox.unstuckme.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by agustinpagnoni on 8/1/15.
 */
public class ImageUploadListener implements View.OnClickListener{

    private int code;
    private Fragment context;

    public ImageUploadListener(Fragment context, int code) {
        this.context = context;
        this.code = code;
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        context.startActivityForResult(i, code);
    }
}
