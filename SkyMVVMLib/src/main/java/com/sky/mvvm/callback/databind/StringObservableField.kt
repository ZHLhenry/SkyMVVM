package com.sky.mvvm.callback.databind

import androidx.databinding.ObservableField

/**
 * @Class: StringObservableField
 * @Author: Henry
 * @Date: 2025/2/23 10:11
 * @Description: String类型的DataBinding可观察字段，重写get避免空值
 */

open class StringObservableField(value: String = "") : ObservableField<String>(value) {
    override fun get(): String {
        return super.get()!!
    }
}