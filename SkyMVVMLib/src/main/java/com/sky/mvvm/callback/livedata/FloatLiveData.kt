package com.sky.mvvm.callback.livedata

import androidx.lifecycle.MutableLiveData

/**
 * @Class: FloatLiveData
 * @Author: Henry
 * @Date: 2025/2/23 10:14
 * @Description: Float类型的LiveData封装，提供默认值避免空指针
 */

class FloatLiveData(value: Float = 0f) : MutableLiveData<Float>(value) {
    override fun getValue(): Float {
        return super.getValue()!!
    }
}