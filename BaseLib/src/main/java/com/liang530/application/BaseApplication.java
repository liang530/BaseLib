package com.liang530.application;

import android.app.Application;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.http.RequestQueue;

import com.liang530.exception.BaseCrashHandler;
import com.liang530.log.SP;
import com.liang530.rxvolley.NetRequest;
import com.liang530.rxvolley.OkHttpStack;
import com.liang530.system.SystemBarTintManager;
import com.liang530.utils.BasePrefsUtil;
import okhttp3.OkHttpClient;

/**
 * 基础的application类
 */
public abstract class BaseApplication extends Application {
    SystemBarTintManager.SystemBarTintConfig config;
    private static BaseApplication instance;


    public static BaseApplication getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;


       // initCrashHandler(); // 初始化程序崩溃捕捉处理
        initPrefs(); // 初始化SharedPreference
        initOkHttp();
        //友盟统计日志加密
//        MobclickAgent.enableEncrypt(false);
    }

    private void initOkHttp() {
        NetRequest.setRequestQueue(RequestQueue.newRequestQueue(RxVolley.CACHE_FOLDER,
                new OkHttpStack(new OkHttpClient())));
    }

    public void saveMd5Pwd(String md5Pwd){
        BasePrefsUtil.getPrefsUtil("encrypt_prefs").putString("md5Pwd",md5Pwd).commit();
    }
    public String getMd5Pwd(){
        return BasePrefsUtil.getPrefsUtil("encrypt_prefs").getString("md5Pwd","");
    }


    /**
     * 初始化程序崩溃捕捉处理
     */
    protected void initCrashHandler() {
        BaseCrashHandler.init(getApplicationContext());
    }

    /**
     * 初始化SharedPreference
     */
    protected void initPrefs() {
        SP.init(getApplicationContext());
        BasePrefsUtil.init(this, "encrypt_prefs", MODE_PRIVATE);
    }

    public SystemBarTintManager.SystemBarTintConfig getConfig() {
        return config;
    }

    /**
     * @param config 状态栏,底部栏配置
     */
    public void setStatusColorConfig(SystemBarTintManager.SystemBarTintConfig config) {
        this.config=config;
    }
    public abstract Class getLoginClass();
}
