package com.sky.mvvm.sample.feature.nav.ui

import android.os.Bundle
import android.widget.Button
import com.sky.mvvm.base.fragment.BaseVmFragment
import com.sky.mvvm.base.nav.popBack
import com.sky.mvvm.base.nav.popTo
import com.sky.mvvm.base.nav.popToRoot
import com.sky.mvvm.base.nav.setNavResultTo
import com.sky.mvvm.base.viewmodel.BaseViewModel
import com.sky.mvvm.core.common.ext.dismissLoadingExt
import com.sky.mvvm.core.common.ext.showLoadingExt
import com.sky.mvvm.ext.view.clickNoRepeat
import com.sky.mvvm.sample.R
import dagger.hilt.android.AndroidEntryPoint

/**
 * 第三层页面，演示返回栈操作 API：
 * 1. popBack：返回上一页（Second）
 * 2. popTo：返回到指定 destination（First）
 * 3. popToRoot：返回到根节点（First）
 */
@AndroidEntryPoint
class ThirdFragment : BaseVmFragment<BaseViewModel>() {

    override fun layoutId(): Int = R.layout.fragment_third

    override fun initView(savedInstanceState: Bundle?) {
        val btnPopBack = requireView().findViewById<Button>(R.id.btn_pop_back)
        val btnPopToFirst = requireView().findViewById<Button>(R.id.btn_pop_to_first)
        val btnPopToRoot = requireView().findViewById<Button>(R.id.btn_pop_to_root)
        val btnSetResultToFirst = requireView().findViewById<Button>(R.id.btn_set_result_to_first)

        // 返回上一页（Second）
        btnPopBack.clickNoRepeat {
            popBack()
        }

        // 返回到指定 destination（First，First 保留在栈中）
        btnPopToFirst.clickNoRepeat {
            popTo(R.id.firstFragment)
        }

        // 返回根页面（First）
        btnPopToRoot.clickNoRepeat {
            popToRoot()
        }

        // 跨层返回 First 并携带参数（跳过 Second，First 用 observeNavResult 接收）
        btnSetResultToFirst.clickNoRepeat {
            setNavResultTo(R.id.firstFragment, "result_key", "来自 Third 跨层带回的数据")
        }
    }

    override fun createObserver() {}
    override fun lazyLoadData() {}

    override fun showLoading(message: String) {
        showLoadingExt(message)
    }

    override fun dismissLoading() {
        dismissLoadingExt()
    }
}
