package com.sky.mvvm.ext.download

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.sky.mvvm.R

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/2/23 10:38}</p>
 * <p>{@code description: 文件描述}</p>
 */
fun downLoadExt(content: Context, downloadResultState: MutableLiveData<DownloadResultState>): OnDownLoadListener {
    return object : OnDownLoadListener {
        override fun onDownLoadPrepare(key: String) {
            //开始下载
            downloadResultState.postValue(DownloadResultState.onPending())
        }

        override fun onDownLoadError(key: String, throwable: Throwable) {
            //下载错误
            downloadResultState.postValue(
                DownloadResultState.onError(
                    throwable.message ?: content.getString(R.string.sky_mvvmlib_download_error)
                )
            )
        }

        override fun onDownLoadSuccess(key: String, path: String, size: Long) {
            //下载成功
            downloadResultState.postValue(DownloadResultState.onSuccess(path, size))
        }

        override fun onDownLoadPause(key: String) {
            //下载暂停
            downloadResultState.postValue(DownloadResultState.onPause())
        }

        override fun onUpdate(key: String, progress: Int, read: Long, count: Long, done: Boolean) {
            //正在下载
            downloadResultState.postValue(DownloadResultState.onProgress(read, count, progress))
        }
    }
}