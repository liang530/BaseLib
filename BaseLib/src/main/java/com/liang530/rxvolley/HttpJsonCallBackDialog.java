package com.liang530.rxvolley;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Looper;

import com.google.gson.Gson;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.application.BaseApplication;
import com.liang530.log.L;
import com.liang530.log.T;
import com.liang530.manager.AppManager;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/**
 * Created by 刘红亮 on 2016/3/6.
 */
public class HttpJsonCallBackDialog<E> extends HttpCallback {
    public final static String HTTP_CODE="code";
    public final static String HTTP_MSG="msg";
    public final static String HTTP_DATA="data";
    private WeakReference<Dialog> wdialog;
    HttpParams params;
    private boolean showErrorToast;
    public HttpJsonCallBackDialog() {
    }

    public void setNetRequest(NetRequest netRequest) {
        this.wdialog = new WeakReference<Dialog>(netRequest.getDialog());
        this.showErrorToast=netRequest.isShowErrorToast();
        this.params = netRequest.getParams();
    }
    @Override
    public void onPreStart() {
        super.onPreStart();
        if (wdialog.get() != null) {//如果dialog还存在，则弹出
            wdialog.get().show();
        }
    }

    @Override
    public void onPreHttp() {
        super.onPreHttp();
    }

    @Override
    public void onSuccessInAsync(byte[] t) {
        super.onSuccessInAsync(t);
    }

    @Override
    public void onSuccess(String t) {
        super.onSuccess(t);
        L.json("netnet-success", t);
        try {
            JSONObject jsonObject= new JSONObject(t);
            int httpCode = jsonObject.getInt(HTTP_CODE);
            if(httpCode>=400){
                if(httpCode==403){//重新登陆
                    Intent intent=new Intent();
                    intent.setClassName(BaseApplication.getInstance(), BaseApplication.getInstance().getLoginClass().getName());
                    if(AppManager.getActivityStack().size()==0){
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        BaseApplication.getInstance().startActivity(intent);
                    }else{
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        AppManager.getAppManager().currentActivity().startActivity(intent);
                    }
                }
                onJsonSuccess(httpCode,jsonObject.getString(HTTP_MSG),t);
            }else{
                E data = new Gson().fromJson(t, getTClass());
                onDataSuccess(data);
            }
        } catch (Exception e) {
            onJsonSuccess(0,"数据异常",t);
            L.e("json解析异常");
            e.printStackTrace();
        }
    }
    public void onDataSuccess(E data){

    }
    public void onJsonSuccess(int code,String msg,String data){
        if(showErrorToast&&isInMainThread()){
            if(AppManager.getActivityStack().size()!=0){
                T.s(AppManager.getActivityStack().get(0),msg);
            }
        }
    }

    @Override
    public void onSuccess(Map<String, String> headers, byte[] t) {
        super.onSuccess(headers, t);
    }

    @Override
    public void onFailure(int errorNo, String strMsg, String completionInfo) {
        super.onFailure(errorNo, strMsg, completionInfo);
        L.e("netnet-error", completionInfo);

        if(showErrorToast&&isInMainThread()){
            if(AppManager.getActivityStack().size()!=0){
                T.s(AppManager.getActivityStack().get(0),VolleyErrorMsg.getMessage(errorNo, strMsg));
            }

        }
    }

    @Override
    public void onFinish() {
        super.onFinish();
        if (wdialog.get() != null) {//如果dialog还存在，则关闭
            wdialog.get().dismiss();
        }
    }

    @Override
    public void onSuccess(Map<String, String> headers, Bitmap bitmap) {
        super.onSuccess(headers, bitmap);
    }
    protected Class<E> getTClass() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Type resultType = type.getActualTypeArguments()[0];
        if (resultType instanceof Class) {
            return (Class<E>) resultType;
        } else {
            // 处理集合
            try {
                Field field = resultType.getClass().getDeclaredField("rawTypeName");
                field.setAccessible(true);
                String rawTypeName = (String) field.get(resultType);
                return (Class<E>) Class.forName(rawTypeName);
            } catch (Exception e) {
                return (Class<E>) Collection.class;
            }
        }
    }
    public static boolean isInMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
