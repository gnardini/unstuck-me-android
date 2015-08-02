package ar.com.wolox.unstuckme.fragment.create_question;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ar.com.wolox.unstuckme.Configuration;
import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.UnstuckMeApplication;
import ar.com.wolox.unstuckme.model.Option;
import ar.com.wolox.unstuckme.utils.ImageEraseListener;
import ar.com.wolox.unstuckme.utils.ImageUploadListener;
import ar.com.wolox.unstuckme.utils.QuestionBuilder;

public class CreateQuestionsFragment extends Fragment {

    private static final int IMAGE_UPLOAD_1 = 11;
    private static final int IMAGE_UPLOAD_2 = 12;
    private static final int IMAGE_UPLOAD_3 = 13;
    private static final int IMAGE_UPLOAD_4 = 14;
    private static final String TAG = "IMAGE_UPLOAD";
    private static final int MIN_IMAGES_TO_UPLOAD = 2;
    private static int RESULT_LOAD_IMAGE = 1;

    ImageView mImageUpload1;
    ImageView mImageUpload2;
    ImageView mImageUpload3;
    ImageView mImageUpload4;
    List<ImageView> mImagesToUpload = new LinkedList<ImageView>();

    ImageButton mImageErase1;
    ImageButton mImageErase2;
    ImageButton mImageErase3;
    ImageButton mImageErase4;

    ImageButton mReadyButton;
    View.OnClickListener mOpenGallery;
    View.OnClickListener mEraseImageListener;


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
        this.getActivity();
    }

    private void setUi(View v) {
        mImageUpload1 = (ImageView) v.findViewById(R.id.create_questions_image_upload_1);
        mImageUpload2 = (ImageView) v.findViewById(R.id.create_questions_image_upload_2);
        mImageUpload3 = (ImageView) v.findViewById(R.id.create_questions_image_upload_3);
        mImageUpload4 = (ImageView) v.findViewById(R.id.create_questions_image_upload_4);
        mImageErase1 = (ImageButton) v.findViewById(R.id.create_questions_image_erase_1);
        mImageErase2 = (ImageButton) v.findViewById(R.id.create_questions_image_erase_2);
        mImageErase3 = (ImageButton) v.findViewById(R.id.create_questions_image_erase_3);
        mImageErase4 = (ImageButton) v.findViewById(R.id.create_questions_image_erase_4);
        mReadyButton = (ImageButton) v.findViewById(R.id.upload_ready);
    }

    private void setListeners(View v) {
        mImageUpload1.setOnClickListener(new ImageUploadListener((Fragment) this, IMAGE_UPLOAD_1));
        mImageUpload2.setOnClickListener(new ImageUploadListener((Fragment) this, IMAGE_UPLOAD_2));
        mImageUpload3.setOnClickListener(new ImageUploadListener((Fragment) this, IMAGE_UPLOAD_3));
        mImageUpload4.setOnClickListener(new ImageUploadListener((Fragment) this, IMAGE_UPLOAD_4));

        mImageErase1.setOnClickListener(new ImageEraseListener(v, R.id.create_questions_image_upload_1));
        mImageErase2.setOnClickListener(new ImageEraseListener(v, R.id.create_questions_image_upload_2));
        mImageErase3.setOnClickListener(new ImageEraseListener(v, R.id.create_questions_image_upload_3));
        mImageErase4.setOnClickListener(new ImageEraseListener(v, R.id.create_questions_image_upload_4));

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
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.create_questions_container, PrivacyQuestionsFragment.newInstance())
                            .addToBackStack(TAG)
                            .commit();
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
            String picturePath = getPath(selectedImageUri);

            ImageView imageView = null;

            switch (requestCode) {
                case IMAGE_UPLOAD_1:
                    imageView = (ImageView) getActivity().findViewById(R.id.create_questions_image_upload_1);
                    break;
                case IMAGE_UPLOAD_2:
                    imageView = (ImageView) getActivity().findViewById(R.id.create_questions_image_upload_2);
                    break;
                case IMAGE_UPLOAD_3:
                    imageView = (ImageView) getActivity().findViewById(R.id.create_questions_image_upload_3);
                    break;
                case IMAGE_UPLOAD_4:
                    imageView = (ImageView) getActivity().findViewById(R.id.create_questions_image_upload_4);
                    break;
            }

            if (imageView != null) {
//                imageView.setImageDrawable();
                imageView.setImageBitmap(decodeFile(picturePath));
//                imageView.setImageURI(selectedImageUri);
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri,
                projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private CreateQuestionsFragment add(ImageView iv) {
        mImagesToUpload.add(iv);
        return this;
    }

    // Decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(String path) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(path), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE=150;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(path), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }
}
