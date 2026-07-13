package com.sky.mvvm.callback.databind

import androidx.databinding.ObservableField

/**
 * @Class: ByteObservableField
 * @Author: Henry
 * @Date: 2025/2/23 10:09
 * @Description: Byte类型的DataBinding可观察字段，重写get避免空值
 */

class ByteObservableField(value: Byte = 0) : ObservableField<Byte>(value) {
    override fun get(): Byte {
        return super.get()!!
    }
}