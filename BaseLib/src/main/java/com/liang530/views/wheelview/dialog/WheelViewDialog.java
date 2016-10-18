package com.liang530.views.wheelview.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Interpolator;

import com.liang530.views.wheelview.WheelView;
import com.liang530.views.wheelview.type.WheelType;

/**
 * Created by hongliang on 16-6-4.
 *  设置wheelview组的可显示数量
 *  设置wheelView
 */
public class WheelViewDialog extends Dialog{
    private WheelType model;
    public void setCurrentItem(int level,int index, boolean animated){
        model.setCurrentItem(level,index,animated);
    }
    public void setCurrentItem(int level,String key, boolean animated){
        model.setCurrentItem(level,key,animated);
    }
    public Object getCurrentItem(int level){
        return model.getCurrentItem(level);
    }
    private WheelViewDialog(Context context){
       super(context);
    }
    private WheelViewDialog(Context context,int themeResId){
        super(context,themeResId);
    }
    private void setWheelDialogModel(WheelType model){
        this.model=model;
    }
    public static class Builder{
        private Context context;
        private Integer gravity=Gravity.BOTTOM;
//        private List<WheelData> firstDatas;
//        private Map<String,List<WheelData>>[] moreDataMaps;
        private Integer visibleItemCount=5;
        private Boolean cyclic=false;
        private Interpolator interpolator;
        private Integer wheelLineColor;
        private Boolean drawShadows;
        private Integer start,  middle,  end;
        private Integer wheelBackgroundRecId;
//        private Integer wheelForegroundRecId;

        public Builder setGravity(Integer gravity) {
            this.gravity = gravity;
            return this;
        }

        public Integer getGravity() {
            return gravity;
        }

        public Integer getVisibleItemCount() {
            return visibleItemCount;
        }

        public Boolean getCyclic() {
            return cyclic;
        }

        public Interpolator getInterpolator() {
            return interpolator;
        }

        public Integer getWheelLineColor() {
            return wheelLineColor;
        }

        public Boolean getDrawShadows() {
            return drawShadows;
        }

        public Integer getStart() {
            return start;
        }

        public Integer getMiddle() {
            return middle;
        }

        public Integer getEnd() {
            return end;
        }

        public Integer getWheelBackgroundRecId() {
            return wheelBackgroundRecId;
        }

//        public Integer getWheelForegroundRecId() {
//            return wheelForegroundRecId;
//        }

        public Builder() {
        }

//        /**
//         * 设置数据集
//         * @param firstDatas 第一级
//         * @param moreDataMaps 更多级别，有多少级别传多少级别
//         */
//        public Builder setData(List<WheelData> firstDatas, Map<String,List<WheelData>> ... moreDataMaps ){
//            this.firstDatas=firstDatas;
//            this.moreDataMaps=moreDataMaps;
//            return this;
//        }
        /**
         * 设置可见的item数
         * @param visibleItemCount
         */
        public Builder setVisibleItemCount(int visibleItemCount){
            this.visibleItemCount=visibleItemCount;
            return this;
        }
        public Builder setCyclic(boolean cyclic){
            this.cyclic=cyclic;
            return this;
        }
        /**
         * Set the the specified scrolling interpolator
         * @param interpolator the interpolator
         */
        public Builder setInterpolator(Interpolator interpolator) {
            this.interpolator=interpolator;
            return this;
        }
        public Builder setWheelLineColor(int wheelLineColor) {
            this.wheelLineColor = wheelLineColor;
            return this;
        }
        /**
         * Set whether shadows should be drawn
         * @param drawShadows flag as true or false
         */
        public Builder setDrawShadows(boolean drawShadows) {
            this.drawShadows = drawShadows;
            return this;
        }

        /**
         * Set the shadow gradient color
         * @param start
         * @param middle
         * @param end
         */
        public Builder setShadowColor(int start, int middle, int end) {
            this.start=start;
            this.end=end;
            this.middle=middle;
            return this;
        }

        /**
         * Sets the drawable for the wheel background
         * @param wheelBackgroundRecId
         */
        public Builder setWheelBackgroundId(int wheelBackgroundRecId) {
            this.wheelBackgroundRecId=wheelBackgroundRecId;
            return this;
        }

//        /**
//         * Sets the drawable for the wheel foreground
//         * @param wheelForegroundRecId
//         *
//         */
//        public Builder setWheelForeground(int wheelForegroundRecId) {
//           this.wheelForegroundRecId=wheelForegroundRecId;
//            return this;
//        }
        public WheelViewDialog show(Context context,WheelType wheelDialogModel){
            final WheelViewDialog dialog = new WheelViewDialog(context, wheelDialogModel.getStyleResId());
            dialog.setWheelDialogModel(wheelDialogModel);
            View customView = wheelDialogModel.getCustomView(context);
            dialog.setContentView(customView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            Window window = dialog.getWindow();
            WindowManager.LayoutParams wl = window.getAttributes();
            wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
            wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            wl.gravity = gravity;
            WheelView[] wheelViews = wheelDialogModel.initView(customView, dialog, this);
            apply(wheelViews);
            dialog.show();
            return dialog;
        }

        private void apply(WheelView[] wheelViews) {
            for(int i=0;i<wheelViews.length;i++){
                if(visibleItemCount!=null){
                    wheelViews[i].setVisibleItems(visibleItemCount);
                }
                if(cyclic!=null){
                    wheelViews[i].setCyclic(cyclic);
                }
                if(interpolator!=null){
                    wheelViews[i].setInterpolator(interpolator);
                }
                if(wheelLineColor!=null){
                    wheelViews[i].setWheelLineColor(wheelLineColor);
                }
                if(drawShadows!=null){
                    wheelViews[i].setDrawShadows(drawShadows);
                }
                if(start!=null&&middle!=null&&end!=null){
                    wheelViews[i].setShadowColor(start,middle,end);
                }
                if(wheelBackgroundRecId!=null){
                    wheelViews[i].setWheelBackground(wheelBackgroundRecId);
                }

            }
        }
    }
}
