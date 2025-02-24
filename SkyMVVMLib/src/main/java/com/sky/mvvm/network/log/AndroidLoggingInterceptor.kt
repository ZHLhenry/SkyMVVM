package com.sky.mvvm.network.log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/2/23 10:22}</p>
 * <p>{@code description: 文件描述}</p>
 */
@InstallIn(SingletonComponent::class)
@Module
object AndroidLoggingInterceptor {
    @Singleton
    @Provides
    fun build(
        isDebug: Boolean = true,
        hideVerticalLine: Boolean = false,
        requestTag: String = "Request",
        responseTag: String = "Response"
    ): LoggingInterceptor {
        init()
        return if (hideVerticalLine) {
            LoggingInterceptor.Builder()
                .loggable(isDebug) // TODO: 发布到生产环境需要改成false
                .androidPlatform()
                .request()
                .requestTag(requestTag)
                .response()
                .responseTag(responseTag)
                .hideVerticalLine()// 隐藏竖线边框
                .build()
        } else {
            LoggingInterceptor.Builder()
                .loggable(isDebug) // TODO: 发布到生产环境需要改成false
                .androidPlatform()
                .request()
                .requestTag(requestTag)
                .response()
                .responseTag(responseTag)
                .build()
        }
    }
}