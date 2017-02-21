package com.shuqu.microcredit.Interface;

/**
 * Class Info ：
 * Created by lyndon on 16/9/22.
 */

public interface IUpdateProgress {
    /**
     * download start
     */
    public void start();

    /**
     * update download progress
     * @param progress
     */
    public void update(int progress);

    /**
     * download success
     */
    public void success();

    /**
     * download error
     */
    public void error();
}
