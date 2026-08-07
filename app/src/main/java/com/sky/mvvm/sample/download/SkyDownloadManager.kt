package com.sky.mvvm.sample.download

import com.azhon.appupdate.base.BaseHttpDownloadManager
import com.azhon.appupdate.base.bean.DownloadStatus
import com.sky.mvvm.ext.download.DownLoadManager
import com.sky.mvvm.ext.download.FileTool
import com.sky.mvvm.ext.download.OnDownLoadListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import java.io.File

/**
 * <p>{@code className: SkyDownloadManager}</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2026/7/27}</p>
 * <p>{@code description: 继承 AppUpdate 的 BaseHttpDownloadManager，
 * 内部使用 SkyMVVMLib 的 DownLoadManager 实现下载，
 * 将下载回调转换为 DownloadStatus Flow 供 AppUpdate 库消费}</p>
 */
class SkyDownloadManager : BaseHttpDownloadManager() {

    private val downloadTag = "app_update_sky_download"
    private val downLoadManager = DownLoadManager()
    private var scope: CoroutineScope? = null

    override fun download(apkUrl: String, apkName: String): Flow<DownloadStatus> {
        return callbackFlow {
            val savePath = FileTool.getBasePath() + "/download"
            scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

            scope?.launch {
                downLoadManager.downLoad(
                    tag = downloadTag,
                    url = apkUrl,
                    savePath = savePath,
                    saveName = apkName,
                    reDownload = false,
                    whetherHttps = true,
                    loadListener = object : OnDownLoadListener {
                        override fun onDownLoadPrepare(key: String) {
                            trySend(DownloadStatus.Start)
                        }

                        override fun onDownLoadError(key: String, throwable: Throwable) {
                            trySend(DownloadStatus.Error(throwable))
                        }

                        override fun onDownLoadSuccess(key: String, path: String, size: Long) {
                            trySend(DownloadStatus.Done(File(path)))
                        }

                        override fun onDownLoadPause(key: String) {
                            trySend(DownloadStatus.Cancel)
                        }

                        override fun onUpdate(
                            key: String,
                            progress: Int,
                            read: Long,
                            count: Long,
                            done: Boolean
                        ) {
                            trySend(DownloadStatus.Downloading(count.toInt(), progress))
                        }
                    }
                )
            }

            awaitClose {
                downLoadManager.cancel(downloadTag)
                scope?.cancel()
            }
        }
    }

    override fun cancel() {
        downLoadManager.cancel(downloadTag)
        scope?.cancel()
    }

    override fun release() {
        downLoadManager.cancel(downloadTag)
        scope?.cancel()
    }
}
