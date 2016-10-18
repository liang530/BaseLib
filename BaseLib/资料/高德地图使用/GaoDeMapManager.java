package com.kufeng.hj.buyfood.util;

/**
 * Created by 刘红亮 on 2015/11/3 17:18.
 */

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.kufeng.hj.buyfood.event.LocationEvent;

import org.greenrobot.eventbus.EventBus;

import com.liang530.log.L;


/**
 * 高德地图定位，poi检索管理
 *
 * @author liuhongliang
 *         <p/>
 *         2015-2-5 下午1:38:25
 */
public class GaoDeMapManager implements AMapLocationListener {
    //定位得到的经纬度信息
    private AMapLocation aMapLocation;
    private Context context;
    private static GaoDeMapManager gaoDeMapManager;
    // 定位相关
    private AMapLocationClient mLocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;


    /**
     * @param context 上下文对象
     */
    private GaoDeMapManager(Context context) {
        this.context = context;
    }

    /**
     * 单例模式
     *
     * @param context
     * @return
     */
    public static synchronized GaoDeMapManager getInstance(Context context) {
        if (gaoDeMapManager == null) {
            gaoDeMapManager = new GaoDeMapManager(context);
        }
        return gaoDeMapManager;
    }

    /**
     * 开启定位,定位结果可在SuccessLocationListener的回调方法中获得
     *
     * @param updateTime  更新时间,单位ms,-1代表只定位一次，不更新
     * @param minDistance 更新的最小距离，即移动的距离 单位为米
     */
    public void startLocation(long updateTime, float minDistance) {
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(context.getApplicationContext());
        }
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(updateTime);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();//停止定位
            mLocationClient.onDestroy();//销毁定位客户端
        }
        mLocationClient = null;
    }

    /**
     * 定位的监听
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null
                && amapLocation.getErrorCode() == 0) {
            // 获取位置信息
            this.aMapLocation = amapLocation;
            L.e("定位成功   la:" + aMapLocation.getLatitude() + "   lo:" + aMapLocation.getLongitude());
            LocationEvent locationEvent = new LocationEvent(amapLocation);
            locationEvent.latitude = amapLocation.getLatitude();
            locationEvent.lontitude = amapLocation.getLongitude();
            locationEvent.addrStr = amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息
            locationEvent.city = amapLocation.getCity();
            locationEvent.district = amapLocation.getDistrict();
            locationEvent.province = amapLocation.getProvince();
            locationEvent.street = amapLocation.getStreet();
            locationEvent.streetNum=amapLocation.getStreetNum();
            locationEvent.bearing = amapLocation.getBearing();
            EventBus.getDefault().postSticky(locationEvent);
        }else{
            L.e("定位失败：" + aMapLocation.getErrorInfo());
        }
    }
    public AMapLocation getAmapLocation() {
        return aMapLocation;
    }
    public void setAmapLocation(AMapLocation aMapLocation) {
        this.aMapLocation = aMapLocation;
    }

}
