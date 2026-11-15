package com.sky.mvvm.sample.feature.tab.ui

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.sky.mvvm.base.fragment.BaseVmFragment
import com.sky.mvvm.base.nav.popBack
import com.sky.mvvm.base.viewmodel.BaseViewModel
import com.sky.mvvm.core.common.ext.dismissLoadingExt
import com.sky.mvvm.core.common.ext.showLoadingExt
import com.sky.mvvm.ext.view.clickNoRepeat
import com.sky.mvvm.sample.R
import com.sky.mvvm.util.extraFrag
import dagger.hilt.android.AndroidEntryPoint

/**
 * Tab 详情页（两个导航图共用），演示 Tab 内导航与返回栈操作。
 */
@AndroidEntryPoint
class TabDetailFragment : BaseVmFragment<BaseViewModel>() {

    // 复用 extraFrag 读取 navigateTo 传递的参数
    private var title: String? by extraFrag("title")

    override fun layoutId(): Int = R.layout.fragment_tab_detail

    override fun initView(savedInstanceState: Bundle?) {
        requireView().findViewById<TextView>(R.id.tv_title).text = title ?: "详情页"

        requireView().findViewById<Button>(R.id.btn_pop_back).clickNoRepeat {
            popBack()
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
