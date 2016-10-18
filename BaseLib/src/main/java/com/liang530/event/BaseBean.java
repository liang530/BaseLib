package com.liang530.event;

/**
 * Created by Nowy on 2016/4/11.
 * 继承JSON基类
 * JSON：{"code":1,"msg":"","data":T}
 */
public class BaseBean<T> {
    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
