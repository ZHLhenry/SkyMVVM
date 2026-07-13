package com.sky.mvvm.callback.databind

import androidx.databinding.ObservableField

/**
 * @Class: DoubleObservableField
 * @Author: Henry
 * @Date: 2025/2/23 10:09
 * @Description: Double类型的DataBinding可观察字段，重写get避免空值
 */

class DoubleObservableField(value: Double = 0.0) : ObservableField<Double>(value) {

    override fun get(): Double {
        return super.get()!!
    }

}