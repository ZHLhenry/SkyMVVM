package com.sky.mvvm.ext.lifecycle
import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.sky.mvvm.ext.util.logI

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/2/23 08:59}</p>
 * <p>{@code description: 文件描述}</p>
 */
class KtxLifeCycleCallBack : Application.ActivityLifecycleCallbacks {

    private val TAG = javaClass.simpleName

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        KtxActivityManger.pushActivity(activity)
        "onActivityCreated : ${activity.localClassName}".logI(TAG)
    }

    override fun onActivityStarted(activity: Activity) {
        "onActivityStarted : ${activity.localClassName}".logI(TAG)
    }

    override fun onActivityResumed(activity: Activity) {
        "onActivityResumed : ${activity.localClassName}".logI(TAG)
    }

    override fun onActivityPaused(activity: Activity) {
        "onActivityPaused : ${activity.localClassName}".logI(TAG)
    }


    override fun onActivityDestroyed(activity: Activity) {
        "onActivityDestroyed : ${activity.localClassName}".logI(TAG)
        KtxActivityManger.popActivity(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityStopped(activity: Activity) {
        "onActivityStopped : ${activity.localClassName}".logI(TAG)
    }


}