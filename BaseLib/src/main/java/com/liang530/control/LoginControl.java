package com.liang530.control;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.liang530.security.des3.Des3;

/**
 * @作者: XQ
 * @创建时间：15-7-20 下午10:08
 * @类说明:登录控制类
 */
public class LoginControl {
    protected static final String PRFS_LOGIN_ACCOUNT = "account";
    protected static final String PRFS_LOGIN_KEY = "key";
    protected static final String PRFS_LOGIN_OPENDID = "opendid";
    protected static final String PRFS_LOGIN_ID = "id";
    protected static final String PRFS_LOGIN_SEQID = "seqid";

    protected static final String PRFS_LOGIN_PHONE = "phone";
    protected static final String PRFS_LOGIN_USERICO = "userico";
    protected static SharedPreferences login_sp =null;
    protected static String PRFS_LOGIN_TOKEN = "token";
    protected static String PRFS_LOGIN_USERNAME = "username";
    protected static String PRFS_LOGIN_USERPSW = "userpsw";
    protected static String PRFS_LOGIN_DES_USERPSW = "userDespsw";
    protected static String PRFS_LOGIN_RECORDPWD = "recordPwd";
    protected static String PRFS_LOGIN_USERID = "userid";
    protected static String SEND_POINT_ID = "deliveryId";
    protected static String userToken;
    protected static String userName;
    protected static String userPsw;
    protected static String desUserPsw;
    protected static String seqId;
    protected static String phone;

    protected static String userIco;
    protected static int userId;
    protected static int sendPointId;
    protected static boolean mRecordPwd;//是否记住密码
    protected static  String account;
    protected static  String key;
    protected static  String opendId;
    protected static String id;


    public static synchronized void init(Context context){
        if(login_sp ==null){
            login_sp = context.getSharedPreferences(LoginControl.class.getName(), Context.MODE_PRIVATE);
        }
    }
    public static void saveUserIco(String userIcoStr){
        if (!TextUtils.isEmpty(userIcoStr)) {
            userIco = userIcoStr;
            login_sp.edit().putString(PRFS_LOGIN_USERICO, userIcoStr).commit();
        }
    }
    public static String getUserIco() {
        if(!TextUtils.isEmpty(userIco)){
            return userIco;
        }
        userIco = login_sp.getString(PRFS_LOGIN_USERICO, "");
        return userIco;
    }
    public static void savePhone(String phoneStr){
        if (!TextUtils.isEmpty(phoneStr)) {
            phone = phoneStr;
            login_sp.edit().putString(PRFS_LOGIN_PHONE, phoneStr).commit();
        }
    }

    public static String getPhone() {
        if(!TextUtils.isEmpty(phone)){
            return phone;
        }
        phone = login_sp.getString(PRFS_LOGIN_PHONE, "");
        return phone;
    }
    public static void saveSeqId(String seqIdStr){
        if (!TextUtils.isEmpty(seqIdStr)) {
            seqId = seqIdStr;
            login_sp.edit().putString(PRFS_LOGIN_SEQID, seqIdStr).commit();
        }
    }

    public static String getSeqId() {
        if(!TextUtils.isEmpty(seqId)){
            return seqId;
        }
        seqId = login_sp.getString(PRFS_LOGIN_SEQID, "");
        return seqId;
    }

    public static void saveId(String idstr){
        if (!TextUtils.isEmpty(idstr)) {
            id = idstr;
            login_sp.edit().putString(PRFS_LOGIN_ID, idstr).commit();
        }
    }

    public static String getId() {
        if(!TextUtils.isEmpty(id)){
            return id;
        }
        id = login_sp.getString(PRFS_LOGIN_ID, "");
        return id;
    }

    public static void saveOpendId(String opendIdStr){
        if (!TextUtils.isEmpty(opendIdStr)) {
            opendId = opendIdStr;
            login_sp.edit().putString(PRFS_LOGIN_OPENDID, opendIdStr).commit();
        }
    }
    public static String getOpendId() {
        if(!TextUtils.isEmpty(opendId)){
            return opendId;
        }
        opendId = login_sp.getString(PRFS_LOGIN_OPENDID, "");
        return opendId;
    }


    public static void saveKey(String keyStr){
        if (!TextUtils.isEmpty(keyStr)) {
            key = keyStr;
            login_sp.edit().putString(PRFS_LOGIN_KEY, keyStr).commit();
        }
    }

    public static String getKey() {
        if(!TextUtils.isEmpty(key)){
            return key;
        }
        key = login_sp.getString(PRFS_LOGIN_KEY, "");
        return key;
    }

    public static void saveAccount(String accountStr){
        if (!TextUtils.isEmpty(accountStr)) {
            account = accountStr;
            login_sp.edit().putString(PRFS_LOGIN_ACCOUNT, accountStr).commit();
        }
    }
    public static String getAccount() {
        if(!TextUtils.isEmpty(account)){
            return account;
        }
        account = login_sp.getString(PRFS_LOGIN_ACCOUNT, "");
        return account;
    }


    public static void saveToken(String token) {
        if (!TextUtils.isEmpty(token)) {
            userToken = token;
            login_sp.edit().putString(PRFS_LOGIN_TOKEN, token).commit();
        }
    }

    public static void saveUserid(int userid) {
        if (userid > 0) {
            userId = userid;
            login_sp.edit().putInt(PRFS_LOGIN_USERID, userid).commit();
        }
    }

    public static int getUserId() {
        userId = login_sp.getInt(PRFS_LOGIN_USERID, 0);
        return userId;
    }
    public static void saveSendPointId(int id) {
        if (id > 0) {
            sendPointId = id;
            login_sp.edit().putInt(SEND_POINT_ID, id).commit();
        }
    }

    public static int getSendPointId() {
        sendPointId = login_sp.getInt(SEND_POINT_ID, 0);
        return sendPointId;
    }


    public static String getToken() {
        userToken = login_sp.getString(PRFS_LOGIN_TOKEN, "");
//        L.e("LoginControl--getToken=" + userToken);
        return userToken;
    }


    public static void saveUserName(String username) {
        if (!TextUtils.isEmpty(username)) {
            userName = username;
            login_sp.edit().putString(PRFS_LOGIN_USERNAME, username).commit();
        }
    }

    public static void saveUserPassword(String userPassword) {
        if (!TextUtils.isEmpty(userPassword)) {
            userPsw = userPassword;
            login_sp.edit().putString(PRFS_LOGIN_USERPSW, userPassword).commit();
        }
    }
    public static void saveDesUserPassword(String userPassword) {
        if (!TextUtils.isEmpty(userPassword)) {
            try {
                userPassword = Des3.encode(userPassword);
            } catch (Exception e) {
                e.printStackTrace();
                userPassword="";
            }
            login_sp.edit().putString(PRFS_LOGIN_DES_USERPSW, userPassword).commit();
        }
    }

    /**
     * 积分商城用的加密字符串
     *
     */
    public static void saveRecordPwd(boolean recordPwd) {
        mRecordPwd = recordPwd;
        login_sp.edit().putBoolean(PRFS_LOGIN_RECORDPWD, recordPwd).commit();
    }


    public static String getUserName() {
        if(!TextUtils.isEmpty(userName)){
            return userName;
        }
        userName = login_sp.getString(PRFS_LOGIN_USERNAME, "");
        return userName;
    }

    public static String getUserPassword() {
        if(!TextUtils.isEmpty(userPsw)){
            return userPsw;
        }
        userPsw = login_sp.getString(PRFS_LOGIN_USERPSW, "");
        return userPsw;
    }
    public static String getDesDecodeUserPassword() {
        if(!TextUtils.isEmpty(desUserPsw)) return desUserPsw;
        desUserPsw= login_sp.getString(PRFS_LOGIN_DES_USERPSW, "");
        try {
            desUserPsw = Des3.decode(desUserPsw);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return desUserPsw;
    }

    /**
     * @return 是否记住密码
     */
    public static boolean isRecordPwd() {
        mRecordPwd = login_sp.getBoolean(PRFS_LOGIN_RECORDPWD,true);
        return mRecordPwd;
    }


    //false 不为空
    public static boolean isLogin() {
        if (TextUtils.isEmpty(getToken())) {
            return false;
        } else {
            return true;
        }
    }

    public static void clearLogin() {
        userToken = null;
        userId = -1;
        userName = null;
        userPsw = null;
        desUserPsw=null;
        mRecordPwd = true;
        sendPointId = -1;
        opendId=null;
        key=null;
        account=null;
        if(isRecordPwd()){
            userName=getUserName();
            desUserPsw= getDesDecodeUserPassword();
        }
        if (login_sp!= null) {
            if (login_sp.edit() != null) {
                login_sp.edit().clear().commit();
            }
        }
        //保存了密码
        if(userName!=null){
            saveUserName(userName);
            saveDesUserPassword(desUserPsw);
        }
    }
    public static void clear(){
        userToken = null;
        userId = -1;
        userName = null;
        userPsw = null;
        mRecordPwd = true;
        sendPointId = -1;
        if (login_sp!= null) {
            if (login_sp.edit() != null) {
                login_sp.edit().clear().commit();
            }
        }
    }
}
