package com.sky.mvvm.core.common

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/3/3 15:33}</p>
 * <p>{@code description: 文件描述}</p>
 */
object AppConfig {
    const val IS_PROD: Boolean = BuildConfig.FLAVOR == "prod"

    //http请求地址
    const val BASE_URL = "https://wanandroid.com/"
    const val CACHE_ID = "SkyMVVM_Cache"
    const val DEFAULT_TIMEOUT = 20L
}