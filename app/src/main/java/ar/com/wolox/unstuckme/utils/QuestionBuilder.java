package ar.com.wolox.unstuckme.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.cloudinary.json.JSONObject;
import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ar.com.wolox.unstuckme.Configuration;
import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.UnstuckMeApplication;
import ar.com.wolox.unstuckme.fragment.create_question.FinishLoadingListener;
import ar.com.wolox.unstuckme.fragment.create_question.ShareQuestionFragment;
import ar.com.wolox.unstuckme.model.Question;
import ar.com.wolox.unstuckme.model.QuestionNew;
import ar.com.wolox.unstuckme.network.QuestionsService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by agustinpagnoni on 8/2/15.
 */
public class QuestionBuilder {

    private static List<InputStream> imagesStreams = new LinkedList<InputStream>();
    private static boolean privacy;



    private static Question questionReady;

    public static void setListener(FinishLoadingListener listener) {
        QuestionBuilder.listener = listener;
    }

    private static FinishLoadingListener listener;

    public static void putImage(ImageView imageView) {
        Bitmap bm = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] imageInByte = stream.toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(imageInByte);
        imagesStreams.add(bis);
    }

    public static void putPrivacy(boolean p) {
        privacy = p;
    }

    public static void resetBuilder() {
        imagesStreams = new LinkedList<InputStream>();
        privacy = false;
    }

    public static List<String> handleImageLoading() {
        List<String> cloudinaryUrls = new LinkedList<String>();
        Cloudinary cloudinary = UnstuckMeApplication.getCloudinary();
        Upload task = new Upload( cloudinary, imagesStreams );
        task.execute();
        return cloudinaryUrls;

    }

    private static class Upload extends AsyncTask<String, Void, List<String>> {

        private Cloudinary mCloudinary;
        private List<InputStream> mInputStreams;

        public Upload( Cloudinary cloudinary, List<InputStream> is ) {
            super();
            mCloudinary = cloudinary;
            mInputStreams = is;
        }
        @Override
        protected List<String> doInBackground(String... urls) {
            List<String> response = new LinkedList<String>();

            try {
                for(InputStream is : mInputStreams) {
                    JSONObject result = new JSONObject(mCloudinary.uploader().upload(is, ObjectUtils.emptyMap()));
                    response.add(mCloudinary.url().resourceType("image").type("upload").generate(result.get("public_id").toString()));
                }
                return response;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            //TODO
            if (result != null) {
                QuestionBuilder.onImagesUploaded(result);
            } else {
                // TODO handle error in upload
            }

        }
    }

    private static void onImagesUploaded(List<String> cloudinaryPaths) {
        QuestionNew question = new QuestionNew(cloudinaryPaths, privacy);

        UnstuckMeApplication.sQuestionsService.postQuestion(question, new Callback<QuestionNew>() {
            @Override
            public void success(QuestionNew jsonObject, Response response) {
                //TODO notify good
                String jsonResponse = new String(((TypedByteArray) response.getBody()).getBytes());
                Toast.makeText(UnstuckMeApplication.getAppContext(), R.string.create_questions_upload_ok, Toast.LENGTH_LONG).show();
                Gson gson = new Gson();
                Question question = gson.fromJson(jsonResponse, Question.class);
                if (listener != null) {
                    questionReady = question;
                    listener.onFinish();
                }
                else
                    onErrorToast();
            }

            @Override
            public void failure(RetrofitError error) {
                onErrorToast();
            }
        });
    }

    private static void onErrorToast() {
        Toast.makeText(UnstuckMeApplication.getAppContext(), R.string.error_network_create_questions_upload, Toast.LENGTH_LONG).show();
    }

    public static Question getQuestionReady() {
        return questionReady;
    }
}
