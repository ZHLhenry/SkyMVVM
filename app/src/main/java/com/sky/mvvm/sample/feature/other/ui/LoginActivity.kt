package com.sky.mvvm.sample.feature.other.ui

import com.sky.mvvm.core.common.base.BaseActivity
import com.sky.mvvm.sample.databinding.ActivityLoginBinding
import com.sky.mvvm.sample.feature.other.vm.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/3/5 10:04}</p>
 * <p>{@code description: 文件描述}</p>
 */
@AndroidEntryPoint
class LoginActivity: BaseActivity<LoginViewModel,ActivityLoginBinding>() {
    override fun initView(savedInstanceState: android.os.Bundle?) {
        addLoadingObserve(mViewModel)
    }
}