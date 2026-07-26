package com.sky.mvvm.core.common.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.databinding.ViewDataBinding
import com.gyf.immersionbar.ktx.immersionBar
import com.sky.mvvm.base.activity.BaseVmDbActivity
import com.sky.mvvm.base.viewmodel.BaseViewModel
import com.sky.mvvm.core.common.ext.dismissLoadingExt
import com.sky.mvvm.core.common.ext.showLoadingExt

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/2/23 15:31</p>
 * <p>{@code description: 文件描述}</p>
 */
abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmDbActivity<VM, DB>() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        immersionBar{
            transparentStatusBar()
            transparentNavigationBar()
            init()
        }
    }

    abstract override fun initView(savedInstanceState: Bundle?)

    override fun createObserver() {
    }

    override fun showLoading(message: String) {
        showLoadingExt(message)
    }

    override fun dismissLoading() {
        dismissLoadingExt()
    }
}