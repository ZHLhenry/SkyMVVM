package com.sky.mvvm.network.log

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/2/23 10:24}</p>
 * <p>{@code description: 文件描述}</p>
 */
interface LogProxy {
    fun e(tag: String, msg: String)

    fun w(tag: String, msg: String)

    fun i(tag: String, msg: String)

    fun d(tag: String, msg: String)
}