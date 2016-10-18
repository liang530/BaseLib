package com.liang530.views.wheelview.adapter;

import android.content.Context;

import java.util.List;

import com.liang530.views.wheelview.model.EmptyWheelData;
import com.liang530.views.wheelview.model.WheelData;

/**
 * Created by hongliang on 16-6-4.
 */
public class WheelViewTextAdapter extends AbstractWheelTextAdapter {
    private List datas;
    public WheelViewTextAdapter(Context context, List datas) {
        super(context);
        this.datas=datas;
    }

    public WheelViewTextAdapter(Context context, int itemResource, List<Object> datas) {
        super(context, itemResource);
        this.datas=datas;
    }

    public WheelViewTextAdapter(Context context, int itemResource, int itemTextResource, List<Object> datas) {
        super(context, itemResource, itemTextResource);
        this.datas=datas;
    }

    @Override
    public CharSequence getItemText(int index) {
        Object obj = datas.get(index);
        if(obj instanceof WheelData){
            return ((WheelData)obj).getWheelText();
        }else{
            return obj.toString();
        }
    }

    @Override
    public int getItemsCount() {
        if(datas.size()==0){
            datas.add(0,new EmptyWheelData());
        }
        return datas.size();
    }
}
