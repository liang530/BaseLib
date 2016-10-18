package com.liang530.views.refresh.mvc;

/**
 * 进度更新发送者
 *
 * @author LuckyJayce
 */
public interface ProgressSender {
    /**
     * 通知进度更新
     *
     * @param current  当前进度
     * @param total    总量
     * @param exraData 额外的数据，可以传null
     */
    void sendProgress(long current, long total, Object exraData);
}
