package com.sky.mvvm.callback.livedata

import androidx.lifecycle.MutableLiveData

/**
 * @Class: ShortLiveData
 * @Author: Henry
 * @Date: 2025/2/23 10:15
 * @Description: Short类型的LiveData封装，提供默认值避免空指针
 */

class ShortLiveData : MutableLiveData<Short>() {
    override fun getValue(): Short {
        return super.getValue() ?: 0
    }
}