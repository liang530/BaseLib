package com.liang530.rxvolley;

import android.app.Dialog;
import android.graphics.Bitmap;

import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Map;

import com.liang530.log.L;
import com.liang530.rxvolley.http_record.HttpRecord;

/**
 * Created by 刘红亮 on 2016/3/6.
 */
public class HttpCallBackDialog extends HttpCallback {
    private WeakReference<Dialog> wdialog;
    private WeakReference<HttpResultListener> listenerWeakReference;
    private String flag[];
    private String url;
    HttpParams params;
    private boolean againRequest;
    private String gotoActName_Position;
    private String listenerName;
    private String method;

    public HttpCallBackDialog(NetRequest netRequest, String method, HttpResultListener listener, String url) {
        this.wdialog = new WeakReference<Dialog>(netRequest.getDialog());
        this.listenerWeakReference = new WeakReference<HttpResultListener>(listener);
        this.flag = netRequest.getFlag();
        this.url = url;
        this.againRequest = netRequest.isAgainRequestSwitch() && netRequest.isAgainRequest();
        this.gotoActName_Position = netRequest.getGotoActName_Position();
        this.params = netRequest.getParams();
        this.listenerName = listener.getClass().getName();
        this.method = method;
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
        if (listenerWeakReference.get() != null) {
            Integer id=null;
            //判断code码
            try {

                JSONObject jsonObject = new JSONObject(t);
                int code = jsonObject.getInt("code");
                if (code == 40000) {//token过期
                   id=recordHttp(HttpRecord.TYPE_TOKEN);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            listenerWeakReference.get().onHttpResult(t, -1, flag, null,id);
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
        if (listenerWeakReference.get() != null) {
            listenerWeakReference.get().onHttpResult(null, errorNo, flag, strMsg,null);
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

    /**
     * 记录http请求到数据库去
     * @param type
     * @return
     */
    private Integer recordHttp(int type) {
//        if (againRequest) {//将联网操作存入数据库
//            HttpRecord httpRecord = new HttpRecord();
//            httpRecord.setUrl(url);
//            if(flag!=null){
//                httpRecord.setFlagJson(JSON.toJSONString(flag));
//            }
//            httpRecord.setMethod(method);
//            httpRecord.setGotoActName_Position(gotoActName_Position);
//            httpRecord.setListenerName(listenerName);
//            httpRecord.setShowDialog(wdialog.get() == null ? 0 : 1);
//            httpRecord.setType(type);
//            if(params!=null){
//                ArrayList<HttpParamsEntry> urlParamsMap = params.getUrlParamsMap();
//                httpRecord.setParams(JSON.toJSONString(urlParamsMap));
//            }
//            return HttpRecordUtil.insert(httpRecord);
//        }
        return null;
    }
//    public void clearParam( ArrayList<HttpParamsEntry> params){
//        for(int index=params.size()-1;index>=0;index--){
//            HttpParamsEntry httpParamsEntry = params.get(index);
//            if("sign".equals(httpParamsEntry.k)||si){
//                params.remove(httpParamsEntry);
//            }
//        }
//    }
}
