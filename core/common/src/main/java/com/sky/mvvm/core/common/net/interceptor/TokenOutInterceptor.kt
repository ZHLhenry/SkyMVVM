package com.sky.mvvm.core.common.net.interceptor

import com.hjq.gson.factory.GsonFactory
import com.sky.mvvm.core.common.ErrorCode.ERROR_200
import com.sky.mvvm.core.common.net.ApiResponse
import com.sky.mvvm.flow.SkyFlow
import com.sky.mvvm.flow.SkyFlowEvent
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
    @OptIn(DelicateCoroutinesApi::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        return if (response.body != null && response.body!!.contentType() != null) {
            val mediaType = response.body!!.contentType()
            val string = response.body!!.string()
            val responseBody = string.toResponseBody(mediaType)
            val apiResponse = GsonFactory.getSingletonGson().fromJson(string, ApiResponse::class.java)
            if (apiResponse.errorCode == ERROR_200) {
                GlobalScope.launch {
                    val skyFlowEvent = SkyFlowEvent(ERROR_200.toString(),"TokenOutInterceptor")
                    SkyFlow.with<SkyFlowEvent>(ERROR_200.toString()).post(skyFlowEvent)
                }
            }
            response.newBuilder().body(responseBody).build()
        } else {
            response
        }
    }
}