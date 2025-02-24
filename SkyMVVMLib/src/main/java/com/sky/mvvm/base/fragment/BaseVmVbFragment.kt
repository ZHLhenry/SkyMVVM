package com.sky.mvvm.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.sky.mvvm.base.viewmodel.BaseViewModel
import com.sky.mvvm.ext.inflateBindingWithGeneric

abstract class BaseVmVbFragment<VM : BaseViewModel, VB : ViewBinding> : BaseVmFragment<VM>() {

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