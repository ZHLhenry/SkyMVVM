package com.sky.mvvm.ext.download

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/2/23 10:36}</p>
 * <p>{@code description: 注入下载}</p>
 */
@Module
@InstallIn(SingletonComponent::class)
object DownLoadManagerModule {
    @Singleton
    @Provides
    fun provideDownLoadManager(): DownLoadManager {
        return DownLoadManager()
    }

}