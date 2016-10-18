package com.liang530.views.refresh.mvc;

/**
 * 用于请求结束时发送数据或者发送异常
 *
 * @param <DATA>
 */
public interface ResponseSender<DATA> extends ProgressSender {

    void sendError(Exception exception);

    void sendData(DATA data);

    void sendProgress(long current, long total, Object extraData);

}