package com.sky.mvvm.network.state
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.sky.mvvm.network.AppException
import com.sky.mvvm.network.BaseResponse
import com.sky.mvvm.network.ExceptionHandle

/**
 * @Class: ResultState
 * @Author: Henry
 * @Date: 2025/2/23 10:28
 * @Description: 网络请求结果密封类，封装Loading、Success、Error三种状态
 */

sealed class ResultState<out T> {
    companion object {
        fun <T> onAppSuccess(data: T): ResultState<T> = Success(data)
        fun <T> onAppLoading(loadingMessage: String): ResultState<T> = Loading(loadingMessage)
        fun <T> onAppError(error: AppException): ResultState<T> = Error(error)
    }

    data class Loading(val loadingMessage: String) : ResultState<Nothing>()
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error(val error: AppException) : ResultState<Nothing>()
}

/**
 * 处理返回值
 * @param result 请求结果
 */
fun <T> MutableLiveData<ResultState<T>>.paresResult(context: Context,result: BaseResponse<T>) {
    value = when {
        result.isSucces() -> {
            ResultState.onAppSuccess(result.getResponseData())
        }

        else -> {
            ResultState.onAppError(AppException(context,result.getResponseCode(), result.getResponseMsg()))
        }
    }
}

/**
 * 不处理返回值 直接返回请求结果
 * @param result 请求结果
 */
fun <T> MutableLiveData<ResultState<T>>.paresResult(result: T) {
    value = ResultState.onAppSuccess(result)
}

/**
 * 异常转换异常处理
 */
fun <T> MutableLiveData<ResultState<T>>.paresException(context: Context, e: Throwable) {
    this.value = ResultState.onAppError(ExceptionHandle.handleException(context,e))
}