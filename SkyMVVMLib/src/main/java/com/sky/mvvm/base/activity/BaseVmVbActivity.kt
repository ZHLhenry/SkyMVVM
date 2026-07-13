package com.sky.mvvm.base.activity
import android.view.View
import androidx.viewbinding.ViewBinding
import com.sky.mvvm.base.viewmodel.BaseViewModel
import com.sky.mvvm.ext.inflateBindingWithGeneric

/**
 * @Class: BaseVmVbActivity
 * @Author: Henry
 * @Date: 2025/2/23 09:50
 * @Description: 支持ViewBinding的Activity基类，自动通过泛型inflate布局并绑定ViewModel
 */

abstract class BaseVmVbActivity<VM : BaseViewModel, VB : ViewBinding> : BaseVmActivity<VM>() {

    private val TAG = "BaseVmVbActivity"

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