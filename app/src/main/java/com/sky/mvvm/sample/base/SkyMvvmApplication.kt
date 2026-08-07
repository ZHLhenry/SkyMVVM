package com.sky.mvvm.sample.base

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.lifecycle.ProcessLifecycleOwner
import com.hjq.toast.Toaster
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.sky.multistatelayout.SkyMultiStateLayout
import com.sky.mvvm.SkyMVVMLib
import com.sky.mvvm.SkyMVVMLibConfig
import com.sky.mvvm.base.BaseApplication
import com.sky.mvvm.core.common.AppConfig
import com.sky.mvvm.core.common.ErrorCode.ERROR_200
import com.sky.mvvm.core.common.R
import com.sky.mvvm.ext.lifecycle.KtxAppLifeObserver
import com.sky.mvvm.flow.SkyFlow
import com.sky.mvvm.flow.SkyFlowEventData
import com.sky.mvvm.sample.BuildConfig
import com.sky.mvvm.sample.feature.other.ui.LoginActivity
import com.sky.mvvm.util.ActivityMessenger
import com.sky.widget.iconfont.SkyIconFontsLib
import com.sky.widget.tools.qrcode.SkyQRCode
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * <p>{@code className: SkyMvvmApplication}</p>
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
        // 必须最先设置，确保所有 Activity 创建前生效
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
        super.onCreate()
        mApplication = this
        // 由 app 模块注入 flavor 信息到 common 层
        AppConfig.IS_PROD = BuildConfig.FLAVOR == "prod"
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

        // 模拟flow接受事件消息(登录过期拦截并跳转登录页面)
        SkyFlow.with<SkyFlowEventData>(ERROR_200.toString())
            .register(scope = applicationScope, action = {
                val flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                ActivityMessenger.startActivity<LoginActivity>(this,flags)
            })

        // 初始化并注册 sky-iconfont 自定义图标库
        // JSON 文件为 iconfont.cn 导出的标准格式（含 css_prefix_text 和 glyphs 字段）
//        SkyIconFontsLib.initRegister(this, "fonts/testSky_iconfont.ttf")
        SkyIconFontsLib.initRegister(this)
        SkyQRCode.init(this)

        // 监听应用生命周期，进入后台时清理资源
        KtxAppLifeObserver.isForeground.observe(ProcessLifecycleOwner.get()) { isForeground ->
            if (!isForeground) {
                SkyFlow.clearUnusedFlow()
            }
        }
    }
}