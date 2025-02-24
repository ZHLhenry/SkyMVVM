package com.sky.mvvm.callback.livedata

import androidx.lifecycle.MutableLiveData

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/2/23 10:12}</p>
 * <p>{@code description: 自定义的Boolean类型 MutableLiveData 提供了默认值，避免取值的时候还要判空}</p>
 */
class BooleanLiveData : MutableLiveData<Boolean>() {

    override fun getValue(): Boolean {
        return super.getValue() ?: false
    }
}