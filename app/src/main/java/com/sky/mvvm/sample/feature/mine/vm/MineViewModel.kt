package com.sky.mvvm.sample.feature.mine.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sky.mvvm.base.viewmodel.BaseViewModel
import com.sky.mvvm.core.common.net.ApiService
import com.sky.mvvm.ext.download.DownLoadManager
import com.sky.mvvm.ext.download.DownloadResultState
import com.sky.mvvm.ext.download.FileTool
import com.sky.mvvm.ext.download.OnDownLoadListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/3/4 20:30}</p>
 * <p>{@code description: 我的页面ViewModel，包含下载示例}</p>
 */
@HiltViewModel
class MineViewModel @Inject constructor(
    private val apiService: ApiService,
    private val downLoadManager: DownLoadManager
) : BaseViewModel() {

    /** 下载状态 LiveData */
    private val _downloadResultState = MutableLiveData<DownloadResultState>()
    val downloadResultState: LiveData<DownloadResultState> = _downloadResultState

    /** 下载标识 */
    private val downloadTag = "sample_download"

    /**
     * 开始下载
     * @param url 下载地址
     * @param saveName 保存文件名
     */
    fun startDownload(url: String, saveName: String) {
        val savePath = FileTool.getBasePath() + "/download"
        viewModelScope.launch {
            downLoadManager.downLoad(
                tag = downloadTag,
                url = url,
                savePath = savePath,
                saveName = saveName,
                reDownload = false,
                whetherHttps = true,
                loadListener = object : OnDownLoadListener {
                    override fun onDownLoadPrepare(key: String) {
                        _downloadResultState.postValue(DownloadResultState.onPending())
                    }

                    override fun onDownLoadError(key: String, throwable: Throwable) {
                        _downloadResultState.postValue(
                            DownloadResultState.onError(throwable.message ?: "下载失败")
                        )
                    }

                    override fun onDownLoadSuccess(key: String, path: String, size: Long) {
                        _downloadResultState.postValue(DownloadResultState.onSuccess(path, size))
                    }

                    override fun onDownLoadPause(key: String) {
                        _downloadResultState.postValue(DownloadResultState.onPause())
                    }

                    override fun onUpdate(key: String, progress: Int, read: Long, count: Long, done: Boolean) {
                        _downloadResultState.postValue(DownloadResultState.onProgress(read, count, progress))
                    }
                }
            )
        }
    }

    /**
     * 取消下载
     */
    fun cancelDownload() {
        downLoadManager.cancel(downloadTag)
    }
}
