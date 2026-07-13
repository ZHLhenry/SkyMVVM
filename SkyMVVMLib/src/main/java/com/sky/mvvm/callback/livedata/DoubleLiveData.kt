package com.sky.mvvm.callback.livedata

import androidx.lifecycle.MutableLiveData

/**
 * @Class: DoubleLiveData
 * @Author: Henry
 * @Date: 2025/2/23 10:13
 * @Description: Double类型的LiveData封装，提供默认值避免空指针
 */

class DoubleLiveData : MutableLiveData<Double>() {
    override fun getValue(): Double {
        return super.getValue() ?: 0.0
    }
}