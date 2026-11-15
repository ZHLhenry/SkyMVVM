package com.sky.mvvm.sample.feature.tab.ui

import android.os.Bundle
import android.widget.Button
import com.sky.mvvm.base.fragment.BaseVmFragment
import com.sky.mvvm.base.nav.navigateTo
import com.sky.mvvm.base.viewmodel.BaseViewModel
import com.sky.mvvm.core.common.ext.dismissLoadingExt
import com.sky.mvvm.core.common.ext.showLoadingExt
import com.sky.mvvm.ext.view.clickNoRepeat
import com.sky.mvvm.sample.R
import dagger.hilt.android.AndroidEntryPoint

/**
 * 我的 Tab 根页面。
 */
@AndroidEntryPoint
class MineTabFragment : BaseVmFragment<BaseViewModel>() {

    override fun layoutId(): Int = R.layout.fragment_tab_mine

    override fun initView(savedInstanceState: Bundle?) {
        requireView().findViewById<Button>(R.id.btn_go_detail).clickNoRepeat {
            navigateTo(R.id.mineTabDetailFragment, "title" to "我的 Tab 详情")
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
