package com.sky.mvvm.ext.download

import android.os.SystemClock
import com.sky.mvvm.base.BaseApplication.Companion.app
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File
import java.io.RandomAccessFile
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/**
 * @Class: FileTool
 * @Author: Henry
 * @Date: 2025/2/23 10:38
 * @Description: 文件下载工具类，处理文件写入、目录创建、字节格式化等
 */

object FileTool {

    //定义GB的计算常量
    private const val GB = 1024 * 1024 * 1024

    //定义MB的计算常量
    private const val MB = 1024 * 1024

    //定义KB的计算常量
    private const val KB = 1024

    //下载缓冲区大小
    private const val BUFFER_SIZE = 16 * 1024

    //断点记录的最小写入间隔（毫秒），避免频繁写SharedPreferences
    private const val PROGRESS_SAVE_INTERVAL = 500L

    /**
     * 下载文件到本地
     * @param key String 下载标识，用于断点记录与任务池管理
     * @param savePath String 文件保存目录
     * @param saveName String 文件保存名称
     * @param currentLength Long 已下载的字节数，用于断点续传
     * @param responseBody ResponseBody 下载响应体
     * @param loadListener OnDownLoadListener 下载结果回调
     */
    suspend fun downToFile(
        key: String,
        savePath: String,
        saveName: String,
        currentLength: Long,
        responseBody: ResponseBody,
        loadListener: OnDownLoadListener
    ) {
        try {
            val filePath = withContext(Dispatchers.IO) {
                getFilePath(savePath, saveName)
            }
            if (filePath == null) {
                DownLoadPool.markFinished(key)
                withContext(Dispatchers.Main) {
                    loadListener.onDownLoadFailure(key, Throwable("mkdirs file [$savePath] error"))
                }
                DownLoadPool.remove(key)
                return
            }
            //保存到文件
            saveToFile(currentLength, responseBody, filePath, key, loadListener)
        } catch (throwable: Throwable) {
            if (throwable is CancellationException) {
                //暂停或取消导致的协程异常，不作为下载失败回调
                throw throwable
            }
            DownLoadPool.markFinished(key)
            withContext(Dispatchers.Main) {
                loadListener.onDownLoadFailure(key, throwable)
            }
            DownLoadPool.remove(key)
        }
    }

    /**
     * 将响应体流式写入文件，支持断点续传
     * @param currentLength Long 已下载的字节数
     * @param responseBody ResponseBody 下载响应体
     * @param filePath String 文件保存路径
     * @param key String 下载标识
     * @param loadListener OnDownLoadListener 下载进度与结果回调
     */
    suspend fun saveToFile(
        currentLength: Long,
        responseBody: ResponseBody,
        filePath: String,
        key: String,
        loadListener: OnDownLoadListener
    ) = withContext(Dispatchers.IO) {
        val totalLength = getFileLength(currentLength, responseBody)
        if (totalLength < currentLength) {
            //响应头长度无效（如chunked）或本地断点记录异常，无法安全续传
            DownLoadPool.markFinished(key)
            withContext(Dispatchers.Main) {
                loadListener.onDownLoadFailure(
                    key,
                    Throwable("invalid contentLength [${responseBody.contentLength()}]")
                )
            }
            DownLoadPool.remove(key)
            return@withContext
        }
        if (totalLength == currentLength) {
            //文件已完整，直接回调成功
            DownLoadPool.markFinished(key)
            withContext(Dispatchers.Main) {
                loadListener.onDownLoadSuccess(key, filePath, totalLength)
            }
            DownLoadPool.remove(key)
            return@withContext
        }

        val buffer = ByteArray(BUFFER_SIZE)
        var lastProgress = 0
        var lastSaveTime = 0L
        var currentSaveLength = currentLength //当前的长度
        try {
            responseBody.use { body ->
                RandomAccessFile(File(filePath), "rw").use { accessFile ->
                    accessFile.seek(currentLength)
                    body.byteStream().use { inputStream ->
                        while (true) {
                            val len = inputStream.read(buffer)
                            if (len == -1) {
                                break
                            }
                            accessFile.write(buffer, 0, len)
                            currentSaveLength += len

                            val progress =
                                (currentSaveLength.toFloat() / totalLength * 100).toInt() // 计算百分比
                            if (lastProgress != progress) {
                                lastProgress = progress
                                //节流记录断点位置
                                val now = SystemClock.elapsedRealtime()
                                if (now - lastSaveTime >= PROGRESS_SAVE_INTERVAL) {
                                    lastSaveTime = now
                                    ShareDownLoadUtil.putLong(key, currentSaveLength)
                                }
                                withContext(Dispatchers.Main) {
                                    loadListener.onUpdate(
                                        key,
                                        progress,
                                        currentSaveLength,
                                        totalLength,
                                        currentSaveLength == totalLength
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } finally {
            //暂停或异常退出时保留断点，已完成则由remove统一清理
            if (currentSaveLength != totalLength) {
                ShareDownLoadUtil.putLong(key, currentSaveLength)
            }
        }

        DownLoadPool.markFinished(key)
        if (currentSaveLength == totalLength) {
            withContext(Dispatchers.Main) {
                loadListener.onDownLoadSuccess(key, filePath, totalLength)
            }
        } else {
            withContext(Dispatchers.Main) {
                loadListener.onDownLoadFailure(
                    key,
                    Throwable("download incomplete [$currentSaveLength/$totalLength]")
                )
            }
        }
        DownLoadPool.remove(key)
    }

    /**
     * 计算文件总长度
     * @param currentLength Long 已下载的字节数
     * @param responseBody ResponseBody 下载响应体
     * @return Long 文件总长度，响应头未携带有效长度时返回-1
     */
    fun getFileLength(
        currentLength: Long,
        responseBody: ResponseBody
    ): Long {
        val contentLength = responseBody.contentLength()
        return if (contentLength < 0) contentLength else currentLength + contentLength
    }

    /**
     * 获取下载地址
     * @param savePath String 文件保存目录
     * @param saveName String 文件保存名称
     * @return String? 文件完整路径，目录创建失败时返回null
     */
    fun getFilePath(savePath: String, saveName: String): String? {
        if (!createFile(savePath)) {
            return null
        }
        return "$savePath/$saveName"
    }

    /**
     * 创建文件夹
     * @param downLoadPath String 目录路径
     * @return Boolean 目录是否存在或创建成功
     */
    fun createFile(downLoadPath: String): Boolean {
        val file = File(downLoadPath)
        if (!file.exists()) {
            return file.mkdirs()
        }
        return true
    }

    /**
     * 格式化文件大小
     * @param bytes Long 文件字节数
     * @return String 带单位的可读大小，如1.5GB
     */
    fun bytes2kb(bytes: Long): String {
        val format = DecimalFormat("###.0", DecimalFormatSymbols(Locale.US))
        return when {
            bytes / GB >= 1 -> {
                format.format(bytes.toDouble() / GB) + "GB"
            }
            bytes / MB >= 1 -> {
                format.format(bytes.toDouble() / MB) + "MB"
            }
            bytes / KB >= 1 -> {
                format.format(bytes.toDouble() / KB) + "KB"
            }
            else -> {
                "${bytes}B"
            }
        }
    }

    /**
     * 获取App文件的根路径
     * @return String 应用外部私有文件目录，不可用时返回空字符串
     */
    fun getBasePath(): String {
        return app.getExternalFilesDir(null)?.absolutePath ?: ""
    }
}