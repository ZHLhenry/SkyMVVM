package com.sky.mvvm.callback.livedata.event

import com.sky.mvvm.callback.UnPeekLiveData

/**
 * @Class: EventLiveData
 * @Author: Henry
 * @Date: 2025/2/23 10:17
 * @Description: 事件型LiveData，继承UnPeekLiveData用于一次性事件分发
 */

class EventLiveData<T> : UnPeekLiveData<T>()