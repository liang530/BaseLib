package com.liang530.views.wheelview.type;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import core.base.R;
import com.liang530.views.wheelview.WheelView;
import com.liang530.views.wheelview.dialog.WheelViewDialog;


/**
 * Created by hongliang on 16-6-4.
 */
public abstract class WheelType {
    private int defaultViewId= R.layout.dialog_show_wheel;
    private int defaultStyleResId= R.style.photo_dialog_bg_style;
    /**
     * 初始化View
     * @param customView
     * @param builder
     * @return 返回显示的WheelView 数量
     */
    public abstract WheelView[] initView(View customView, WheelViewDialog dialog, WheelViewDialog.Builder builder);


    /**
     * 默认的获取dialog显示的内容view
     * @param context
     * @return
     */
    public View getCustomView(Context context){
        return LayoutInflater.from(context).inflate(defaultViewId, null);
    }
    /**
     * 默认的获取dialog显示的内容view
     * @return
     */
    public int getStyleResId(){
        return defaultStyleResId;
    }

    public abstract void setCurrentItem(int level, int index, boolean animated);
    public abstract void setCurrentItem(int level, String key, boolean animated);

    public abstract Object getCurrentItem(int level);
}
