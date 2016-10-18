package com.liang530.views.wheelview.type;

import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import core.base.R;
import com.liang530.application.BaseApplication;
import com.liang530.utils.address.ProvinceUtil;
import com.liang530.utils.address.provice.CityModel;
import com.liang530.utils.address.provice.DistrictModel;
import com.liang530.utils.address.provice.ProvinceModel;
import com.liang530.views.wheelview.OnWheelChangedListener;
import com.liang530.views.wheelview.WheelView;
import com.liang530.views.wheelview.adapter.WheelViewTextAdapter;
import com.liang530.views.wheelview.dialog.WheelViewDialog;
import com.liang530.views.wheelview.dialog.WheelViewDialogListener;
import com.liang530.views.wheelview.model.WheelData;

/**
 * Created by hongliang on 2016/6/5.
 */
public class WheelAddress3Type extends WheelType {
    //特别注意存的是第二级别级以上的，注意下标-1
    Map<String,? extends List<? extends WheelData>>[] moreDataMaps;
    List<List<? extends WheelData>> datas=new ArrayList<List<? extends WheelData>>(5);
    private int wheelViewCount=1;
    private WheelViewDialogListener listener;
    private String flag;
    WheelView wheelViews[]=new WheelView[5];
    public WheelAddress3Type(WheelViewDialogListener listener){
        ProvinceUtil provinceUtil=new ProvinceUtil();
        provinceUtil.initProvinceDatas(BaseApplication.getInstance().getAssets());
        Map<String, List<CityModel>> cityListMap = provinceUtil.getmCitisDatasMap();
        Map<String, List<DistrictModel>> districtListMap = provinceUtil.getmDistrictDatasMap();
        List<ProvinceModel> provinceModels = provinceUtil.getmProvinceDatas();
        this.listener = listener;
        this.flag = flag;
        init(provinceModels,cityListMap,districtListMap);
    }
    public void init(List<? extends WheelData> firstDatas,Map<String,? extends List<? extends WheelData>>... moreDataMaps){
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
                WheelData wheelData = datas.get(i - 1).get(0);
                datas.add(moreDataMaps[i - 1].get(wheelData.getWheelKey()));
            }
        }
    }
    public WheelAddress3Type(WheelViewDialogListener listener, String flag,List<? extends WheelData> firstDatas,Map<String,List<? extends WheelData>>... moreDataMaps) {
        this.listener = listener;
        this.flag = flag;
        init(firstDatas,moreDataMaps);
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
                    WheelData[] wheelDatas=new WheelData[wheelViewCount];
                    for(int i=0;i<wheelViewCount;i++){
                        indexs[i]=wheelViews[i].getCurrentItem();
                        wheelDatas[i]=datas.get(i).get(indexs[i]);
                    }
                    listener.onlickRight(indexs,wheelDatas,flag);
                }
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(listener!=null){
                    int[] indexs=new int[wheelViewCount];
                    WheelData[] wheelDatas=new WheelData[wheelViewCount];
                    for(int i=0;i<wheelViewCount;i++){
                        indexs[i]=wheelViews[i].getCurrentItem();
                        wheelDatas[i]=datas.get(i).get(indexs[i]);
                    }
                    listener.onlickRight(indexs,wheelDatas,flag);
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
        List<? extends WheelData> wheelDatas = datas.get(level);
        for(int i=0;i<wheelDatas.size();i++){
            if(key.equals(wheelDatas.get(i).getWheelKey())){
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
    public class MoreWheelChangedListener implements OnWheelChangedListener {
        private WheelView[] wheelViews;
        private int level;
        public MoreWheelChangedListener(WheelView[] wheelViews,int level){
            this.level=level;
            this.wheelViews=wheelViews;
        }
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            WheelData wheelData = datas.get(level).get(newValue);//获得改变的当前级别的数据
            //改变后面级别的数据
            for(int index=level+1;index<wheelViewCount;index++){
                WheelData beforeWheelData = datas.get(index - 1).get(newValue);//上一级选中的数据
                List<?extends WheelData> wheelDatas=null;
                if(beforeWheelData!=null){//上一级已经没有数据了，下一级还是要清空数据
                    wheelDatas =  moreDataMaps[index-1].get(beforeWheelData.getWheelKey());
                }
                if(wheelDatas==null){
                    wheelDatas=new ArrayList();
                }
                datas.set(index,wheelDatas);//更新数据
                wheelViews[index].setViewAdapter(new WheelViewTextAdapter(wheelViews[index].getContext(),wheelDatas));
                wheelViews[index].setCurrentItem(0);
                newValue=0;//第一次过后都是默认选中位置0
            }
        }
    }
}
