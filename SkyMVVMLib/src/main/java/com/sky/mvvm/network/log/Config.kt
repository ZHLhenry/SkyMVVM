package com.sky.mvvm.network.log
import android.util.Log
/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/2/23 10:23}</p>
 * <p>{@code description: 在使用日志拦截器之前,必须要先实现 LogProxy ，否则无法打印网络请求的 request 、response,所以，先调用这个方法}</p>
 */
fun init() {
    LogManager.logProxy(object : LogProxy {
        override fun e(tag: String, msg: String) {
            Log.e(tag, msg)
        }

        override fun w(tag: String, msg: String) {
            Log.w(tag, msg)
        }

        override fun i(tag: String, msg: String) {
            Log.i(tag, msg)
        }

        override fun d(tag: String, msg: String) {
            Log.d(tag, msg)
        }
    })
}