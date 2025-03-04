package com.sky.mvvm.sample.base

import android.app.Application
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.interceptor.BlacklistTagsFilterInterceptor
import com.elvishew.xlog.printer.AndroidPrinter
import com.elvishew.xlog.printer.Printer
import com.hjq.toast.Toaster
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.sky.multistatelayout.SkyMultiStateLayout
import com.sky.mvvm.base.BaseApplication
import com.sky.mvvm.core.common.BuildConfig
import com.sky.mvvm.core.common.R
import dagger.hilt.android.HiltAndroidApp

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/2/21 14:20}</p>
 * <p>{@code description: 文件描述}</p>
 */
@HiltAndroidApp
class SkyMvvmApplication : BaseApplication() {
    companion object {
        lateinit var mApplication: Application
    }

    override fun onCreate() {
        super.onCreate()
        mApplication = this
        Toaster.init(mApplication)
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.black, R.color.white) // 全局设置主题颜色
            ClassicsHeader(context) //.setTimeFormat(DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        }
        // 设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ -> // 指定为经典Footer，默认是 BallPulseFooter
            ClassicsFooter(context).setDrawableSize(20f)
        }
        SkyMultiStateLayout.init()
            .setLoadingView(layoutId = R.layout.state_view_loading)
            .setEmptyView(layoutId = R.layout.state_view_empty, hintTextId = R.id.tvEmpty)
            .setErrorView(
                layoutId = R.layout.state_view_error,
                hintTextId = R.id.btnError,
                clickViewIds = intArrayOf(R.id.btnError)
            )
            .setNoNetworkView(
                layoutId = R.layout.state_view_no_network,
                hintTextId = R.id.btnNoNetwork,
                clickViewIds = intArrayOf(R.id.btnNoNetwork)
            )

        val config = LogConfiguration.Builder()
            .logLevel(
                if (BuildConfig.DEBUG) LogLevel.ALL
                else LogLevel.NONE
            )
            .enableThreadInfo() // 允许打印线程信息，默认禁止
            .enableStackTrace(2) // 允许打印深度为 2 的调用栈信息，默认禁止
            .enableBorder() // 允许打印日志边框，默认禁止
            .addInterceptor(
                BlacklistTagsFilterInterceptor( // 添加黑名单 TAG 过滤器
                    "blacklist1", "blacklist2", "blacklist3"
                )
            )
            .build()
        val androidPrinter: Printer = AndroidPrinter(true)
        XLog.init(
            config,
            androidPrinter
        )
    }
}