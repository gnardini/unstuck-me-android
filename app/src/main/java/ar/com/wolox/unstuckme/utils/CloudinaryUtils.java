package ar.com.wolox.unstuckme.utils;

import com.cloudinary.Transformation;

import ar.com.wolox.unstuckme.Configuration;
import ar.com.wolox.unstuckme.UnstuckMeApplication;

public class CloudinaryUtils {

    //SETTINGS
    private static final int QUALITY_HIGH = 90;
    private static final int QUALITY_MID = 80;
    private static final int QUALITY_LOW = 65;
    private static final String CROP_LIMIT = "limit";
    private static final String PREFERED_FORMAT = ".webp";

    private static String cleanImageUrl(String originalUrl) {
        String imageId = "";
        if (null != originalUrl && originalUrl.length() > 0 )
        {
            int endIndex = originalUrl.lastIndexOf("/");
            if (endIndex != -1) {
                imageId = originalUrl.substring(endIndex + 1, originalUrl.length());
            }
        }
        return imageId;
    }
    public static String getReducedImage(String uncompressedUrl) {
        return UnstuckMeApplication.getCloudinary()
                .url()
                .transformation(new Transformation()
                        .width(Configuration.SCREEN_BASE_WIDTH / 2)
                        .height(Configuration.SCREEN_BASE_HEIGHT / 2)
                        .crop(CROP_LIMIT)
                        .quality(QUALITY_MID))
                .generate(cleanImageUrl(uncompressedUrl) + PREFERED_FORMAT);
    }

    public static String getFullScreenImage(String uncompressedUrl) {
        return UnstuckMeApplication.getCloudinary()
                .url()
                .transformation(new Transformation()
                        .width(Configuration.SCREEN_BASE_WIDTH)
                        .height(Configuration.SCREEN_BASE_HEIGHT)
                        .crop(CROP_LIMIT)
                        .quality(QUALITY_HIGH))
                .generate(cleanImageUrl(uncompressedUrl) + PREFERED_FORMAT);
    }

}
