package ar.com.wolox.unstuckme.network.interceptor;

import retrofit.RequestInterceptor;

public class SecureRequestInterceptor implements RequestInterceptor {

    private static final String USER_AGENT_HEADER = "User-Agent";
    private static final String ACCEPT_HEADER = "Accept";

    public void intercept(RequestFacade request) {
        request.addHeader(USER_AGENT_HEADER, "Android");
        request.addHeader(ACCEPT_HEADER, "application/json");
    }
}