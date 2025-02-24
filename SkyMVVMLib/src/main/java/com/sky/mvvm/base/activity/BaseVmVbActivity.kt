package com.sky.mvvm.base.activity
import android.view.View
import androidx.viewbinding.ViewBinding
import com.sky.mvvm.base.viewmodel.BaseViewModel
import com.sky.mvvm.ext.inflateBindingWithGeneric

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/2/23 09:50}</p>
 * <p>{@code description: 文件描述}</p>
 */
abstract class BaseVmVbActivity<VM : BaseViewModel, VB : ViewBinding> : BaseVmActivity<VM>() {

    override fun layoutId(): Int = 0

    lateinit var mViewBind: VB

    /**
     * 创建DataBinding
     */
    override fun initDataBind(): View? {
        mViewBind = inflateBindingWithGeneric(layoutInflater)
        return mViewBind.root

    }
}