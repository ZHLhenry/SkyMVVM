@file:Suppress("DEPRECATION")

package com.sky.mvvm.ext.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.sky.mvvm.callback.livedata.BooleanLiveData

/**
 * @Class: KtxAppLifeObserver
 * @Author: Henry
 * @Date: 2025/2/23 08:58
 * @Description: 应用前后台生命周期观察者，监听App进入前台和进入后台
 */

object KtxAppLifeObserver : LifecycleObserver {

    var isForeground = BooleanLiveData()

    //在前台
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onForeground() {
        isForeground.value = true
    }

    //在后台
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onBackground() {
        isForeground.value = false
    }

}