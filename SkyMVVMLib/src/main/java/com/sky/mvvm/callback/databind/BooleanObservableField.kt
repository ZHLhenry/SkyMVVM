package com.sky.mvvm.callback.databind
import androidx.databinding.ObservableField

/**
 * @Class: BooleanObservableField
 * @Author: Henry
 * @Date: 2025/2/23 10:08
 * @Description: Boolean类型的DataBinding可观察字段，重写get避免空值
 */

class BooleanObservableField(value: Boolean = false) : ObservableField<Boolean>(value) {
    override fun get(): Boolean {
        return super.get()!!
    }

}