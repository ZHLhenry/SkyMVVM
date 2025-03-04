package com.sky.mvvm.core.common.net.interceptor

import android.util.Log
import com.hjq.gson.factory.GsonFactory
import com.sky.mvvm.core.common.net.ApiResponse
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/3/3 11:21}</p>
 * <p>{@code description: 文件描述}</p>
 */
class TokenOutInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        return if (response.body != null && response.body!!.contentType() != null) {
            val mediaType = response.body!!.contentType()
            val string = response.body!!.string()
            val responseBody = string.toResponseBody(mediaType)
            val apiResponse = GsonFactory.getSingletonGson().fromJson(string, ApiResponse::class.java)
            //判断逻辑 模拟一下
            if (apiResponse.errorCode == 99999) {
                Log.i("TokenOutInterceptor","errorCode被拦截了")
            }
            response.newBuilder().body(responseBody).build()
        } else {
            response
        }
    }
}