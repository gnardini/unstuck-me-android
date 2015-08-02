package ar.com.wolox.unstuckme.utils;

import com.cloudinary.Transformation;

import ar.com.wolox.unstuckme.UnstuckMeApplication;

public class CloudinaryUtils {

    public static String getQuestionCompressedImage(String uncompressedUrl) {
        String imageId = "";
        if (null != uncompressedUrl && uncompressedUrl.length() > 0 )
        {
            int endIndex = uncompressedUrl.lastIndexOf("/");
            if (endIndex != -1) {
                imageId = uncompressedUrl.substring(endIndex + 1, uncompressedUrl.length());
            }
        }

        return UnstuckMeApplication.getCloudinary()
                .url()
                .transformation(new Transformation()
                        .width(200)
                        .height(400)
                        .crop("fill")
                        .gravity("face:center")
                        .quality(80))
                .generate(imageId + ".webp");
    }

}
