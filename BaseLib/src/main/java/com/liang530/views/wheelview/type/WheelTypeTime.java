package com.liang530.views.wheelview.type;

import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import core.base.R;
import com.liang530.views.wheelview.OnWheelChangedListener;
import com.liang530.views.wheelview.WheelView;
import com.liang530.views.wheelview.adapter.WheelViewTextAdapter;
import com.liang530.views.wheelview.dialog.WheelViewDialog;
import com.liang530.views.wheelview.dialog.WheelViewDialogListener;
import com.liang530.views.wheelview.model.TimeWheelData;
import com.liang530.views.wheelview.model.WheelData;

/**
 * Created by hongliang on 16-6-6.
 */
public class WheelTypeTime extends WheelType {
    public final static int TYPE_YMD=0;
    public final static int TYPE_YMDHM=1;
    public final static int TYPE_HM=2;
    private final static int wheelViewCount=5;
    private String flag;
    WheelView wheelViews[]=new WheelView[5];

    List<List<? extends WheelData>> datas=new ArrayList<List<? extends WheelData>>(5);

    //开始时间
    private Calendar startTime=Calendar.getInstance();

    // 是否允许选择此时间之前的时间
    private boolean isShowBeforeTime=true;

    //结束时间
    private Calendar endTime=Calendar.getInstance();

    //模式  年月日,年月日时分,时分
    private int type=TYPE_YMD;


    //显示后缀
    private String suffix[]={"年","月","日","时","分"};
    private WheelViewDialogListener listener;
    public WheelTypeTime( String flag, WheelViewDialogListener listener){
        this.flag=flag;
        this.listener=listener;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public boolean isShowBeforeTime() {
        return isShowBeforeTime;
    }

    public void setShowBeforeTime(boolean showBeforeTime) {
        isShowBeforeTime = showBeforeTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String[] getSuffix() {
        return suffix;
    }

    public void setSuffix(String[] suffix) {
        this.suffix = suffix;
    }
    public void setSuffix(String yearSuffix,String monthSuffix,String daySuffix,String hourSuffix,String minuteSuffix){
        suffix[0]=yearSuffix;
        suffix[1]=monthSuffix;
        suffix[2]=daySuffix;
        suffix[3]=hourSuffix;
        suffix[4]=minuteSuffix;
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

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(listener!=null){
                    int[] indexs=new int[5];
                    WheelData[] wheelDatas=new WheelData[5];
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
        initData(type);
        return new WheelView[0];
    }
    private void initYearData(){
        //生成年
        int startYear = startTime.get(Calendar.YEAR);
        int endYear = endTime.get(Calendar.YEAR);
        List<TimeWheelData> yearWheelDatas=new ArrayList<>();
        for(int i=startYear;i<=endYear;i++){
            TimeWheelData timeWheelData=new TimeWheelData();
            timeWheelData.setWheelValue(i+"");
            timeWheelData.setWheelKey(""+i);
            timeWheelData.setWheelText(i+suffix[0]);
            yearWheelDatas.add(timeWheelData);
        }
        datas.add(0,yearWheelDatas);
        //初始化其他为空数据
        datas.add(1,new ArrayList<WheelData>());
        datas.add(2,new ArrayList<WheelData>());
        datas.add(3,new ArrayList<WheelData>());
        datas.add(4,new ArrayList<WheelData>());
    }
    private void initData(int type){
        //初始化数据
        switch (type){
            case TYPE_YMDHM:
                wheelViews[0].setVisibility(View.VISIBLE);
                wheelViews[1].setVisibility(View.VISIBLE);
                wheelViews[2].setVisibility(View.VISIBLE);
                wheelViews[3].setVisibility(View.VISIBLE);
                wheelViews[4].setVisibility(View.VISIBLE);
                break;
            case TYPE_YMD:
                wheelViews[0].setVisibility(View.VISIBLE);
                wheelViews[1].setVisibility(View.VISIBLE);
                wheelViews[2].setVisibility(View.VISIBLE);
                wheelViews[3].setVisibility(View.GONE);
                wheelViews[4].setVisibility(View.GONE);
                break;
            case TYPE_HM:
                wheelViews[0].setVisibility(View.GONE);
                wheelViews[1].setVisibility(View.GONE);
                wheelViews[2].setVisibility(View.GONE);
                wheelViews[3].setVisibility(View.VISIBLE);
                wheelViews[4].setVisibility(View.VISIBLE);
                break;
        }
        initYearData();
        for(int i=0;i<wheelViewCount;i++){
            wheelViews[i].setViewAdapter(new WheelViewTextAdapter(wheelViews[i].getContext(),datas.get(i)));
            if(i!=wheelViewCount-1){//最后一级不需要监听
                wheelViews[i].addChangingListener(new TimeWheelChangedListener(wheelViews,i));
            }
        }
        if(wheelViews[0].getCurrentItem()!=0){
            wheelViews[0].setCurrentItem(0);
        }else{
            forceUpdate(0,0,0);
        }

    }
    public class TimeWheelChangedListener implements OnWheelChangedListener {
        private WheelView[] wheelViews;
        private int level;
        public TimeWheelChangedListener(WheelView[] wheelViews, int level){
            this.level=level;
            this.wheelViews=wheelViews;
        }
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            forceUpdate(level,oldValue,newValue);
        }
    }

    /**
     * 强制刷新所有的wheelView
     * @param level
     * @param oldValue
     * @param newValue
     */
    public void forceUpdate(int level, int oldValue, int newValue){
        //改变后面级别的数据
        for(int index=level+1;index<wheelViewCount;index++){
            List<TimeWheelData> wheelDatas=null;
            switch (index){
                case 1:
                    wheelDatas=createMonth();
                    break;
                case 2:
                    wheelDatas=createDay();
                    break;
                case 3:
                    wheelDatas=createHour();
                    break;
                case 4:
                    wheelDatas=createMinute();
                    break;
            }
            if(wheelDatas==null){
                wheelDatas=new ArrayList();
            }

            datas.set(index,wheelDatas);//更新数据
            wheelViews[index].setViewAdapter(new WheelViewTextAdapter(wheelViews[index].getContext(),wheelDatas));
            // 防止数组越界
            if (wheelViews[index].getCurrentItem() >= wheelDatas.size()) {
                wheelViews[index].setCurrentItem(wheelDatas.size() - 1);
            }
        }
    }

    private List<TimeWheelData> createHour() {
        List<TimeWheelData> hourdatas=new ArrayList<>();;
        if(type==TYPE_YMD){

            TimeWheelData timeWheelData=new TimeWheelData();
            timeWheelData.setWheelValue("00");
            timeWheelData.setWheelKey("00");
            timeWheelData.setWheelText("00时");
            hourdatas.add(timeWheelData);
            return hourdatas;
        }
        for(int i=1;i<=24;i++){
            TimeWheelData timeWheelData=new TimeWheelData();
            StringBuffer preBuffer=new StringBuffer();
            if(i<10) {
                preBuffer.append("0");
            }
            preBuffer.append(i);
            timeWheelData.setWheelValue(preBuffer.toString());
            timeWheelData.setWheelKey(preBuffer.toString());
            timeWheelData.setWheelText(preBuffer.append(suffix[3]).toString());
            hourdatas.add(timeWheelData);
        }
        return hourdatas;
    }
//    private List<TimeWheelData> getData(int level) {
//        StringBuffer buffer=new StringBuffer(datas.get(0).get(wheelViews[0].getCurrentItem()).getWheelKey());
//
//        for(int i=1;i<wheelViewCount;i++){
//            if(i<level){
//                buffer.append("-").append(datas.get(i).get(wheelViews[i].getCurrentItem()).getWheelKey());
//            }
//        }
//        for(int)
//
//        return null;
//    }
    private List<TimeWheelData> createDay() {
        int currentItem = wheelViews[0].getCurrentItem();
        WheelData wheelData = datas.get(0).get(currentItem);
        String wheelKey0 = wheelData.getWheelKey();
        int startYear = startTime.get(Calendar.YEAR);
        int endYear = endTime.get(Calendar.YEAR);
        String wheelKey1 = datas.get(1).get(wheelViews[1].getCurrentItem()).getWheelKey();
        int startMonth = startTime.get(Calendar.MONTH)+1;
        int endMonth = endTime.get(Calendar.MONTH)+1;
        int startday=1;
        int endday=getDay(Integer.parseInt(wheelKey0), Integer.parseInt(wheelKey1));

//        if(wheelKey0.equals(startYear+"")&&wheelKey1.equals(st)){//是起始年这一年,需要判断月份限制
//            startMonth= startTime.get(Calendar.MONTH)+1;
//        }
//        if(wheelKey0.equals(endYear+"")){//是结束年这一年,需要判断月份限制
//            endMonth= endTime.get(Calendar.MONTH)+1;
//        }
        List<TimeWheelData> dayWheelDatas=new ArrayList<>();
        //根据年生成月
        for(int i=startday;i<=endday;i++){
            TimeWheelData timeWheelData=new TimeWheelData();
            StringBuffer preBuffer=new StringBuffer();
            if(i<10) {
                preBuffer.append("0");
            }
            preBuffer.append(i);
            timeWheelData.setWheelValue(preBuffer.toString());
            timeWheelData.setWheelKey(preBuffer.toString());
            timeWheelData.setWheelText(preBuffer.append(suffix[2]).toString());
            dayWheelDatas.add(timeWheelData);
        }
        return dayWheelDatas;

    }

    private List<TimeWheelData> createMonth() {
        int currentItem = wheelViews[0].getCurrentItem();
        WheelData wheelData = datas.get(0).get(currentItem);
        String wheelKey = wheelData.getWheelKey();
        int startYear = startTime.get(Calendar.YEAR);
        int endYear = endTime.get(Calendar.YEAR);
        int startMonth=1;
        int endMonth=12;
        //计算开始时间,结束时间
//        if(wheelKey.equals(endYear+"")){//是结束年这一年,需要判断月份限制
//            endMonth= endTime.get(Calendar.MONTH)+1;
//        }
        List<TimeWheelData> monthWheelDatas=new ArrayList<>();
        //根据年生成月
        for(int i=startMonth;i<=endMonth;i++){
            TimeWheelData timeWheelData=new TimeWheelData();
            StringBuffer preBuffer=new StringBuffer();
            if(i<10) {
                preBuffer.append("0");
            }
            preBuffer.append(i);
            timeWheelData.setWheelValue(preBuffer.toString());
            timeWheelData.setWheelKey(preBuffer.toString());
            timeWheelData.setWheelText(preBuffer.append(suffix[1]).toString());
            monthWheelDatas.add(timeWheelData);
        }
        return monthWheelDatas;
    }
    private List<TimeWheelData> createMinute() {
        List<TimeWheelData> minutedatas=new ArrayList<>();
        if(type==TYPE_YMD){
            TimeWheelData timeWheelData=new TimeWheelData();
            timeWheelData.setWheelValue("00");
            timeWheelData.setWheelKey("00");
            timeWheelData.setWheelText("00分");
            minutedatas.add(timeWheelData);
            return minutedatas;
        }
        for(int i=1;i<=60;i++){
            TimeWheelData timeWheelData=new TimeWheelData();
            StringBuffer preBuffer=new StringBuffer();
            if(i<10) {
                preBuffer.append("0");
            }
            preBuffer.append(i);
            timeWheelData.setWheelValue(preBuffer.toString());
            timeWheelData.setWheelKey(preBuffer.toString());
            timeWheelData.setWheelText(preBuffer.append(suffix[4]).toString());
            minutedatas.add(timeWheelData);
        }
        return minutedatas;
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
        return null;
    }
    // 根据当前年份和月份判断这个月的天数
    private static int getDay(int year, int month) {
        int day;
        if (year % 4 == 0 && year % 100 != 0) { // 闰年
            if (month == 1 || month == 3 || month == 5 || month == 7
                    || month == 8 || month == 10 || month == 12) {
                day = 31;
            } else if (month == 2) {
                day = 29;
            } else {
                day = 30;
            }
        } else { // 平年
            if (month == 1 || month == 3 || month == 5 || month == 7
                    || month == 8 || month == 10 || month == 12) {
                day = 31;
            } else if (month == 2) {
                day = 28;
            } else {
                day = 30;
            }
        }
        return day;
    }
}
