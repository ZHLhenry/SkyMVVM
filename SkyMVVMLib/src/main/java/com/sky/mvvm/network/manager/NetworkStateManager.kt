package com.sky.mvvm.network.manager

import com.sky.mvvm.callback.livedata.event.EventLiveData

/**
 * @Class: NetworkStateManager
 * @Author: Henry
 * @Date: 2025/2/23 10:27
 * @Description: 网络状态管理器，单例模式管理网络状态变化的EventLiveData
 */

class NetworkStateManager private constructor() {
    val mNetworkStateCallback = EventLiveData<NetState>()

    companion object {
        val instance: NetworkStateManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkStateManager()
        }
    }

}