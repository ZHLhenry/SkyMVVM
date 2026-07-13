package com.sky.mvvm

import android.annotation.SuppressLint
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.interceptor.BlacklistTagsFilterInterceptor
import com.elvishew.xlog.printer.AndroidPrinter
import com.elvishew.xlog.printer.Printer
import com.sky.mvvm.network.log.LoggingInterceptor

/**
 * @Class: SkyMVVMLib
 * @Author: Henry
 * @Date: 2025/6/30
 * @Description: SkyMVVM库入口，负责初始化日志、网络拦截器等核心配置
 */

object SkyMVVMLib {
    private const val TAG = "SkyMVVMLib"
    @SuppressLint("StaticFieldLeak")
    private var config: SkyMVVMLibConfig? = null

    /**
     * 是否已初始化
     */
    val isInitialized: Boolean
        get() = config != null

    /**
     * 初始化SkyMVVMLib
     * @param config 配置项
     */
    fun init(config: SkyMVVMLibConfig) {
        if (config.xLogLibEnabled) {
            val xLogConfig: LogConfiguration = config.xLogConfig ?: LogConfiguration.Builder()
                .logLevel(LogLevel.ALL)
                .disableBorder()
                .addInterceptor(BlacklistTagsFilterInterceptor())
                .build()
            val defaultPrinter: Printer = AndroidPrinter(true)
            val printers: Array<out Printer> = if (config.xLogPrinter.isNotEmpty()) config.xLogPrinter else arrayOf(defaultPrinter)
            XLog.init(xLogConfig, *printers)
        }
        if (config.okHttpLogLibEnabled && config.okHttpLogConfig == null) {
            config.okHttpLogConfig = LoggingInterceptor.Builder()
                .loggable(enableLog = true)
                .androidPlatform()
                .request()
                .requestTag(tag = "Request")
                .response()
                .responseTag(tag = "Response")
                .hideVerticalLine()
                .build()
        }
        this.config = config
    }

    /**
     * 获取当前配置
     */
    fun getConfig(): SkyMVVMLibConfig? = config

    /**
     * 检查库是否已初始化，未初始化则抛出异常
     * @throws UninitializedException 未初始化时抛出
     */
    internal fun requireInit() {
        if (!isInitialized) {
            throw UninitializedException(
                "Please first call SkyMVVMLib.init() in the Application to perform the initialization."
            )
        }
    }

    /**
     * SkyMVVMLib 未初始化异常
     */
    class UninitializedException(message: String) : RuntimeException(message)
}
