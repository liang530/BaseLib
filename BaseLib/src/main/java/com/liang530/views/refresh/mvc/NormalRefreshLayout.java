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

import android.view.View;



/**
 * 注意 ：<br>
 * contentView 必须有Parent
 * 
 * @author LuckyJayce
 *
 * @param <DATA>
 */
public class NormalRefreshLayout<DATA> extends BaseRefreshLayout<DATA> {

	public NormalRefreshLayout(View contentView) {
		super(new RefreshView(contentView));
	}

	public NormalRefreshLayout(View contentView, ILoadViewFactory.ILoadView loadView) {
		super(new RefreshView(contentView), loadView);
	}

	public NormalRefreshLayout(View contentView, ILoadViewFactory.ILoadView loadView, ILoadViewFactory.ILoadMoreView loadMoreView) {
		super(new RefreshView(contentView), loadView, loadMoreView);
	}

	private static class RefreshView implements IRefreshView {
		private View contentView;

		public RefreshView(View contentView) {
			super();
			this.contentView = contentView;
		}

		@Override
		public View getContentView() {
			return contentView;
		}

		@Override
		public void setOnRefreshListener(OnRefreshListener onRefreshListener) {

		}

		@Override
		public void showRefreshComplete() {

		}

		@Override
		public void showRefreshing() {

		}

		@Override
		public View getSwitchView() {
			return contentView;
		}

	}

}
