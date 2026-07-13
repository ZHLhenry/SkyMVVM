package com.sky.mvvm.callback.databind

import androidx.databinding.ObservableField

/**
 * @Class: FloatObservableField
 * @Author: Henry
 * @Date: 2025/2/23 10:10
 * @Description: Float类型的DataBinding可观察字段，重写get避免空值
 */

class FloatObservableField(value: Float = 0f) : ObservableField<Float>(value) {
    override fun get(): Float {
        return super.get()!!
    }

}