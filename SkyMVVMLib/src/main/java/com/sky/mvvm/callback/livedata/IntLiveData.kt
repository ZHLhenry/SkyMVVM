package com.sky.mvvm.callback.livedata

import androidx.lifecycle.MutableLiveData

/**
 * @Class: IntLiveData
 * @Author: Henry
 * @Date: 2025/2/23 10:14
 * @Description: Int类型的LiveData封装，提供默认值0避免空指针
 */

class IntLiveData : MutableLiveData<Int>() {
    override fun getValue(): Int {
        return super.getValue() ?: 0
    }
}