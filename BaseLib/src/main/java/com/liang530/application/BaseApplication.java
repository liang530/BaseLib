package com.liang530.application;

import android.app.Application;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.http.RequestQueue;
import com.kymjs.rxvolley.toolbox.HTTPSTrustManager;
import com.liang530.exception.BaseCrashHandler;
import com.liang530.log.SP;
import com.liang530.rxvolley.NetRequest;
import com.liang530.rxvolley.OkHttpIgnoreHttpsStack;
import com.liang530.system.SystemBarTintManager;
import com.liang530.utils.BasePrefsUtil;

import okhttp3.OkHttpClient;

/**
 * 基础的application类
 */
public abstract class BaseApplication extends Application {
    SystemBarTintManager.SystemBarTintConfig config;
    private static BaseApplication instance;
    private long defaultClickDelayTime=1000;

    public static BaseApplication getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;


       // initCrashHandler(); // 初始化程序崩溃捕捉处理
        initPrefs(); // 初始化SharedPreference
        initHttpConfig();
        //友盟统计日志加密
//        MobclickAgent.enableEncrypt(false);
    }

    /**
     * 默认使用okhttp请求网络并且忽略所有https证书
     * 并且忽略urlConnection的https证书认证
     */
    protected void initHttpConfig() {
        HTTPSTrustManager.allowAllSSL();
        NetRequest.setRequestQueue(RequestQueue.newRequestQueue(RxVolley.CACHE_FOLDER,
                new OkHttpIgnoreHttpsStack(new OkHttpClient())));
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

    public long getDefaultClickDelayTime() {
        return defaultClickDelayTime;
    }

    public void setDefaultClickDelayTime(long defaultClickDelayTime) {
        this.defaultClickDelayTime = defaultClickDelayTime;
    }

    public abstract Class getLoginClass();
}
