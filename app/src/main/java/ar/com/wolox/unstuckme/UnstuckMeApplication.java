package ar.com.wolox.unstuckme;

import android.app.Application;
import android.content.Context;

import com.cloudinary.Cloudinary;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.parse.Parse;
import com.parse.ParseInstallation;

import java.util.HashMap;
import java.util.Map;

import ar.com.wolox.unstuckme.model.QuestionNew;
import ar.com.wolox.unstuckme.network.QuestionsService;
import ar.com.wolox.unstuckme.network.interceptor.SecureRequestInterceptor;
import ar.com.wolox.unstuckme.network.serializer.QuestionNewSerializer;
import ar.com.wolox.unstuckme.utils.PushNotificationUtils;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class UnstuckMeApplication extends Application {

    private static Context sContext;
    private static RequestInterceptor sSecureRequestInterceptor;
    private static Cloudinary sCloudinary;

    public static QuestionsService sQuestionsService;

    static {
        buildRestServices();
    }

    public static void buildRestServices() {
        sSecureRequestInterceptor = new SecureRequestInterceptor();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(QuestionNew.class, new QuestionNewSerializer())
                .create();
        RestAdapter apiaryAdapter = new RestAdapter.Builder()
                .setEndpoint(Configuration.APIARY_ENDPOINT)
                .setConverter(new GsonConverter(gson))
                .setRequestInterceptor(sSecureRequestInterceptor)
                .build();
        RestAdapter apiAdapter = new RestAdapter.Builder()
                .setEndpoint(Configuration.API_ENDPOINT)
                .setConverter(new GsonConverter(gson))
                .setRequestInterceptor(sSecureRequestInterceptor)
                .build();

        apiAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        sQuestionsService = apiAdapter.create(QuestionsService.class);

        sCloudinary = configCloudinary();
    }

    private static Cloudinary configCloudinary() {
        Map config = new HashMap();
        config.put("cloud_name", Configuration.CLOUDINARY_NAME);
        config.put("api_key", Configuration.CLOUDINARY_KEY);
        config.put("api_secret", Configuration.CLOUDINARY_SECRET);
        return new Cloudinary(config);
    }

    public static Context getAppContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        setupParse();
    }

    private void setupParse() {
        Parse.initialize(this, Configuration.PARSE_APP_ID, Configuration.PARSE_CLIENT_KEY);
        PushNotificationUtils.subscribe();
        Parse.setLogLevel(Parse.LOG_LEVEL_NONE);
    }

    public static Cloudinary getCloudinary() {
        return sCloudinary;
    }

}
