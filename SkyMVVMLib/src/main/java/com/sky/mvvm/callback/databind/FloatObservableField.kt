package com.sky.mvvm.callback.databind

import androidx.databinding.ObservableField

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/2/23 10:10}</p>
 * <p>{@code description: 自定义的 Float 类型 ObservableField  提供了默认值，避免取值的时候还要判空}</p>
 */
class FloatObservableField(value: Float = 0f) : ObservableField<Float>(value) {
    override fun get(): Float {
        return super.get()!!
    }

}