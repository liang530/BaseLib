package com.liang530.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;

import com.liang530.log.L;
import com.liang530.rxvolley.NetRequest;

public class BaseFragment extends Fragment {
	protected LayoutInflater inflater;
	private View contentView;
	protected Context mContext;
	private ViewGroup container;
	protected String TAG = getClass().getName();
	protected void le(String msg) {
		L.e(TAG, msg);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	@Override
	public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
		this.container = container;
		onCreateView(savedInstanceState);
		if (contentView == null)
			return super.onCreateView(inflater, container, savedInstanceState);
		return contentView;
	}

	protected void onCreateView(Bundle savedInstanceState) {

	}

	@Override
	public void onDestroyView() {
		NetRequest.getRequestQueue().cancelAll(this);//activity销毁时取消请求
		super.onDestroyView();
		contentView = null;
		container = null;
		inflater = null;
	}

	public Context getApplicationContext() {
		return mContext.getApplicationContext();
	}

	public void setContentView(int layoutResID) {
		setContentView((ViewGroup) inflater.inflate(layoutResID, container, false));

	}

	public void setContentView(View view) {
		contentView = view;

	}

	public View getContentView() {
		return contentView;
	}

	public View findViewById(int id) {
		if (contentView != null)
			return contentView.findViewById(id);
		return null;
	}

	// http://stackoverflow.com/questions/15207305/getting-the-error-java-lang-illegalstateexception-activity-has-been-destroyed
	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
