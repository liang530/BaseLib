package com.liang530.views.wheelview.model;

/**
 * Created by hongliang on 16-6-6.
 */
public class TimeWheelData implements WheelData {
    private String wheelText;
    private String wheelValue;
    private String wheelKey;

    @Override
    public String getWheelKey() {
        return wheelKey;
    }

    @Override
    public String getWheelText() {
        return wheelText;
    }

    @Override
    public String getWheelValue() {
        return wheelValue;
    }

    public void setWheelText(String wheelText) {
        this.wheelText = wheelText;
    }

    public void setWheelValue(String wheelValue) {
        this.wheelValue = wheelValue;
    }

    public void setWheelKey(String wheelKey) {
        this.wheelKey = wheelKey;
    }
}
