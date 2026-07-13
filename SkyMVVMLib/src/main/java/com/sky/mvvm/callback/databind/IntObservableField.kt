package com.sky.mvvm.callback.databind

import androidx.databinding.ObservableField

/**
 * @Class: IntObservableField
 * @Author: Henry
 * @Date: 2025/2/23 10:10
 * @Description: Int类型的DataBinding可观察字段，重写get避免空值
 */

class IntObservableField(value: Int = 0) : ObservableField<Int>(value) {
    override fun get(): Int {
        return super.get()!!
    }
}