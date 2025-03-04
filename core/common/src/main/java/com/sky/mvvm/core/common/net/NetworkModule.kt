package com.sky.mvvm.core.common.net

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.sky.mvvm.base.BaseApplication.Companion.app
import com.sky.mvvm.core.common.AppConfig
import com.sky.mvvm.core.common.net.interceptor.HeadsInterceptor
import com.sky.mvvm.core.common.net.interceptor.TokenOutInterceptor
import com.sky.mvvm.network.BaseNetworkApi
import com.sky.mvvm.network.interceptor.CacheInterceptor
import com.sky.mvvm.network.log.AndroidLoggingInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/3/3 11:14}</p>
 * <p>{@code description: 文件描述}</p>
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule : BaseNetworkApi() {
    override fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        builder.apply {
            /** 超时时间 连接、读、写 */
            connectTimeout(AppConfig.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(AppConfig.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(AppConfig.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            /** 设置缓存配置 缓存最大10M */
            cache(Cache(File(app.cacheDir, AppConfig.CACHE_ID), 10 * 1024 * 1024))
            /** 添加缓存拦截器 可传入缓存天数，不传默认7天 */
            addInterceptor(CacheInterceptor(1))
            /** 添加公共heads 注意要设置在日志拦截器之前，不然Log中会不显示head信息 */
            addInterceptor(HeadsInterceptor())
            /** token过期拦截器 */
            addInterceptor(TokenOutInterceptor())
            /** Checker拦截器 **/
            if (!AppConfig.IS_PROD) {
                addInterceptor(ChuckerInterceptor.Builder(app).build())
            }
            /** 日志拦截器 */
            addInterceptor(AndroidLoggingInterceptor.build(isDebug = !AppConfig.IS_PROD))
        }
        return builder
    }

    override fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder {
        return builder.apply {
            addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .addLast(KotlinJsonAdapterFactory())
                        .build()
                )
            )
        }
    }

    @Provides
    fun provideApiService(): ApiService {
        return NetworkModule.getApi(ApiService::class.java, AppConfig.BASE_URL, false)
    }

}