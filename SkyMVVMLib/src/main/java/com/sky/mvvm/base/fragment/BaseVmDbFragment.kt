package com.sky.mvvm.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.sky.mvvm.base.viewmodel.BaseViewModel
import com.sky.mvvm.ext.inflateBindingWithGeneric

/**
 * @Class: BaseVmDbFragment
 * @Author: Henry
 * @Date: 2026/07/13 10:22
 * @Description: 支持DataBinding的Fragment基类，自动通过泛型inflate布局并绑定ViewModel
 */

abstract class BaseVmDbFragment<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmFragment<VM>() {

    private val TAG = "BaseVmDbFragment"

    override fun layoutId() = 0

    //该类绑定的ViewDataBinding
    private var _binding: DB? = null
    val mDatabind: DB get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBindingWithGeneric(inflater, container, false)
        return mDatabind.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}