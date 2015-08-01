package ar.com.wolox.unstuckme.fragment.endless;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import ar.com.wolox.unstuckme.R;
import ar.com.wolox.unstuckme.listener.EndlessScrollListener;
import ar.com.wolox.unstuckme.listener.SmoothScrollable;
import ar.com.wolox.unstuckme.network.provider.Provider;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

abstract class AbstractEndlessScrollFragment<T> extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, SmoothScrollable {

    public static final int ELEMENTS_PER_PAGE = 20;

    protected static final int SCROLL_TO_TOP_LIMIT = 15;

    protected enum Status { REFRESHING, LOADING, LOADED, ERROR }

    protected Status mStatus;
    protected List<T> mList = new ArrayList<>();
    protected Deque<Integer> mLoadQueue = new LinkedList<>();
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected BaseAdapter mAdapter;
    protected Provider mProvider;
    protected EndlessScrollListener mEndlessScrollListener;
    protected View mLoadingView;

    private int mStartOffset = 0;

    protected abstract int layout();

    protected abstract BaseAdapter loadAdapter();

    protected abstract Provider loadProvider();

    protected abstract void showList();

    protected abstract void hideList();

    protected abstract void addFooter();

    protected abstract void removeFooter();

    protected abstract void setOnItemClickListener(AdapterView.OnItemClickListener listener);

    protected abstract boolean hasHeader();

    protected abstract void setEndlessScrollListenerToCollection(EndlessScrollListener listener);

    protected abstract void setAdapter(BaseAdapter adapter);

    public void setStartOffset(int startOffset) {
        this.mStartOffset = startOffset;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(layout(), container, false);
        setUi(v);
        init();
        populate();
        setListeners();
        return v;
    }

    @Override
    public void onRefresh() {
        reload();
    }

    public void reload() {
        if (mStatus == Status.REFRESHING
                || mStatus == Status.LOADING
                || mList == null
                || mAdapter == null) {
            return;
        }
        mStatus = Status.REFRESHING;
        mEndlessScrollListener.reset();
        clearQueue();
        mList.clear();
        mAdapter.notifyDataSetChanged();
        loadMore(1);
    }

    protected void setUi(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_view);
        mLoadingView = View.inflate(getActivity(), R.layout.loading, null);
    }

    protected void setListeners() {
        setRefreshListener(this);
    }

    protected void populate() {
        if (mList == null) mList = new ArrayList<>();
        loadMore(1);
    }

    protected void init() {
        mAdapter = loadAdapter();
        mProvider = loadProvider();
    }

    protected int getEndlessScrollAmount() {
        return ELEMENTS_PER_PAGE;
    }

    private void setRefreshListener(SwipeRefreshLayout.OnRefreshListener target) {
        mSwipeRefreshLayout.setOnRefreshListener(target);
        mEndlessScrollListener = new EndlessScrollListener(getEndlessScrollAmount()) {
            @Override
            public void onScrollMore(int currentPage) {
                loadMore(currentPage);
            }
        };
        setEndlessScrollListenerToCollection(mEndlessScrollListener);
    }

    private void setStatusLoading() {
        if (mStatus == Status.LOADING) return;
        if (mStatus != Status.REFRESHING) {
            addFooter();
        }
        mStatus = Status.LOADING;
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        showList();
        setAdapter(mAdapter);
    }

    private void setStatusLoaded() {
        if (mStatus == Status.LOADING) removeFooter();
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        if (!mList.isEmpty()) {
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            showList();
        }
        mStatus = Status.LOADED;
    }

    private void setStatusError() {
        if (mStatus == Status.LOADING) removeFooter();
        mSwipeRefreshLayout.setRefreshing(false);
        mStatus = Status.ERROR;
    }

    private void clearQueue() {
        mLoadQueue.clear();
    }

    private void loadMore(final int currentPage) {
        if (mStatus == Status.LOADING) {
            if (currentPage > 1) mLoadQueue.offer(currentPage);
            return;
        }
        setStatusLoading();
        mProvider.provide(currentPage, ELEMENTS_PER_PAGE,
                new Callback<List<T>>() {
                    @Override
                    public void success(List<T> list, Response response) {
                        if (currentPage == 1) {
                            mList.clear();
                        }
                        if (getActivity() != null &&  list != null && !list.isEmpty()) {
                            mList.addAll(list);
                            mAdapter.notifyDataSetChanged();
                        }
                        setStatusLoaded();
                        if (!mLoadQueue.isEmpty()) loadMore(mLoadQueue.poll());
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        setStatusError();
                    }
                }
        );
    }
}