package com.sky.mvvm.ext.util

import android.util.Log

const val TAG = "SkyMVVM"

var skyMVVMLog = true

private enum class LEVEL {
    V, D, I, W, E
}

fun String.logv(tag: String = TAG) =
    log(LEVEL.V, tag, this)

fun String.logd(tag: String = TAG) =
    log(LEVEL.D, tag, this)

fun String.logi(tag: String = TAG) =
    log(LEVEL.I, tag, this)

fun String.logw(tag: String = TAG) =
    log(LEVEL.W, tag, this)

fun String.loge(tag: String = TAG) =
    log(LEVEL.E, tag, this)

private fun log(level: LEVEL, tag: String, message: String) {
    if (!skyMVVMLog) return
    when (level) {
        LEVEL.V -> Log.v(tag, message)
        LEVEL.D -> Log.d(tag, message)
        LEVEL.I -> Log.i(tag, message)
        LEVEL.W -> Log.w(tag, message)
        LEVEL.E -> Log.e(tag, message)
    }
}