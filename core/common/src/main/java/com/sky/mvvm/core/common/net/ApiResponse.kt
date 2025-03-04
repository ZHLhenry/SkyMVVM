package com.sky.mvvm.core.common.net

import com.sky.mvvm.network.BaseResponse
import com.squareup.moshi.JsonClass

/**
 * <p>{@code className: ApiResponse}</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2024/6/4 16:52}</p>
 * <p>{@code description: 文件描述}</p>
 */
@JsonClass(generateAdapter = true)
data class ApiResponse<T>(val errorCode: Int, val errorMsg: String, val data: T) :
    BaseResponse<T>() {

    override fun isSucces() = errorCode == 0

    override fun getResponseCode() = errorCode

    override fun getResponseData() = data

    override fun getResponseMsg() = errorMsg

}
