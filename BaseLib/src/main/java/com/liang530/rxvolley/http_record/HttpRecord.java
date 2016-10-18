package com.liang530.rxvolley.http_record;

/**
 * Created by hongliang on 16-5-16.
 */
public class HttpRecord {
    public final static int TYPE_TOKEN=1;
    public final static int TYPE_FAIL=2;
    private long id;
    private String flagJson;//flag数组
    private String url;
    private String params;//params数组
    private String files; //文件数组
    private String method;
    private String listenerName;
    private String gotoActName_Position; //用户取消后去往的activity和位置
    private int showDialog;
    private int count;
    private int type;//1:token 过期  2:请求失败
    private long createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFlagJson() {
        return flagJson;
    }

    public void setFlagJson(String flagJson) {
        this.flagJson = flagJson;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getListenerName() {
        return listenerName;
    }

    public void setListenerName(String listenerName) {
        this.listenerName = listenerName;
    }

    public int getShowDialog() {
        return showDialog;
    }

    public void setShowDialog(int showDialog) {
        this.showDialog = showDialog;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getGotoActName_Position() {
        return gotoActName_Position;
    }

    public void setGotoActName_Position(String gotoActName_Position) {
        this.gotoActName_Position = gotoActName_Position;
    }

    @Override
    public String toString() {
        return "HttpRecord{" +
                "id=" + id +
                ", flagJson='" + flagJson + '\'' +
                ", url='" + url + '\'' +
                ", params='" + params + '\'' +
                ", files='" + files + '\'' +
                ", method='" + method + '\'' +
                ", listenerName='" + listenerName + '\'' +
                ", gotoActName_Position='" + gotoActName_Position + '\'' +
                ", showDialog=" + showDialog +
                ", count=" + count +
                ", type=" + type +
                ", createTime=" + createTime +
                '}';
    }
}
