package com.liang530.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.liang530.application.BaseActivity;

public class LoginActivity extends BaseActivity {
    public final static int LOGIN_REQUEST_CODE=1011;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public static void start(Context context) {
        Intent starter = new Intent(context.getPackageName()+".LoginActivity");
        starter.addCategory(Intent.CATEGORY_DEFAULT);
//        starter.setClassName(context,"ui."+LoginActivity.class.getSimpleName());
        context.startActivity(starter);
    }
    public static void startForResult(Activity activity,Integer requestId){
        Intent starter = new Intent(activity.getPackageName()+".LoginActivity");
        starter.addCategory(Intent.CATEGORY_DEFAULT);
        starter.putExtra("requestId",requestId);
        activity.startActivityForResult(starter,LOGIN_REQUEST_CODE);
    }
    public void setLoginSuccess(){
        Intent intent=new Intent();
        intent.putExtra("requestId",getIntent().getIntExtra("requestId",0));
        setResult(RESULT_CANCELED,intent);
        finish();
    }
    public void setLoginCancle(){
        Intent intent=new Intent();
        intent.putExtra("requestId",getIntent().getIntExtra("requestId",0));
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void finish() {
        Intent intent=new Intent();
        intent.putExtra("requestId",getIntent().getIntExtra("requestId",0));
        setResult(RESULT_CANCELED,intent);
        super.finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("requestId",getIntent().getIntExtra("requestId",0));
        setResult(RESULT_CANCELED,intent);
        finish();
    }
}
