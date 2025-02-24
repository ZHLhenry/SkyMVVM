package com.sky.mvvm.callback.databind
import androidx.databinding.ObservableField
/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/2/23 10:08}</p>
 * <p>{@code description: 自定义的Boolean类型ObservableField 提供了默认值，避免取值的时候还要判空}</p>
 */
class BooleanObservableField(value: Boolean = false) : ObservableField<Boolean>(value) {
    override fun get(): Boolean {
        return super.get()!!
    }

}