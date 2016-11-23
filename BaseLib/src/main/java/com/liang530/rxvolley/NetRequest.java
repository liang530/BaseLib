package com.liang530.rxvolley;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.FileRequest;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.client.ProgressListener;
import com.kymjs.rxvolley.client.RequestConfig;
import com.kymjs.rxvolley.http.DefaultRetryPolicy;
import com.kymjs.rxvolley.http.RequestQueue;

import com.liang530.application.BaseApplication;
import com.liang530.control.LoginControl;
import com.liang530.log.L;
import com.liang530.manager.AppManager;
import com.liang530.security.HttpEncrypt;
import com.liang530.utils.BaseAppUtil;

/**
 * Created by 刘红亮 on 2016/3/6.
 */
public class NetRequest {
    private static final int REQ_POST_TIME_OUT_DEBUG = 5*60*1000;
    private static final int REQ_POST_TIME_OUT   = 15*1000;
    public static boolean mIsDebug = false;
    private boolean showErrorToast=true;
    private String flag[];
    private HttpParams params;
    private Dialog dialog;
    private Object mTag;
    private boolean mShouldCache=false;
    private final String  TAG=NetRequest.class.getSimpleName();
    private static boolean safeRequest=false;

    /**
     * 看request(mTag)
     * @return
     */
    @Deprecated
    public static NetRequest request() {
        return new NetRequest(null);
    }

    public static NetRequest request(Object mTag) {
        return new NetRequest(mTag);
    }
    private NetRequest(Object tag){
        this.mTag=tag;
    }
    private static boolean againRequestSwitch=true;
    private  boolean againRequest=true;
    private String gotoActName_Position;//登陆取消要跳转的类和位置
    /**
     * 获取一个请求队列(单例)
     */
    public synchronized static RequestQueue getRequestQueue() {
        return RxVolley.getRequestQueue();
    }

    public static boolean isSafeRequest() {
        return safeRequest;
    }

    public static void setSafeRequest(boolean safeRequest) {
        NetRequest.safeRequest = safeRequest;
    }

    public boolean isAgainRequest() {
        return againRequest;
    }

    public void setAgainRequest(boolean againRequest) {
        this.againRequest = againRequest;
    }

    public String getGotoActName_Position() {
        return gotoActName_Position;
    }

    public void setGotoActName_Position(String gotoActName_Position) {
        this.gotoActName_Position = gotoActName_Position;
    }

    public static boolean isAgainRequestSwitch() {
        return againRequestSwitch;
    }

    public static void setAgainRequestSwitch(boolean againRequestSwitch) {
        NetRequest.againRequestSwitch = againRequestSwitch;
    }

    /**
     * 设置请求队列,必须在调用Core#getRequestQueue()之前设置
     *
     * @return 是否设置成功
     */
    public synchronized static boolean setRequestQueue(RequestQueue queue) {
        return RxVolley.setRequestQueue(queue);
    }

    public boolean isShowErrorToast() {
        return showErrorToast;
    }

    public NetRequest setShowErrorToast(boolean showErrorToast) {
        this.showErrorToast = showErrorToast;
        return this;
    }

    public String[] getFlag() {
        return flag;
    }

    public HttpParams getParams() {
        return params;
    }

    public Dialog getDialog() {
        return dialog;
    }

    /**
     *  设置请求标示
     * @param flag 单个或多个标示
     * @return
     */
    public NetRequest setFlag(String ... flag) {
        this.flag = flag;
        return this;
    }

    public boolean ismShouldCache() {
        return mShouldCache;
    }

    public void setShouldCache(boolean mShouldCache) {
        this.mShouldCache = mShouldCache;
    }

    /**
     * 设置请求参数
     * @param params 请求参数
     * @return
     */
    public NetRequest setParams(HttpParams params) {
        this.params = params;
        return this;
    }
    @Deprecated
    public NetRequest showDialog(Context context,String message,boolean canCancel) {
        Activity activity = AppManager.getAppManager().currentActivity();
        if(activity!=null){
            this.dialog=new LoadingDialogView(activity);
            this.dialog.setCancelable(canCancel);
            this.dialog.setTitle(message);
        }
        return this;
    }

    /**
     * 使用不带Context的
     * @param context
     * @param canCancel
     * @return
     */
    @Deprecated
    public NetRequest showDialog(Context context,boolean canCancel) {
        Activity activity = AppManager.getAppManager().currentActivity();
        if(activity!=null){
            this.dialog=new LoadingDialogView(activity);
            this.dialog.setCancelable(canCancel);
        }
        return this;
    }
    /**
     * 使用不带Context的
     * @param canCancel
     * @return
     */
    public NetRequest showDialog(String message,boolean canCancel) {
        Activity activity = AppManager.getAppManager().currentActivity();
        if(activity!=null){
            this.dialog=new LoadingDialogView(activity);
            this.dialog.setCancelable(canCancel);
            this.dialog.setTitle(message);
        }
        return this;
    }
    public NetRequest showDialog(boolean canCancel) {
        Activity activity = AppManager.getAppManager().currentActivity();
        if(activity!=null){
            this.dialog=new LoadingDialogView(activity);
            this.dialog.setCancelable(canCancel);
        }
        return this;
    }

    public NetRequest setDialog(Dialog dialog) {
        this.dialog=dialog;
        return this;
    }
    public NetRequest setTag(Object tag){
        this.mTag=tag;
        return this;
    }
    /**
     *  接口方式的get 请求
     * @param url
     * @param httpResultListener 回调接口
     */
    public void get(String url,HttpResultListener httpResultListener){
        if(mTag==null) this.mTag=httpResultListener;
        getHttp(url, new HttpCallBackDialog(this, "get",httpResultListener, url));
    }
    /**
     *  接口方式的get 请求
     * @param url
     * @param httpJsonCallBackDialog 回调接口
     */
    public void get(String url,HttpJsonCallBackDialog httpJsonCallBackDialog){
        if(mTag==null) this.mTag=httpJsonCallBackDialog;
        httpJsonCallBackDialog.setNetRequest(this);
        getHttp(url, httpJsonCallBackDialog);
    }
    /**
     *  接口方式的post 请求
     * @param url
     * @param httpJsonCallBackDialog 回调接口
     */
    public void post(String url, HttpJsonCallBackDialog httpJsonCallBackDialog) {
        if(mTag==null) this.mTag=httpJsonCallBackDialog;
        httpJsonCallBackDialog.setNetRequest(this);
        postHttp(url, httpJsonCallBackDialog);
    }
    /**
     *  接口方式的post 请求
     * @param url
     * @param httpResultListener 回调接口
     */
    public void post(String url, HttpResultListener httpResultListener) {
        if(mTag==null) this.mTag=httpResultListener;

        postHttp(url, new HttpCallBackDialog(this, "post", httpResultListener,url));
    }
    /**
     * 接口方式的post 上传进度回调请求
     * @param url
     * @param listener 上传进度回调接口
     * @param httpJsonCallBackDialog  回调接口
     */
    public void post(String url,ProgressListener listener, HttpJsonCallBackDialog httpJsonCallBackDialog) {
        if(mTag==null) this.mTag=httpJsonCallBackDialog;
        httpJsonCallBackDialog.setNetRequest(this);
        postHttp(url, listener, httpJsonCallBackDialog);
    }
    /**
     * 接口方式的post 上传进度回调请求
     * @param url
     * @param listener 上传进度回调接口
     * @param httpResultListener  回调接口
     */
    public void post(String url,ProgressListener listener, HttpResultListener httpResultListener) {
        if(mTag==null) this.mTag=httpResultListener;
        postHttp(url, listener, new HttpCallBackDialog(this, "post", httpResultListener,url));
    }
    /**
     * 下载
     *
     * @param storeFilePath    本地存储绝对路径
     * @param url              要下载的文件的url
     * @param progressListener 下载进度回调
     * @param callback         回调
     */
    public void download(String storeFilePath, String url, ProgressListener
            progressListener, HttpCallback callback) {
        addEncrypt(params);
        printUrl(url, params);
        RequestConfig config = new RequestConfig();
        config.mUrl = url;
        config.mRetryPolicy = new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                20, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        FileRequest request = new FileRequest(storeFilePath, config, callback);
        request.setTag(url);
        request.setOnProgressListener(progressListener);
        new RxVolley.Builder().setRequest(request).doTask();
    }

    public void getHttp(String url, HttpCallback callback) {
        addEncrypt(params);
        printUrl(url, params);

        if(BaseAppUtil.isOnline(BaseApplication.getInstance())){
            new RxVolley.Builder().url(url).shouldCache(mShouldCache)
                    .params(params!=null?params:new HttpParams()).setTag(mTag!=null?mTag:url).callback(callback).doTask();
        }else{
            callback.onFailure(502,"手机可能未连接网络","由于无网络，再请求前被拦截");
        }
    }
    public void postHttp(String url, HttpCallback callback) {
        addEncrypt(params);
        printUrl(url, params);
        if(BaseAppUtil.isOnline(BaseApplication.getInstance())){
            new RxVolley.Builder().url(url).shouldCache(mShouldCache).retryPolicy(new DefaultRetryPolicy(mIsDebug ? REQ_POST_TIME_OUT_DEBUG : REQ_POST_TIME_OUT,
                    0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)).params(params != null ? params : new HttpParams()).httpMethod(RxVolley.Method.POST).setTag(mTag!=null?mTag:url).callback(callback).doTask();
        }else{
            callback.onFailure(502,"手机可能未连接网络","由于无网络，再请求前被拦截");
        }
    }

    public void postHttp(String url,  ProgressListener listener, HttpCallback callback) {
        addEncrypt(params);
        printUrl(url, params);
        if(BaseAppUtil.isOnline(BaseApplication.getInstance())){
            new RxVolley.Builder().url(url).shouldCache(mShouldCache).params(params != null ? params : new HttpParams()).progressListener(listener).httpMethod(RxVolley.Method.POST).setTag(mTag!=null?mTag:url)
                    .callback(callback).doTask();
        }else{
            callback.onFailure(502,"手机可能未连接网络","由于无网络，再请求前被拦截");
        }

    }
    /**
     * 添加签名
     * @param params
     */
    public static void addEncrypt(HttpParams params){
        if(safeRequest){
            String appSign = HttpEncrypt.getAppSign(params, LoginControl.getUserPassword());
            if(params==null){
                params=new HttpParams();
            }
            params.put("sign",appSign);//添加加密参数i
        }
    }

    public void printUrl(String url,HttpParams params){
        if(params==null){
            params=new HttpParams();
        }
        L.e("netnet-url", new StringBuffer(url).append(params.getUrlParams()).toString());
    }

    public static void setIsDebug(boolean isDebug) {
        NetRequest.mIsDebug = mIsDebug;
    }
}
