package com.sky.mvvm.network.log
import com.sky.mvvm.SkyMVVMLib
import com.sky.mvvm.SkyMVVMLib.UninitializedException
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
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
    fun build(): Interceptor {
        SkyMVVMLib.requireInit()
        if (SkyMVVMLib.getConfig()?.okHttpLogLibEnabled == false) {
            throw UninitializedException(
                "Please add the \"enableOkHttpLogLib(true)\" attribute in the SkyMVVMLibConfig configuration."
            )
        }
        init()
        return SkyMVVMLib.getConfig()?.okHttpLogConfig!!
    }
}