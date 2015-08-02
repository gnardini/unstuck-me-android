package ar.com.wolox.unstuckme.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.cloudinary.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import ar.com.wolox.unstuckme.Configuration;
import ar.com.wolox.unstuckme.UnstuckMeApplication;

/**
 * Created by agustinpagnoni on 8/2/15.
 */
public class QuestionBuilder {

    private static List<InputStream> imagesStreams = new LinkedList<InputStream>();
    private static boolean privacy;

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
            }

        }
    }

    private static void onImagesUploaded(List<String> cloudinaryPaths) {


    }
}
