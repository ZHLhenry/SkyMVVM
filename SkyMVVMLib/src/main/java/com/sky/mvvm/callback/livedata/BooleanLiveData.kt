package com.sky.mvvm.callback.livedata

import androidx.lifecycle.MutableLiveData

/**
 * @Class: BooleanLiveData
 * @Author: Henry
 * @Date: 2025/2/23 10:12
 * @Description: Boolean类型的LiveData封装，提供默认值false避免空指针
 */

class BooleanLiveData : MutableLiveData<Boolean>() {

    override fun getValue(): Boolean {
        return super.getValue() ?: false
    }
}