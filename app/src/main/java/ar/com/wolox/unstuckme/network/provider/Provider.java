package ar.com.wolox.unstuckme.network.provider;

import java.util.List;

import retrofit.Callback;

public interface Provider<T> {

    void provide(int currentPage, int itemsPerPage, Callback<List<T>> callback);

}