package ar.com.wolox.unstuckme.network.interceptor;

import ar.com.wolox.unstuckme.utils.AccessUtils;
import retrofit.RequestInterceptor;

public class SecureRequestInterceptor implements RequestInterceptor {

    private static final String USER_AGENT_HEADER = "User-Agent";
    private static final String ACCEPT_HEADER = "Accept";
    private static final String TOKEN = "token";

    public void intercept(RequestFacade request) {
        request.addHeader(USER_AGENT_HEADER, "Android");
        request.addHeader(ACCEPT_HEADER, "application/json");
        request.addHeader(TOKEN, AccessUtils.getDeviceId());
    }
}