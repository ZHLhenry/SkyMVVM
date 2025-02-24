package com.sky.mvvm.callback.databind

import androidx.databinding.ObservableField

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/2/23 10:11}</p>
 * <p>{@code description: 文件描述}</p>
 */
open class StringObservableField(value: String = "") : ObservableField<String>(value) {
    override fun get(): String {
        return super.get()!!
    }
}