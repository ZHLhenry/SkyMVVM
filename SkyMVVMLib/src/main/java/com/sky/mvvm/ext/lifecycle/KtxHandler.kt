@file:Suppress("DEPRECATION")

package com.sky.mvvm.ext.lifecycle

import android.os.Handler
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 * @Class: KtxHandler
 * @Author: Henry
 * @Date: 2025/2/23 08:59
 * @Description: 生命周期感知的Handler，随Lifecycle自动清理消息防止内存泄漏
 */

@Suppress("DEPRECATION")
class KtxHandler(lifecycleOwner: LifecycleOwner, callback: Callback) : Handler(callback),
    LifecycleObserver {

    private val mLifecycleOwner: LifecycleOwner = lifecycleOwner

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy() {
        removeCallbacksAndMessages(null)
        mLifecycleOwner.lifecycle.removeObserver(this)
    }
}