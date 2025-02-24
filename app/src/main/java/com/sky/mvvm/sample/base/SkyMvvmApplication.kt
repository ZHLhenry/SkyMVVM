package com.sky.mvvm.sample.base

import android.R
import android.app.Application
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator
import com.sky.mvvm.base.BaseApplication

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/2/21 14:20}</p>
 * <p>{@code description: 文件描述}</p>
 */
class SkyMvvmApplication : BaseApplication() {
    companion object {
        lateinit var instance: Application
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(DefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.black, R.color.white) // 全局设置主题颜色
            ClassicsHeader(context) //.setTimeFormat(DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        })
        // 设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ -> // 指定为经典Footer，默认是 BallPulseFooter
            ClassicsFooter(context).setDrawableSize(20f)
        }
    }
}