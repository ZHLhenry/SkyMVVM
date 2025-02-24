package com.sky.mvvm.network.log

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/2/23 10:24}</p>
 * <p>{@code description: 文件描述}</p>
 */
object LogManager {
    private var logProxy: LogProxy? = null

    fun logProxy(logProxy: LogProxy) {
        LogManager.logProxy = logProxy
    }

    fun e(tag: String, msg: String) {
        logProxy?.e(tag, msg)
    }

    fun w(tag: String, msg: String) {
        logProxy?.w(tag, msg)
    }

    fun i(tag: String, msg: String) {
        logProxy?.i(tag, msg)
    }

    fun d(tag: String, msg: String) {
        logProxy?.d(tag, msg)
    }
}