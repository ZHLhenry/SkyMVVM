package com.sky.mvvm.callback.livedata

import androidx.lifecycle.MutableLiveData

/**
 * @Class: StringLiveData
 * @Author: Henry
 * @Date: 2025/2/23 10:15
 * @Description: String类型的LiveData封装，提供默认值空字符串避免空指针
 */

class StringLiveData : MutableLiveData<String>() {
    override fun getValue(): String {
        return super.getValue() ?: ""
    }
}