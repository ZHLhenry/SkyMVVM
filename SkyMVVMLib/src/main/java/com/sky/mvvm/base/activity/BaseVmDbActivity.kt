package com.sky.mvvm.base.activity

import android.view.View
import androidx.databinding.ViewDataBinding
import com.sky.mvvm.base.viewmodel.BaseViewModel
import com.sky.mvvm.ext.inflateBindingWithGeneric

/**
 * @Class: BaseVmDbActivity
 * @Author: Henry
 * @Date: 2025/2/23 09:49
 * @Description: 支持DataBinding的Activity基类，自动通过泛型inflate布局并绑定ViewModel
 */

abstract class BaseVmDbActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmActivity<VM>() {

    private val TAG = "BaseVmDbActivity"

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