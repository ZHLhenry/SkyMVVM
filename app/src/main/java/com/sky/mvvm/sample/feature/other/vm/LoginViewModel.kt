package com.sky.mvvm.sample.feature.other.vm

import android.app.Application
import com.sky.mvvm.base.viewmodel.BaseViewModel
import com.sky.mvvm.core.common.net.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/3/5 10:08}</p>
 * <p>{@code description: 文件描述}</p>
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val application: Application,
    private val apiService: ApiService
) : BaseViewModel() {

}