package com.liang530.views.wheelview.model;

/**
 * Created by hongliang on 16-6-4.
 */
public interface WheelData {
    //选中的item,显示的是某个item的某个字段

    /**
     * 获取唯一key
     * @return
     */
    String getWheelKey();

    /**
     * 获取显示的文字
     * @return
     */
    String getWheelText();

    /**
     * 获取值
     * @return
     */
    String getWheelValue();

}
