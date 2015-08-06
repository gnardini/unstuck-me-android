package ar.com.wolox.unstuckme.fragment.create_question;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.utils.AnimationsHelper;
import ar.com.wolox.unstuckme.utils.ImageEraseListener;
import ar.com.wolox.unstuckme.utils.ImageUploadListener;
import ar.com.wolox.unstuckme.utils.QuestionBuilder;

public class CreateQuestionsFragment extends Fragment implements CroppedImageHandler{

    private static final int IMAGE_UPLOAD_1 = 11;
    private static final int IMAGE_UPLOAD_2 = 12;
    private static final int IMAGE_UPLOAD_3 = 13;
    private static final int IMAGE_UPLOAD_4 = 14;
    public static final String TAG = "IMAGE_UPLOAD_FRAGMENT";
    private static final int MIN_IMAGES_TO_UPLOAD = 2;
    private static int RESULT_LOAD_IMAGE = 1;

    ImageView mImageUpload1;
    ImageView mImageUpload2;
    ImageView mImageUpload3;
    ImageView mImageUpload4;
    List<ImageView> mImagesToUpload = new LinkedList<ImageView>();

    ImageView mImagePlaceholder1;
    ImageView mImagePlaceholder2;
    ImageView mImagePlaceholder3;
    ImageView mImagePlaceholder4;

    ImageButton mImageErase1;
    ImageButton mImageErase2;
    ImageButton mImageErase3;
    ImageButton mImageErase4;

    ImageView mReadyButton;
    View.OnClickListener mOpenGallery;
    View.OnClickListener mEraseImageListener;
    private int lastCode;


    @Override
    public void onResume() {
        super.onResume();
    }


    public static CreateQuestionsFragment newInstance() {
        CreateQuestionsFragment f = new CreateQuestionsFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_questions, container, false);
        setUi(v);
        setListeners(v);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        add(mImageUpload1).add(mImageUpload2).add(mImageUpload3).add(mImageUpload4);
    }

    private void setUi(View v) {
        mImageUpload1 = (ImageView) v.findViewById(R.id.create_questions_image_upload_1);
        mImageUpload2 = (ImageView) v.findViewById(R.id.create_questions_image_upload_2);
        mImageUpload3 = (ImageView) v.findViewById(R.id.create_questions_image_upload_3);
        mImageUpload4 = (ImageView) v.findViewById(R.id.create_questions_image_upload_4);


        mImagePlaceholder1 = (ImageView) v.findViewById(R.id.create_questions_placeholder_1);
        mImagePlaceholder2 = (ImageView) v.findViewById(R.id.create_questions_placeholder_2);
        mImagePlaceholder3 = (ImageView) v.findViewById(R.id.create_questions_placeholder_3);
        mImagePlaceholder4 = (ImageView) v.findViewById(R.id.create_questions_placeholder_4);

        mImageErase1 = (ImageButton) v.findViewById(R.id.create_questions_image_erase_1);
        mImageErase2 = (ImageButton) v.findViewById(R.id.create_questions_image_erase_2);
        mImageErase3 = (ImageButton) v.findViewById(R.id.create_questions_image_erase_3);
        mImageErase4 = (ImageButton) v.findViewById(R.id.create_questions_image_erase_4);

        mReadyButton = (ImageView) v.findViewById(R.id.upload_ready);
        mReadyButton.setVisibility(View.GONE);
    }

    private void setListeners(final View view) {

        mImageUpload1.setOnClickListener(new ImageUploadListener((Fragment) this, IMAGE_UPLOAD_1));
        mImageUpload2.setOnClickListener(new ImageUploadListener((Fragment) this, IMAGE_UPLOAD_2));
        mImageUpload3.setOnClickListener(new ImageUploadListener((Fragment) this, IMAGE_UPLOAD_3));
        mImageUpload4.setOnClickListener(new ImageUploadListener((Fragment) this, IMAGE_UPLOAD_4));

        mImageErase1.setOnClickListener(new ImageEraseListener(view,
                R.id.create_questions_image_upload_1
                , mImageErase1));
        mImageErase2.setOnClickListener(new ImageEraseListener(view,
                R.id.create_questions_image_upload_2,
                mImageErase2));
        mImageErase3.setOnClickListener(new ImageEraseListener(view,
                R.id.create_questions_image_upload_3,
                mImageErase3));
        mImageErase4.setOnClickListener(new ImageEraseListener(view,
                R.id.create_questions_image_upload_4,
                mImageErase4));

        mReadyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mImageUpload1.getDrawable() != null)
                    QuestionBuilder.putImage(mImageUpload1);
                if (mImageUpload2.getDrawable() != null)
                    QuestionBuilder.putImage(mImageUpload2);
                if (mImageUpload3.getDrawable() != null)
                    QuestionBuilder.putImage(mImageUpload3);
                if (mImageUpload4.getDrawable() != null)
                    QuestionBuilder.putImage(mImageUpload4);
                if (countEffectiveImages() >= MIN_IMAGES_TO_UPLOAD) {
                    AnimationsHelper.flyPlane(mReadyButton).setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation arg0) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation arg0) {
                        }

                        @Override
                        public void onAnimationEnd(Animation arg0) {
                            getFragmentManager()
                                    .beginTransaction()
                                    .add(R.id.create_questions_container, PrivacyQuestionsFragment.newInstance())
                                    .addToBackStack(TAG)
                                    .commit();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), R.string.error_create_question_not_enough_images, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private int countEffectiveImages() {
        int count = 0;
        for(ImageView iv : mImagesToUpload) {
            if (iv != null && iv.getDrawable() != null)
                count++;
        }
        return count;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean codeOk = requestCode == IMAGE_UPLOAD_1 ||
                requestCode == IMAGE_UPLOAD_2 ||
                requestCode == IMAGE_UPLOAD_3 ||
                requestCode == IMAGE_UPLOAD_4;

        if (codeOk && resultCode == getActivity().RESULT_OK && null != data) {
            Uri selectedImageUri = data.getData();
            lastCode = requestCode;
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.create_questions_container, CropImageFragment.newInstance(selectedImageUri, this))
                    .commit();
        }
    }

    private CreateQuestionsFragment add(ImageView iv) {
        mImagesToUpload.add(iv);
        return this;
    }

    private void setReadyButton() {
        if (countEffectiveImages() >= MIN_IMAGES_TO_UPLOAD) {
            mReadyButton.setVisibility(View.VISIBLE);
            AnimationsHelper.scaleDecelerate(mReadyButton);
        } else {
            mReadyButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCroppedImage(Bitmap cropped) {

        ImageView imageView = null;
        ImageView placeholderView = null;

        switch (lastCode) {
            case IMAGE_UPLOAD_1:
                imageView = (ImageView) getActivity().findViewById(R.id.create_questions_image_upload_1);
                mImageErase1.setVisibility(View.VISIBLE);
                break;
            case IMAGE_UPLOAD_2:
                imageView = (ImageView) getActivity().findViewById(R.id.create_questions_image_upload_2);
                mImageErase2.setVisibility(View.VISIBLE);
                break;
            case IMAGE_UPLOAD_3:
                imageView = (ImageView) getActivity().findViewById(R.id.create_questions_image_upload_3);
                mImageErase3.setVisibility(View.VISIBLE);
                break;
            case IMAGE_UPLOAD_4:
                imageView = (ImageView) getActivity().findViewById(R.id.create_questions_image_upload_4);
                mImageErase4.setVisibility(View.VISIBLE);
                break;
        }

        if (imageView != null) {
            imageView.setImageBitmap(cropped);
            togglePlaceholderVisibility(imageView, View.INVISIBLE);
        }

        setReadyButton();
    }

    public static void togglePlaceholderVisibility(ImageView iv, int visibility) {
        if (iv == null)
            return;
        ViewGroup parent = (ViewGroup) iv.getParent();
        // The scope changed to the parent, so there will be only one [TESTED]
        parent.findViewWithTag("placeholder").setVisibility(visibility);
    }
}
