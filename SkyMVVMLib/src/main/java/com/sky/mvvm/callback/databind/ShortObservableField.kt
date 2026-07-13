package com.sky.mvvm.callback.databind

import androidx.databinding.ObservableField

/**
 * @Class: ShortObservableField
 * @Author: Henry
 * @Date: 2025/2/23 10:11
 * @Description: Short类型的DataBinding可观察字段，重写get避免空值
 */

class ShortObservableField(value: Short = 0) : ObservableField<Short>(value) {
    override fun get(): Short {
        return super.get()!!
    }
}