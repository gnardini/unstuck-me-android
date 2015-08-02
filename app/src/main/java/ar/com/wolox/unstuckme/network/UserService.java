package ar.com.wolox.unstuckme.network;

import ar.com.wolox.unstuckme.Configuration;
import ar.com.wolox.unstuckme.model.User;
import retrofit.Callback;
import retrofit.http.GET;

public interface UserService {

    @GET(Configuration.API_PREFIX + "users")
    void getUserStats(Callback<User> cb);
}
