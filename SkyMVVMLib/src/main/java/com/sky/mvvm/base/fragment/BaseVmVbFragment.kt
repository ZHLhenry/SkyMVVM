package com.sky.mvvm.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.sky.mvvm.base.viewmodel.BaseViewModel
import com.sky.mvvm.ext.inflateBindingWithGeneric

/**
 * @Class: BaseVmVbFragment
 * @Author: Henry
 * @Date: 2026/07/13 10:22
 * @Description: 支持ViewBinding的Fragment基类，自动通过泛型inflate布局
 */

abstract class BaseVmVbFragment<VM : BaseViewModel, VB : ViewBinding> : BaseVmFragment<VM>() {
    private val TAG = "BaseVmVbFragment"

    override fun layoutId() = 0

    //该类绑定的 ViewBinding
    private var _binding: VB? = null
    val mViewBind: VB get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBindingWithGeneric(inflater, container, false)
        return mViewBind.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}