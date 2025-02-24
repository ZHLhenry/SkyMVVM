package com.sky.mvvm.base.activity

import android.view.View
import androidx.databinding.ViewDataBinding
import com.sky.mvvm.base.viewmodel.BaseViewModel
import com.sky.mvvm.ext.inflateBindingWithGeneric

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/2/23 09:49}</p>
 * <p>{@code description: 文件描述}</p>
 */
abstract class BaseVmDbActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmActivity<VM>() {

    override fun layoutId() = 0

    lateinit var mDatabind: DB

    /**
     * 创建DataBinding
     */
    override fun initDataBind(): View? {
        mDatabind = inflateBindingWithGeneric(layoutInflater)
        return mDatabind.root
    }
}