package ar.com.wolox.unstuckme;

import android.app.Application;
import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ar.com.wolox.unstuckme.network.QuestionsService;
import ar.com.wolox.unstuckme.network.interceptor.SecureRequestInterceptor;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class UnstuckMeApplication extends Application {

    private static Context sContext;
    private static RequestInterceptor sSecureRequestInterceptor;

    public static QuestionsService sQuestionsService;

    static {
        buildRestServices();
    }


    public static void buildRestServices() {
        sSecureRequestInterceptor = new SecureRequestInterceptor();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
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

        sQuestionsService = apiaryAdapter.create(QuestionsService.class);
    }

    public static Context getAppContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }
}
