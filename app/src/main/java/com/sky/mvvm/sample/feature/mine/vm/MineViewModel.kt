package com.sky.mvvm.sample.feature.mine.vm

import com.sky.mvvm.base.viewmodel.BaseViewModel
import com.sky.mvvm.core.common.net.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/3/4 20:30}</p>
 * <p>{@code description: 文件描述}</p>
 */
@HiltViewModel
class MineViewModel @Inject constructor(private val apiService: ApiService) : BaseViewModel(){

}