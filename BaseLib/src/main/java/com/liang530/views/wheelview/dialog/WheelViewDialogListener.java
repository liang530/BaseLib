package com.liang530.views.wheelview.dialog;

import com.liang530.views.wheelview.model.WheelData;

/**
 * Created by hongliang on 2016/6/5.
 */
public interface WheelViewDialogListener {

    void onlickLeft(int[] indexs, WheelData[] wheelDatas, String flag);
    void onlickRight(int[] indexs, WheelData[] wheelDatas, String flag);
}
