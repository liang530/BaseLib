package com.liang530.views.refresh.mvc.viewhandler;

import android.view.View;
import android.view.View.OnClickListener;

import com.liang530.views.refresh.mvc.BaseRefreshLayout.OnScrollBottomListener;
import com.liang530.views.refresh.mvc.IDataAdapter;
import com.liang530.views.refresh.mvc.ILoadViewFactory.ILoadMoreView;

public interface ViewHandler {

	/**
	 *
	 * @param contentView
	 * @param adapter
	 * @param loadMoreView
	 * @param onClickLoadMoreListener
     * @return 是否有 init ILoadMoreView
     */
	public boolean handleSetAdapter(View contentView, IDataAdapter<?> adapter, ILoadMoreView loadMoreView, OnClickListener onClickLoadMoreListener);


	public void setOnScrollBottomListener(View contentView, Integer autoLastLoadMorePosition, OnScrollBottomListener onScrollBottomListener);
}
