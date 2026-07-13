package com.sky.mvvm.callback.livedata

import androidx.lifecycle.MutableLiveData

/**
 * @Class: ByteLiveData
 * @Author: Henry
 * @Date: 2025/2/23 10:13
 * @Description: Byte类型的LiveData封装，提供默认值避免空指针
 */

class ByteLiveData : MutableLiveData<Byte>() {
    override fun getValue(): Byte {
        return super.getValue() ?: 0
    }
}