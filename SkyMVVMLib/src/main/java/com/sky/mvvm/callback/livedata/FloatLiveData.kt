package com.sky.mvvm.callback.livedata

import androidx.lifecycle.MutableLiveData

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/2/23 10:14}</p>
 * <p>{@code description: 自定义的Float类型 MutableLiveData 提供了默认值，避免取值的时候还要判空}</p>
 */
class FloatLiveData(value: Float = 0f) : MutableLiveData<Float>(value) {
    override fun getValue(): Float {
        return super.getValue()!!
    }
}