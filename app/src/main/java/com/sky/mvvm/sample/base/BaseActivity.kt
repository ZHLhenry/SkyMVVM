package com.sky.mvvm.sample.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.databinding.ViewDataBinding
import com.gyf.immersionbar.ktx.immersionBar
import com.sky.mvvm.base.activity.BaseVmDbActivity
import com.sky.mvvm.base.viewmodel.BaseViewModel

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/2/23 15:31}</p>
 * <p>{@code description: 文件描述}</p>
 */
abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmDbActivity<VM, DB>() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        immersionBar{
            transparentStatusBar()
            init()
        }
    }

    abstract override fun initView(savedInstanceState: Bundle?)

    override fun createObserver() {
    }

    override fun showLoading(message: String) {
    }

    override fun dismissLoading() {
    }
}