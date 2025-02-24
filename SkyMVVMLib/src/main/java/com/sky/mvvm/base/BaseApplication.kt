package com.sky.mvvm.base
import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.ProcessLifecycleOwner
import com.sky.mvvm.ext.lifecycle.KtxAppLifeObserver
import com.sky.mvvm.ext.lifecycle.KtxLifeCycleCallBack
import com.sky.mvvm.network.manager.NetworkStateReceive
/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/2/22 22:00}</p>
 * <p>{@code description: 文件描述}</p>
 */
@Suppress("DEPRECATION")
open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initAppConfig(this)
    }

    companion object {
        lateinit var app: Application
        private var mNetworkStateReceive: NetworkStateReceive? = null
        private var watchActivityLife = true
        private var watchAppLife = true

        /**
         * 初始化App配置
         */
        @JvmStatic
        fun initAppConfig(application: Application) {
            install(application)
        }

        private fun install(application: Application) {
            app = application
            mNetworkStateReceive = NetworkStateReceive()
            app.registerReceiver(
                mNetworkStateReceive,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
            if (watchActivityLife) application.registerActivityLifecycleCallbacks(
                KtxLifeCycleCallBack()
            )
            if (watchAppLife) ProcessLifecycleOwner.get().lifecycle.addObserver(KtxAppLifeObserver)
        }
    }
}