package com.liang530.baselib;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RxVolley.post("https://kyfw.12306.cn/otn", new HttpParams(), new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                Log.e("hongliang","https连接成功："+t);
            }

            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                super.onFailure(errorNo, strMsg, completionInfo);
                Log.e("hongliang","https连接失败："+completionInfo);
            }
        });
    }
}
