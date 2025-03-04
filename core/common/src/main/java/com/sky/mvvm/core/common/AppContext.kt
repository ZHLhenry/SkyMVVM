package com.sky.mvvm.core.common

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object AppContext {
    private lateinit var context: Context
    fun init(context: Context) {
        AppContext.context = context
    }
}