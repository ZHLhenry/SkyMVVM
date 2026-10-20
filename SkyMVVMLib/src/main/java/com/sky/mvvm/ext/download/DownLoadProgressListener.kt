package com.sky.mvvm.ext.download

/**
 * @Class: DownLoadProgressListener
 * @Author: Henry
 * @Date: 2025/2/23 10:36
 * @Description: 下载进度与状态回调接口定义
 */

interface DownLoadProgressListener {

    /**
     * 下载进度
     * @param key url
     * @param progress  进度
     * @param read  读取
     * @param count 总共长度
     * @param done  是否完成
     */
    fun onUpdate(key: String, progress: Int, read: Long, count: Long, done: Boolean)
}

interface OnDownLoadListener : DownLoadProgressListener {

    //等待下载
    fun onDownLoadPrepare(key: String)

    //下载失败
    fun onDownLoadFailure(key: String, throwable: Throwable)

    //下载成功
    fun onDownLoadSuccess(key: String, path: String, size: Long)

    //下载暂停（保留断点，可续传）
    fun onDownLoadPause(key: String)

    //下载取消（删除已下载文件，不可续传）
    fun onDownLoadCancel(key: String) {}
}