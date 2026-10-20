package com.sky.mvvm.ext.download

import android.os.Looper
import com.sky.mvvm.ext.util.logI
import com.sky.mvvm.util.HttpsCerUtils
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @Class: DownLoadManager
 * @Author: Henry
 * @Date: 2025/2/23 10:35
 * @Description: 文件下载管理器，支持断点续传、暂停、取消下载
 */

@Singleton
class DownLoadManager @Inject constructor() {
    /** 忽略https模式 */
    private val retrofitBuilderHttps = Retrofit.Builder()
        .baseUrl("https://www.baidu.com")
        .client(
            HttpsCerUtils.trustAllCertificateClient.newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS).build()
        ).build()

    /** 正常http模式 */
    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl("https://www.baidu.com")
        .client(
            OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS).build()
        ).build()

    /**
     *开始下载
     * @param tag String 标识
     * @param url String  下载的url
     * @param savePath String 保存的路径
     * @param saveName String 保存的名字
     * @param reDownload Boolean 如果文件已存在是否需要重新下载 默认不需要重新下载
     * @param whetherHttps Boolean 默认不开启忽略https模式
     * @param loadListener OnDownLoadListener
     */
    suspend fun downLoad(
        tag: String,
        url: String,
        savePath: String,
        saveName: String,
        reDownload: Boolean = false,
        whetherHttps: Boolean = false,
        loadListener: OnDownLoadListener
    ) {
        withContext(Dispatchers.IO) {
            doDownLoad(tag, url, savePath, saveName, reDownload, whetherHttps, loadListener, this)
        }
    }

    /**
     * 取消下载
     * @param key String 取消的标识
     */
    fun cancel(key: String) {
        if (DownLoadPool.isFinished(key)) {
            //任务已成功或失败结束，不再回调取消
            DownLoadPool.remove(key)
            return
        }
        val path = DownLoadPool.getPathFromKey(key)
        if (path != null) {
            val file = File(path)
            if (file.exists()) {
                file.delete()
            }
        }
        val scope = DownLoadPool.getScopeFromKey(key)
        if (scope != null && scope.isActive) {
            //下载进行中：标记取消，由下载协程的取消出口统一回调onDownLoadCancel
            DownLoadPool.markCancelled(key)
        } else {
            //没有活跃任务时直接回调取消状态
            DownLoadPool.getListenerFromKey(key)?.onDownLoadCancel(key)
        }
        DownLoadPool.remove(key)
    }

    /**
     * 暂停下载
     * @param key String 暂停的标识
     */
    fun pause(key: String) {
        val listener = DownLoadPool.getListenerFromKey(key)
        listener?.onDownLoadPause(key)
        DownLoadPool.pause(key)
    }

    /**
     * 取消所有下载
     */
    fun doDownLoadCancelAll() {
        DownLoadPool.getListenerMap().forEach {
            cancel(it.key)
        }
    }

    /**
     * 暂停所有下载
     */
    fun doDownLoadPauseAll() {
        DownLoadPool.getListenerMap().forEach {
            pause(it.key)
        }
    }

    /**
     *下载
     * @param tag String 标识
     * @param url String  下载的url
     * @param savePath String 保存的路径
     * @param saveName String 保存的名字
     * @param reDownload Boolean 如果文件已存在是否需要重新下载 默认不需要重新下载
     * @param whetherHttps Boolean 是否是https模式
     * @param loadListener OnDownLoadListener
     * @param coroutineScope CoroutineScope 上下文
     */
    private suspend fun doDownLoad(
        tag: String,
        url: String,
        savePath: String,
        saveName: String,
        reDownload: Boolean,
        whetherHttps: Boolean,
        loadListener: OnDownLoadListener,
        coroutineScope: CoroutineScope
    ) {
        //判断是否已经在队列中
        val scope = DownLoadPool.getScopeFromKey(tag)
        if (scope != null && scope.isActive) {
            "已经在队列中".logI()
            return
        } else if (scope != null && !scope.isActive) {
            "key $tag 已经在队列中 但是已经不再活跃 remove".logI()
            DownLoadPool.removeExitSp(tag)
        }
        //清理上一次任务遗留的取消/结束标记
        DownLoadPool.resetState(tag)

        if (saveName.isEmpty()) {
            withContext(Dispatchers.Main) {
                loadListener.onDownLoadFailure(tag, Throwable("save name is Empty"))
            }
            return
        }

        if (Looper.getMainLooper().thread == Thread.currentThread()) {
            withContext(Dispatchers.Main) {
                loadListener.onDownLoadFailure(tag, Throwable("current thread is in main thread"))
            }
            return
        }

        val file = File("$savePath/$saveName")
        val currentLength = if (!file.exists()) {
            0L
        } else {
            ShareDownLoadUtil.getLong(tag, 0)
        }
        if (file.exists() && currentLength == 0L && !reDownload) {
            //文件已存在了
            loadListener.onDownLoadSuccess(tag, file.path, file.length())
            return
        }
        "startDownLoad current $currentLength".logI()

        try {
            //添加到pool
            DownLoadPool.add(tag, coroutineScope)
            DownLoadPool.add(tag, "$savePath/$saveName")
            DownLoadPool.add(tag, loadListener)

            withContext(Dispatchers.Main) {
                loadListener.onDownLoadPrepare(key = tag)
            }
            val response = if (whetherHttps) {
                retrofitBuilderHttps.create(DownLoadService::class.java)
                    .downloadFile("bytes=$currentLength-", url)
            } else {
                retrofitBuilder.create(DownLoadService::class.java)
                    .downloadFile("bytes=$currentLength-", url)
            }
            val responseBody = response.body()
            if (responseBody == null) {
                "responseBody is null".logI()
                withContext(Dispatchers.Main) {
                    loadListener.onDownLoadFailure(
                        key = tag,
                        throwable = Throwable("responseBody is null please check download url")
                    )
                }
                DownLoadPool.remove(tag)
                return
            }
            //按响应码校准断点位置：只有206才表示服务器支持Range续传
            val startLength = when (response.code()) {
                206 -> currentLength
                200 -> {
                    if (currentLength > 0L) {
                        //服务器忽略Range返回全量，删除旧文件后从头下载，避免文件损坏
                        File("$savePath/$saveName").delete()
                        ShareDownLoadUtil.remove(tag)
                    }
                    0L
                }
                else -> {
                    responseBody.close()
                    withContext(Dispatchers.Main) {
                        loadListener.onDownLoadFailure(
                            key = tag,
                            throwable = Throwable("download failed, http code ${response.code()}")
                        )
                    }
                    DownLoadPool.remove(tag)
                    return
                }
            }
            FileTool.downToFile(
                tag,
                savePath,
                saveName,
                startLength,
                responseBody,
                loadListener
            )
        } catch (throwable: Throwable) {
            if (throwable is CancellationException) {
                if (!DownLoadPool.isFinished(tag) && DownLoadPool.consumeCancelled(tag)) {
                    //主动取消触发的协程取消，在不可取消的主线程上下文中回调
                    withContext(NonCancellable + Dispatchers.Main) {
                        loadListener.onDownLoadCancel(tag)
                    }
                }
                //暂停或取消导致的协程异常，不作为下载失败回调
                throw throwable
            }
            withContext(Dispatchers.Main) {
                loadListener.onDownLoadFailure(key = tag, throwable = throwable)
            }
            DownLoadPool.remove(tag)
        }
    }
}