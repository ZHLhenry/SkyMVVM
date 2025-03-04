package com.sky.mvvm.core.common.net.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/3/3 11:18}</p>
 * <p>{@code description: 文件描述}</p>
 */
class HeadsInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.addHeader("token", "123456789").build()
        return chain.proceed(builder.build())
    }
}