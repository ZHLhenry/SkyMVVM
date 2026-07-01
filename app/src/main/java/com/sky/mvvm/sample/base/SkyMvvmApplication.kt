package com.sky.mvvm.sample.base

import android.app.Application
import android.content.Intent
import com.hjq.toast.Toaster
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.sky.multistatelayout.SkyMultiStateLayout
import com.sky.mvvm.SkyMVVMLib
import com.sky.mvvm.SkyMVVMLibConfig
import com.sky.mvvm.base.BaseApplication
import com.sky.mvvm.core.common.ErrorCode.ERROR_200
import com.sky.mvvm.core.common.R
import com.sky.mvvm.ext.util.logD
import com.sky.mvvm.ext.util.logE
import com.sky.mvvm.ext.util.logI
import com.sky.mvvm.flow.SkyFlow
import com.sky.mvvm.flow.SkyFlowEventData
import com.sky.mvvm.sample.feature.other.ui.LoginActivity
import com.sky.mvvm.util.ActivityMessenger
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/2/21 14:20}</p>
 * <p>{@code description: 文件描述}</p>
 */
@HiltAndroidApp
class SkyMvvmApplication : BaseApplication() {
    private val applicationScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

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

        SkyMVVMLib.init(SkyMVVMLibConfig.Builder(mApplication)
            .enableXLog(enableXLogLib = true)
            .enableSkyFlow(enableSkyFlowLib = true)
            .enableOkHttpLogLib(enableOkHttpLogLib = true)
            .build())

        /**
         * 模拟flow接受事件消息(登录过期拦截并跳转登录页面)
         */
        SkyFlow.with<SkyFlowEventData>(ERROR_200.toString())
            .register(scope = applicationScope, action = {
                val flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                ActivityMessenger.startActivity<LoginActivity>(this,flags)
            })
    }

    override fun onTerminate() {
        super.onTerminate()
        SkyFlow.clearUnusedFlow()
        applicationScope.cancel()
    }
}