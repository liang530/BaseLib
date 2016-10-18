package com.liang530.application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.liang530.log.L;
import com.liang530.manager.AppManager;
import com.liang530.rxvolley.NetRequest;
import com.liang530.utils.BaseAppUtil;

/**
 * @version 1.0
 * @#作者:XQ
 * @#创建时间：15-9-18 上午11:46
 * @#类 说 明:基础的Activity.
 */
public class BaseActivity extends AppCompatActivity {
    protected String TAG = getClass().getName();
    protected Context mContext;
    protected Activity mActivity;

    protected void le(String msg) {
        L.e(TAG, msg);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this.mActivity = this;
//        if(BaseApplication.getInstance().requireStatusColor){
//            BaseAppUtil.setStatusImmerse(this, BaseApplication.getInstance().getStatusColor(), BaseApplication.getInstance().isDarkmode());
//        }
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBar();
    }

    /**
     * 设置状态栏，默认为Appliation
     */
    protected void setStatusBar() {
        if(BaseApplication.getInstance().getConfig()!=null){
            BaseAppUtil.setStatusImmerse(this,BaseApplication.getInstance().getConfig());
        }
    }

    @Override
    protected void onDestroy() {
        AppManager.getAppManager().removeActivity(this);
        NetRequest.getRequestQueue().cancelAll(this);//activity销毁时取消请求
        super.onDestroy();
    }

    /**
     * 加入了友盟时长统计
     */
    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
    }

    /**
     * 加入了友盟时长统计
     */
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }

    /**
     * 跳转activity
     *
     * @param clazz  目标activity
     * @param bundle 携带信息
     */
    public static void go2Act(Context context, Class<? extends Activity> clazz, Bundle bundle) {
        Intent intent = new Intent(context, clazz);
        if (bundle != null)
            intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 回调跳转
     *
     * @param activity
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    public static void startForResult(Activity activity, Class<? extends Activity> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(activity, clazz);
        if (bundle != null)
            intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestCode);
    }


    public static void startForResult(Fragment fragment, Class<? extends Activity> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(fragment.getContext(), clazz);
        if (bundle != null)
            intent.putExtras(bundle);
        fragment.startActivityForResult(intent, requestCode);
    }


    public static void startForResult(Fragment fragment, Class<? extends Activity> clazz, int requestCode) {
        startForResult(fragment, clazz, requestCode, null);
    }


    public static void startForResult(Activity activity, Class<? extends Activity> clazz, int requestCode) {
        startForResult(activity, clazz, requestCode, null);
    }

    /**
     * 跳转activity
     *
     * @param clazz
     */
    public static void go2Act(Context context, Class<? extends Activity> clazz) {
        go2Act(context, clazz, null);
    }
//    protected void loginSuccess(int id) {
//        if (id != 0) {
//            HttpRecord httpRecord = HttpRecordUtil.getById(id);
//            if (httpRecord == null) return;
//            if (this instanceof HttpResultListener && this.getClass().getName().equals(httpRecord.getListenerName())) {
//
//                NetRequest request = NetRequest.request();
//                try {
//                    String flagJson = httpRecord.getFlagJson();
//                    String flag[] = JSON.parseObject(flagJson, new TypeReference<String[]>() {
//                    });
//                    request = request.setFlag(flag);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (httpRecord.getShowDialog() == 1) {
//                    request = request.showDialog(false);
//                }
//                try {
//                    String paramsJson = httpRecord.getParams();
//                    HttpParams httpParams = new HttpParams();
//                    ArrayList<HttpParamsEntry> params = JSON.parseObject(paramsJson, new TypeReference<ArrayList<HttpParamsEntry>>() {
//                    });
//                    for (HttpParamsEntry entry : params) {
//                        if ("sign".equals(entry.k)) {
//                            continue;
//                        }
//                        if ("token".equals(entry.k)) {
//                            httpParams.put("token", LoginControl.getToken());
//                        } else if ("userId".equals(entry.k)) {
//                            httpParams.put("userId", LoginControl.getUserId());
//                        } else {
//                            httpParams.put(entry.k, entry.v);
//                        }
//
//                    }
//                    request = request.setParams(httpParams);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                request.setAgainRequest(false);
//                request.setGotoActName_Position(httpRecord.getGotoActName_Position());
//                request.post(httpRecord.getUrl(), (HttpResultListener) this);
//            }
//        }
//
//    }

//    protected void loginCancel(int id) {
//        if (id != 0) {
//            HttpRecord httpRecord = HttpRecordUtil.getById(id);
//            if (httpRecord == null) return;
//            if (TextUtils.isEmpty(httpRecord.getGotoActName_Position())) {
//                int i = httpRecord.getGotoActName_Position().lastIndexOf("_");
//                if (i > 0) {
//                    String name = httpRecord.getGotoActName_Position().substring(0, i);
//                    String positionStr = httpRecord.getGotoActName_Position().substring(i + 1);
//                    try {
//                        Activity activity = AppManager.getAppManager().finishThisUpActivity(name);
//                        if (activity instanceof TabInterface) {
//                            ((TabInterface) activity).switchPosition(Integer.parseInt(positionStr));
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == LoginActivity.LOGIN_REQUEST_CODE) {//处理登陆回来联网重试方案
//            if (resultCode == RESULT_OK) {//登陆成功
//                if (data != null) {
//                    int id = data.getIntExtra("requestId", 0);
//                    loginSuccess(id);
//                }
//
//            } else if (resultCode == RESULT_CANCELED) {//用户取消
//                int id = data.getIntExtra("requestId", 0);
//                loginCancel(id);
//            }
//        }
//    }


}
