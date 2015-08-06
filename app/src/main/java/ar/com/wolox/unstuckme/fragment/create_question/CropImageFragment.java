package ar.com.wolox.unstuckme.fragment.create_question;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.theartofdev.edmodo.cropper.CropImageView;

import org.w3c.dom.Text;

import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.activity.MainActivity;

/**
 * Created by agustinpagnoni on 8/4/15.
 */
public class CropImageFragment extends Fragment {

    // Static final constants
    private static final int DEFAULT_ASPECT_RATIO_VALUES = 20;

    private static final int ROTATE_NINETY_DEGREES = 90;


    private TextView mRotateButton;
    private CropImageView mCropImageView;
    private Bitmap croppedImage;
    private Uri originalUri;
    private CroppedImageHandler handler;


    public static CropImageFragment newInstance(Uri uri, CroppedImageHandler cih) {
        CropImageFragment f = new CropImageFragment();
        f.originalUri = uri;
        f.handler = cih;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crop_image, container, false);
        setUi(v);
        init();
        setListeners(v);
        return v;
    }

    private void setUi(View v) {
        // Crop image
        mCropImageView = (CropImageView) v.findViewById(R.id.CropImageView);
        mCropImageView.setImageUri(originalUri);
        mCropImageView.setAspectRatio(20,20);
        mCropImageView.setFixedAspectRatio(true);
        // Rotate
        mRotateButton = (TextView) v.findViewById(R.id.Button_rotate);
    }

    private void init() {
        mCropImageView.setCropShape(CropImageView.CropShape.RECTANGLE);
    }

    private void setListeners(View v) {
        mRotateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCropImageView.rotateImage(ROTATE_NINETY_DEGREES);
            }
        });

        v.findViewById(R.id.Button_crop).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                croppedImage = mCropImageView.getCroppedImage();
                handler.onCroppedImage(croppedImage);
                removeItself();
            }
        });
    }

    private void removeItself() {
        ((MainActivity)getActivity()).removeFragment(this);
    }
}
