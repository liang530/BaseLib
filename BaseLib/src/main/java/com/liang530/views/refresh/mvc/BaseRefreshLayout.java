/*
Copyright 2015 shizhefei（LuckyJayce）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.liang530.views.refresh.mvc;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ListView;

import com.liang530.views.refresh.mvc.ILoadViewFactory.ILoadMoreView;
import com.liang530.views.refresh.mvc.ILoadViewFactory.ILoadView;
import com.liang530.views.refresh.mvc.IRefreshView.OnRefreshListener;
import com.liang530.views.refresh.mvc.imp.HsLoadViewFactory;
import com.liang530.views.refresh.mvc.viewhandler.ListViewHandler;
import com.liang530.views.refresh.mvc.viewhandler.RecyclerViewHandler;
import com.liang530.views.refresh.mvc.viewhandler.ViewHandler;
import com.liang530.views.refresh.recyclerview.HFRecyclerAdapter;
import com.liang530.views.refresh.utils.NetworkUtils;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * <h1>下拉刷新，上滑加载更多的控件的辅助类</h1><br>
 * <br>
 * 刷新，加载更多规则<br>
 * 当用户下拉刷新时，会取消掉当前的刷新，以及加载更多的任务<br>
 * 当用户加载更多的时候，如果有已经正在刷新或加载更多是不会再执行加载更多的操作。<br>
 * <br>
 * 注意:记得在Activity的Ondestroy方法调用destory <br>
 * 要添加 android.permission.ACCESS_NETWORK_STATE 权限，这个用来检测是否有网络
 *
 * @param <DATA>
 * @author LuckyJayce
 */
public class BaseRefreshLayout<DATA> {
    public static final int NOMAL=0;
    public static final int REFRESHING=1;
    public static final int LOAD_MORE=2;
    private int currentLoadState=NOMAL;
    private IDataAdapter<DATA> dataAdapter;
    private IRefreshView refreshView;
    private View contentView;
    private Context context;
    private MOnStateChangeListener<DATA> onStateChangeListener = new MOnStateChangeListener<DATA>();
    private long loadDataTime = -1;
    private Integer autoLastLoadMorePosition=8;
    private int page=1;
    private int requestPage=1;
    /**
     * 是否还有更多数据。如果服务器返回的数据为空的话，就说明没有更多数据了，也就没必要自动加载更多数据
     */
    private boolean hasMoreData = true;
    /***
     * 加载更多的时候是否事先检查网络是否可用。
     */
    private boolean needCheckNetwork = true;
    private ILoadViewFactory.ILoadView mLoadView;
    private ILoadViewFactory.ILoadMoreView mLoadMoreView;

    public static ILoadViewFactory loadViewFactory = new HsLoadViewFactory();

    private ListViewHandler listViewHandler = new ListViewHandler();

    private RecyclerViewHandler recyclerViewHandler = new RecyclerViewHandler();
    private AdvRefreshListener advRefreshListener;
    private Handler handler;

    public BaseRefreshLayout(IRefreshView refreshView) {
        this(refreshView, loadViewFactory.madeLoadView(), loadViewFactory.madeLoadMoreView());
    }

    public BaseRefreshLayout(IRefreshView refreshView, ILoadViewFactory.ILoadView loadView) {
        this(refreshView, loadView, null);
    }

    public BaseRefreshLayout(IRefreshView refreshView, ILoadView loadView, ILoadMoreView loadMoreView) {
        super();
        this.context = refreshView.getContentView().getContext().getApplicationContext();
        this.autoLoadMore = true;
        this.refreshView = refreshView;
        contentView = refreshView.getContentView();
        contentView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        refreshView.setOnRefreshListener(onRefreshListener);
        mLoadView = loadView;
        mLoadMoreView = loadMoreView;
        mLoadView.init(refreshView.getSwitchView(), onClickRefresListener);
        handler = new Handler();
    }
    public void setCanLoadMore(boolean canLoadMore){
        setAutoLoadMore(false);
        mLoadMoreView.setCanLoadMore(canLoadMore);
    }
    /**
     * 设置LoadView的factory，用于创建使用者自定义的加载失败，加载中，加载更多等布局
     *
     * @param fractory
     */
    public static void setLoadViewFractory(ILoadViewFactory fractory) {
        loadViewFactory = fractory;
    }

    /**
     * 如果不是网络请求的业务可以把这个设置为false
     *
     * @param needCheckNetwork
     */
    public void setNeedCheckNetwork(boolean needCheckNetwork) {
        this.needCheckNetwork = needCheckNetwork;
    }

    public Integer getAutoLastLoadMorePosition() {
        return autoLastLoadMorePosition;
    }

    /**
     * 设置倒数第几个预加载下一页
     * @param autoLastLoadMorePosition
     */
    public void setAutoLastLoadMorePosition(Integer autoLastLoadMorePosition) {
        this.autoLastLoadMorePosition = autoLastLoadMorePosition;
    }

    public AdvRefreshListener getAdvRefreshListener() {
        return advRefreshListener;
    }

    public void setAdvRefreshListener(AdvRefreshListener advRefreshListener) {
        this.advRefreshListener = advRefreshListener;
        if(refreshView.getSwitchView() instanceof PtrClassicFrameLayout){
            PtrClassicFrameLayout switchView = (PtrClassicFrameLayout) refreshView.getSwitchView();
            switchView.setLastUpdateTimeKey(advRefreshListener.getClass().getName());
        }
    }
    public void setAdvRefreshListener(AdvRefreshListener advRefreshListener,String LastUpdateTimeKey) {
        this.advRefreshListener = advRefreshListener;
        if(refreshView.getSwitchView() instanceof PtrClassicFrameLayout){
            PtrClassicFrameLayout switchView = (PtrClassicFrameLayout) refreshView.getSwitchView();
            switchView.setLastUpdateTimeKey(LastUpdateTimeKey);
        }
    }

    /**
     * 设置适配器，用于显示数据
     *
     * @param adapter
     */
    public void setAdapter(IDataAdapter<DATA> adapter) {
        View view = getContentView();
        hasInitLoadMoreView = false;
        if (view instanceof ListView) {
            hasInitLoadMoreView = listViewHandler.handleSetAdapter(view, adapter, mLoadMoreView, onClickLoadMoreListener);
            listViewHandler.setOnScrollBottomListener(view,autoLastLoadMorePosition, onScrollBottomListener);
        } else if (view instanceof RecyclerView) {
            hasInitLoadMoreView = recyclerViewHandler.handleSetAdapter(view, adapter, mLoadMoreView, onClickLoadMoreListener);
            recyclerViewHandler.setOnScrollBottomListener(view,autoLastLoadMorePosition, onScrollBottomListener);
        }
        this.dataAdapter = adapter;
    }

    public void setAdapter(IDataAdapter<DATA> adapter, ViewHandler viewHandler) {
        hasInitLoadMoreView = false;
        if (viewHandler != null) {
            View view = getContentView();
            hasInitLoadMoreView = viewHandler.handleSetAdapter(view, adapter, mLoadMoreView, onClickLoadMoreListener);
            viewHandler.setOnScrollBottomListener(view,autoLastLoadMorePosition, onScrollBottomListener);
        }
        this.dataAdapter = adapter;
    }

    private boolean hasInitLoadMoreView = false;

    /**
     * 设置状态监听，监听开始刷新，刷新成功，开始加载更多，加载更多成功
     *
     * @param onStateChangeListener
     */
    public void setOnStateChangeListener(OnStateChangeListener<DATA> onStateChangeListener) {
        this.onStateChangeListener.setOnStateChangeListener(onStateChangeListener);
    }

    /**
     * 设置状态监听，监听开始刷新，刷新成功
     *
     * @param onRefreshStateChangeListener
     */
    public void setOnStateChangeListener(OnRefreshStateChangeListener<DATA> onRefreshStateChangeListener) {
        this.onStateChangeListener.setOnRefreshStateChangeListener(onRefreshStateChangeListener);
    }

    /**
     * 设置状态监听，监听开始加载更多，加载更多成功
     *
     * @param onLoadMoreStateChangeListener
     */
    public void setOnStateChangeListener(OnLoadMoreStateChangeListener<DATA> onLoadMoreStateChangeListener) {
        this.onStateChangeListener.setOnLoadMoreStateChangeListener(onLoadMoreStateChangeListener);
    }
    public void resultLoadData(DATA result,Integer maxPage){
        if(maxPage==null) maxPage=Integer.MAX_VALUE;
        if(requestPage==1){
            onEndRefreshExecute(result,maxPage);
        }else{
            onPostLoadMoreExecute(result,maxPage);
        }
    }
    public void resultLoadData(DATA result,int maxCount,int pageSize){
        int maxPage = (int)Math.ceil(maxCount / (double)pageSize);
        if(requestPage==1){
            onEndRefreshExecute(result,maxPage);
        }else{
            onPostLoadMoreExecute(result,maxPage);
        }
    }

    private Runnable showRefreshing;
    protected void onPreRefreshExecute() {
        if (hasInitLoadMoreView && mLoadMoreView != null) {
            mLoadMoreView.showNormal();
        }
        handler.post(showRefreshing = new Runnable() {

            @Override
            public void run() {
                if (dataAdapter.isEmpty()) {
                    mLoadView.showLoading();
                    refreshView.showRefreshComplete();
                } else {
                    mLoadView.restore();
                    refreshView.showRefreshing();

                }
            }
        });
        onStateChangeListener.onStartRefresh(dataAdapter);
        currentLoadState=REFRESHING;
        this.requestPage=1;
    }
    protected void onEndRefreshExecute(DATA result,int maxPage) {
        handler.removeCallbacks(showRefreshing);
        if (result == null) {
            if (dataAdapter.isEmpty()) {
                mLoadView.showFail(null);
            } else {
                mLoadView.tipFail(null);
            }
        } else {
            loadDataTime = System.currentTimeMillis();
            dataAdapter.notifyDataChanged(result, true);
            if (dataAdapter.isEmpty()) {
                mLoadView.showEmpty();
            } else {
                mLoadView.restore();
            }
            this.page=1;
            if (hasInitLoadMoreView && mLoadMoreView != null) {
                this.hasMoreData=page<maxPage;
                if (hasMoreData) {
                    mLoadMoreView.showNormal();
                } else {
                    mLoadMoreView.showNomore();
                }
            }

        }
        onStateChangeListener.onEndRefresh(dataAdapter, result);
        refreshView.showRefreshComplete();
        this.currentLoadState=NOMAL;
    }
    /**
     * 刷新，开启异步线程，并且显示加载中的界面，当数据加载完成自动还原成加载完成的布局，并且刷新列表数据
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void refresh() {
        if (dataAdapter == null ||advRefreshListener == null) {
            if (refreshView != null) {
                refreshView.showRefreshComplete();
            }
            return;
        }
        if(advRefreshListener!=null){
            onPreRefreshExecute();
            advRefreshListener.getPageData(1);

        }
//        if (dataSource != null) {
//            if (asyncTask != null && asyncTask.getStatus() != AsyncTask.Status.FINISHED) {
//                asyncTask.cancel(true);
//            }
//            asyncTask = new RefreshAsyncTask(dataSource, dataAdapter);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            } else {
//                asyncTask.execute();
//            }
//        } else {
//            if (cancle != null) {
//                cancle.cancle();
//                cancle = null;
//            }
//            MResponseSender responseSender = new RefreshResponseSender(asyncDataSource, dataAdapter);
//            responseSender.onPreExecute();
//            cancle = responseSender.execute();
//        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void loadMore() {
        if (isLoading()) {
            return;
        }
        if (dataAdapter.isEmpty()) {
            refresh();
            return;
        }
        if (dataAdapter == null || advRefreshListener== null) {
            if (refreshView != null) {
                refreshView.showRefreshComplete();
            }
            return;
        }
        if(advRefreshListener!=null){
            onPreLoadMoreExecute();
            advRefreshListener.getPageData(page+1);
        }
    }

    /**
     * 做销毁操作，比如关闭正在加载数据的异步线程等
     */
    public void destory() {
        handler.removeCallbacksAndMessages(null);
    }
    protected void onPreLoadMoreExecute() {
        onStateChangeListener.onStartLoadMore(dataAdapter);
        if (hasInitLoadMoreView && mLoadMoreView != null) {
            mLoadMoreView.showLoading();
        }
        this.requestPage=this.page+1;
        this.currentLoadState=LOAD_MORE;
    }

    protected void onPostLoadMoreExecute(final DATA result, final int maxPage) {
        if (result == null) {
            mLoadView.tipFail(null);
            if (hasInitLoadMoreView && mLoadMoreView != null) {
                mLoadMoreView.showFail(null);
            }
            onStateChangeListener.onEndLoadMore(dataAdapter, result);
        } else {
            //如果当前加载更多view已经可见了，就不能秒闪消失，体验不好
            if(contentView instanceof RecyclerView){
                int lastVisibleItemPosition=0;
                RecyclerView.LayoutManager layoutManager = ((RecyclerView) contentView).getLayoutManager();
                if(layoutManager instanceof LinearLayoutManager){
                    lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();

                }else if(layoutManager instanceof GridLayoutManager){
                    lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                }else if(layoutManager instanceof StaggeredGridLayoutManager){
                    int lastVisiblePositions[]=null;
                    lastVisiblePositions=((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(lastVisiblePositions);
                    for(int position:lastVisiblePositions){
                        if(position>lastVisibleItemPosition){
                            lastVisibleItemPosition=position;
                        }
                    }
                }
                int itemViewType = ((RecyclerView) contentView).getAdapter().getItemViewType(lastVisibleItemPosition);
                if(itemViewType== HFRecyclerAdapter.TYPE_FOOTER){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            endLoadMore(result,maxPage);
                        }
                    },500);
                }else{
//                    使用handler解决adapter刷新过程中调用刷新导致的：          java.lang.IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            endLoadMore(result,maxPage);
                        }
                    });
                }
            }else if(contentView instanceof AbsListView){
                int lastVisiblePosition = ((AbsListView) contentView).getLastVisiblePosition();
                if(lastVisiblePosition+1==((AbsListView) contentView).getAdapter().getCount()){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            endLoadMore(result,maxPage);
                        }
                    },500);
                }else{
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            endLoadMore(result,maxPage);
                        }
                    });
                }
            }
        }
    }

    private void endLoadMore(DATA result,int maxPage){
        dataAdapter.notifyDataChanged(result, false);
        if (dataAdapter.isEmpty()) {
            mLoadView.showEmpty();
        } else {
            mLoadView.restore();
        }
        this.page=this.requestPage;
        if (hasInitLoadMoreView && mLoadMoreView != null) {
            this.hasMoreData=page<maxPage;
            if (hasMoreData) {
                mLoadMoreView.showNormal();
            } else {
                mLoadMoreView.showNomore();
            }
        }
        onStateChangeListener.onEndLoadMore(dataAdapter, result);

        this.currentLoadState=NOMAL;
    }

    /**
     * 是否正在加载中
     *
     * @return
     */
    public boolean isLoading() {
        return currentLoadState!=NOMAL;
    }

    private OnRefreshListener onRefreshListener = new OnRefreshListener() {

        @Override
        public void onRefresh() {
            refresh();
        }
    };

    @SuppressWarnings("unchecked")
    public <T extends View> T getContentView() {
        return (T) refreshView.getContentView();
    }

    /**
     * 获取上次刷新数据的时间（数据成功的加载），如果数据没有加载成功过，那么返回-1
     *
     * @return
     */
    public long getLoadDataTime() {
        return loadDataTime;
    }

    public IDataAdapter<DATA> getAdapter() {
        return dataAdapter;
    }


    public ILoadView getLoadView() {
        return mLoadView;
    }

    public ILoadMoreView getLoadMoreView() {
        return mLoadMoreView;
    }

    public void setAutoLoadMore(boolean autoLoadMore) {
        this.autoLoadMore = autoLoadMore;
    }

    private boolean autoLoadMore = true;

    public boolean isAutoLoadMore() {
        return autoLoadMore;
    }

    private OnClickListener onClickLoadMoreListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            loadMore();
        }
    };

    private OnClickListener onClickRefresListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            refresh();
        }
    };

    protected IRefreshView getRefreshView() {
        return refreshView;
    }

    /**
     * 加载监听
     *
     * @param <DATA>
     * @author zsy
     */
    private static class MOnStateChangeListener<DATA> implements OnStateChangeListener<DATA> {
        private OnStateChangeListener<DATA> onStateChangeListener;
        private OnRefreshStateChangeListener<DATA> onRefreshStateChangeListener;
        private OnLoadMoreStateChangeListener<DATA> onLoadMoreStateChangeListener;

        public void setOnStateChangeListener(OnStateChangeListener<DATA> onStateChangeListener) {
            this.onStateChangeListener = onStateChangeListener;
        }

        public void setOnRefreshStateChangeListener(OnRefreshStateChangeListener<DATA> onRefreshStateChangeListener) {
            this.onRefreshStateChangeListener = onRefreshStateChangeListener;
        }

        public void setOnLoadMoreStateChangeListener(OnLoadMoreStateChangeListener<DATA> onLoadMoreStateChangeListener) {
            this.onLoadMoreStateChangeListener = onLoadMoreStateChangeListener;
        }

        @Override
        public void onStartRefresh(IDataAdapter<DATA> adapter) {
            if (onStateChangeListener != null) {
                onStateChangeListener.onStartRefresh(adapter);
            } else if (onRefreshStateChangeListener != null) {
                onRefreshStateChangeListener.onStartRefresh(adapter);
            }
        }

        @Override
        public void onEndRefresh(IDataAdapter<DATA> adapter, DATA result) {
            if (onStateChangeListener != null) {
                onStateChangeListener.onEndRefresh(adapter, result);
            } else if (onRefreshStateChangeListener != null) {
                onRefreshStateChangeListener.onEndRefresh(adapter, result);
            }
        }

        @Override
        public void onStartLoadMore(IDataAdapter<DATA> adapter) {
            if (onStateChangeListener != null) {
                onStateChangeListener.onStartLoadMore(adapter);
            } else if (onLoadMoreStateChangeListener != null) {
                onLoadMoreStateChangeListener.onStartLoadMore(adapter);
            }
        }

        @Override
        public void onEndLoadMore(IDataAdapter<DATA> adapter, DATA result) {
            if (onStateChangeListener != null) {
                onStateChangeListener.onEndLoadMore(adapter, result);
            } else if (onLoadMoreStateChangeListener != null) {
                onLoadMoreStateChangeListener.onEndLoadMore(adapter, result);
            }
        }

    }

    private OnScrollBottomListener onScrollBottomListener = new OnScrollBottomListener() {

        @Override
        public void onScorllBootom() {
            if (autoLoadMore && hasMoreData && !isLoading()) {
                // 如果网络可以用
                if (needCheckNetwork && !NetworkUtils.hasNetwork(context)) {
                    mLoadMoreView.showFail(new Exception("网络不可用"));
                } else {
                    loadMore();
                }
            }
        }
    };

    public static interface OnScrollBottomListener {
        public void onScorllBootom();
    }
}
