package ar.com.wolox.unstuckme.fragment.create_question;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.utils.ImageEraseListener;
import ar.com.wolox.unstuckme.utils.ImageUploadListener;

public class CreateQuestionsFragment extends Fragment {

    private static final int IMAGE_UPLOAD_1 = 11;
    private static final int IMAGE_UPLOAD_2 = 12;
    private static final int IMAGE_UPLOAD_3 = 13;
    private static final int IMAGE_UPLOAD_4 = 14;
    private static final String TAG = "IMAGE_UPLOAD";
    private static int RESULT_LOAD_IMAGE = 1;

    ImageView mImageUpload1;
    ImageView mImageUpload2;
    ImageView mImageUpload3;
    ImageView mImageUpload4;

    ImageButton mImageErase1;
    ImageButton mImageErase2;
    ImageButton mImageErase3;
    ImageButton mImageErase4;

    ImageButton mReadyButton;
    View.OnClickListener mOpenGallery;
    View.OnClickListener mEraseImageListener;

    private Fragment containerFragment;


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
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.create_questions_container, PrivacyQuestionsFragment.newInstance())
                        .addToBackStack(TAG)
                        .commit();
            }
        });
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
            if (imageView != null)
                imageView.setImageURI(selectedImageUri);
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
}
