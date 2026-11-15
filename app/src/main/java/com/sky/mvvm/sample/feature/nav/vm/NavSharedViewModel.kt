package com.sky.mvvm.sample.feature.nav.vm

import androidx.lifecycle.MutableLiveData
import com.sky.mvvm.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 导航图作用域共享 ViewModel。
 * 通过 [com.sky.mvvm.base.nav.obtainNavGraphViewModel] 获取，同一导航图内的所有 destination 共享同一实例。
 */
@HiltViewModel
class NavSharedViewModel @Inject constructor() : BaseViewModel() {

    val sharedMessage = MutableLiveData("等待更新...")

    fun updateMessage(message: String) {
        sharedMessage.value = message
    }
}
