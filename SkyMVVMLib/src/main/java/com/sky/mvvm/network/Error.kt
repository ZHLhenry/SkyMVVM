package com.sky.mvvm.network

import android.content.Context
import androidx.annotation.StringRes
import com.sky.mvvm.R

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/2/23 10:32}</p>
 * <p>{@code description: 错误枚举类}</p>
 */
enum class Error(private val code: Int, @param:StringRes private val errResId: Int) {

    /**
     * 未知错误
     */
    UNKNOWN(1000, R.string.sky_mmvmlib_exception_unknown),

    /**
     * 解析错误
     */
    PARSE_ERROR(1001, R.string.sky_mmvmlib_exception_parse_error),

    /**
     * 网络错误
     */
    NETWORK_ERROR(1002, R.string.sky_mmvmlib_exception_network_error),

    /**
     * 证书出错
     */
    SSL_ERROR(1004, R.string.sky_mmvmlib_exception_ssl_error),

    /**
     * 连接超时
     */
    TIMEOUT_ERROR(1006, R.string.sky_mmvmlib_exception_timeout_error);

    fun getValue(context: Context): String {
        return context.getString(errResId)
    }

    fun getKey(): Int {
        return code
    }

}