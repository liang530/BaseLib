package com.liang530.views.wheelview.type;

import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import core.base.R;
import com.liang530.views.wheelview.OnWheelChangedListener;
import com.liang530.views.wheelview.WheelView;
import com.liang530.views.wheelview.adapter.WheelViewTextAdapter;
import com.liang530.views.wheelview.dialog.WheelViewDialog;
import com.liang530.views.wheelview.dialog.WheelViewDialogSimpleListener;

/**
 * Created by hongliang on 16-6-4.
 *
 */
public class WheelTypeSimple extends WheelType {
    Map<String,List<String>>[] moreDataMaps;
    List<List<String>> datas=new ArrayList<List<String>>(5);
    private int wheelViewCount=1;
    private WheelViewDialogSimpleListener listener;
    private String flag;
    WheelView wheelViews[]=new WheelView[5];
    public WheelTypeSimple(WheelViewDialogSimpleListener listener, List<String> firstDatas, Map<String,List<String>>... moreDataMaps){
        this(listener,null,firstDatas,moreDataMaps);
    }
    public WheelTypeSimple(WheelViewDialogSimpleListener listener, String flag, List<String> firstDatas, Map<String,List<String>>... moreDataMaps) {
        if (firstDatas != null) {
            wheelViewCount = 1;
        }
        if (moreDataMaps != null) {
            wheelViewCount += moreDataMaps.length;
            this.moreDataMaps = moreDataMaps;
        }
        for (int i = 0; i < wheelViewCount; i++) {
            if (i == 0) {
                datas.add(firstDatas);
            } else {
                //上级的第一个数据
                String value = datas.get(i - 1).get(0);
                datas.add(moreDataMaps[i - 1].get(value));
            }
        }
        this.listener = listener;
        this.flag = flag;
    }
    @Override
    public WheelView[] initView(View customView, final WheelViewDialog dialog, WheelViewDialog.Builder builder) {
        View left = customView.findViewById(R.id.wheel_dialog_left);
        View right = customView.findViewById(R.id.wheel_dialog_right);
        wheelViews[0]= (WheelView) customView.findViewById(R.id.wheel_dialog_wheel1);
        wheelViews[1]= (WheelView) customView.findViewById(R.id.wheel_dialog_wheel2);
        wheelViews[2]= (WheelView) customView.findViewById(R.id.wheel_dialog_wheel3);
        wheelViews[3]= (WheelView) customView.findViewById(R.id.wheel_dialog_wheel4);
        wheelViews[4]= (WheelView) customView.findViewById(R.id.wheel_dialog_wheel5);
        for(int i=0;i<wheelViews.length;i++) {
            if (i < wheelViewCount) {
                wheelViews[i].setVisibility(View.VISIBLE);
            } else {
                wheelViews[i].setVisibility(View.GONE);
            }
        }
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(listener!=null){
                    int[] indexs=new int[wheelViewCount];
                    String[] values=new String[wheelViewCount];
                    for(int i=0;i<wheelViewCount;i++){
                        indexs[i]=wheelViews[i].getCurrentItem();
                        values[i]=datas.get(i).get(indexs[i]);
                    }
                    listener.onlickLeft(indexs,values,flag);
                }
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(listener!=null){
                    int[] indexs=new int[wheelViewCount];
                    String[] values=new String[wheelViewCount];
                    for(int i=0;i<wheelViewCount;i++){
                        indexs[i]=wheelViews[i].getCurrentItem();
                        values[i]=datas.get(i).get(indexs[i]);
                    }
                    listener.onlickRight(indexs,values,flag);
                }
            }
        });
        initData();
        return wheelViews;
    }

    @Override
    public void setCurrentItem(int level, int index, boolean animated) {
        wheelViews[level].setCurrentItem(index,animated);
    }

    @Override
    public void setCurrentItem(int level, String key, boolean animated) {
        if(key==null) return;
        List<String> values = datas.get(level);
        for(int i=0;i<values.size();i++){
            if(key.equals(values.get(i))){
                wheelViews[level].setCurrentItem(i,animated);
            }
        }
    }

    @Override
    public Object getCurrentItem(int level) {
        return datas.get(level).get(wheelViews[level].getCurrentItem());
    }

    public void initData(){
        for(int i=0;i<wheelViewCount;i++){
            wheelViews[i].setViewAdapter(new WheelViewTextAdapter(wheelViews[i].getContext(),datas.get(i)));
            if(i!=wheelViewCount-1){//最后一级不需要监听
                wheelViews[i].addChangingListener(new MoreWheelChangedListener(wheelViews,i));
            }
        }
    }
    public class MoreWheelChangedListener implements OnWheelChangedListener{
        private WheelView[] wheelViews;
        private int level;
        public MoreWheelChangedListener(WheelView[] wheelViews,int level){
            this.level=level;
            this.wheelViews=wheelViews;
        }
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            String value = datas.get(level).get(newValue);
            //为下一级设置适配器和数据
            int index=level+1;
            List<String> values = moreDataMaps[index-1].get(value);
            datas.set(index,values);//更新数据
            wheelViews[index].setViewAdapter(new WheelViewTextAdapter(wheelViews[index].getContext(),values));
        }
    }
}
