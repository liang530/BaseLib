1.去sharesdk下载最新的sdk，通过cmd 运行分享自动化配置的jar    java -jar  文件路径

2.复制自动生成的资源和代码到项目中


3.配置清单文件:见清单文件配置,特别注意配置QQ号

4.配置ShareSDK.xml 中申请好的信息：
        4.1 如果在sharesdk官网后台配置了参数，则会优先使用后台的
        4.2 如果后台没有配置，可以通过ShareSdk.setPlatformDevInfo()来设置不同平台的配置
        4.3 如果后台没有，也没有动态配置，则使用sharesdk.xml中的配置

6.可根据需要配置ShareUtil的分享参数

5.复制sharesdk 这个包到项目中，可以如下调用分享，和第三方登录

    private void testShare(){
        ShareDataEvent event=new ShareDataEvent();
        event.text="阳光抗癌上线啦！";
        event.title="阳光抗癌上线啦！";
        event.titleUrl="http://120.24.182.187:8090/anticancer/yangguangkangai/weixin/healthFriendsCircle.html";
        event.url="http://120.24.182.187:8090/anticancer/yangguangkangai/weixin/healthFriendsCircle.html";
        event.resouceId=R.mipmap.logo;
        new ShareUtil().oneKeyShare(getApplicationContext(),event, new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                L.e(TAG, "分享成功！：" + hashMap);
            }
            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                L.e(TAG, "失败！");
            }
            @Override
            public void onCancel(Platform platform, int i) {
                L.e(TAG, "取消！");
            }
        });
    }
    private void gotoQQLogin(){
        final Platform loginQQ = ShareSDK.getPlatform(this, QQ.NAME);
        loginQQ.showUser(null);
        loginQQ.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                T.s("登陆成功！：" + hashMap);
                L.e(TAG, "登陆成功！：" + hashMap);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
//                T.s("登陆Error！："+throwable.getMessage());
                loginQQ.removeAccount();
            }

            @Override
            public void onCancel(Platform platform, int i) {
                L.e("登陆取消！：" + i);
            }
        });
        loginQQ.authorize();
    }





