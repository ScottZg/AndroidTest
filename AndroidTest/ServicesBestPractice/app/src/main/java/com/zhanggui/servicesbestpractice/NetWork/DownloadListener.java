package com.zhanggui.servicesbestpractice.NetWork;

/**
 * Created by zhanggui on 2017/2/23.
 */

/**
 * 下载监听接口
 */
public interface DownloadListener {
    /**
     * 进度
     * @param progress 当前进度
     */
    void onProgress(int progress);

    /**
     * 成功下载接口
     */
    void onSuccess();

    /**
     * 下载失败接口
     */
    void onFailed();

    /**
     * 暂停接口
     */
    void onPaused();

    /**
     * 取消下载
     */
    void onCanceled();
}
