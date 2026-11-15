package com.sky.mvvm.sample.feature.nav.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.sky.mvvm.base.fragment.BaseVmFragment
import com.sky.mvvm.base.nav.navigateTo
import com.sky.mvvm.base.nav.obtainNavGraphViewModel
import com.sky.mvvm.base.nav.setNavResult
import com.sky.mvvm.base.viewmodel.BaseViewModel
import com.sky.mvvm.core.common.ext.dismissLoadingExt
import com.sky.mvvm.core.common.ext.showLoadingExt
import com.sky.mvvm.ext.view.clickNoRepeat
import com.sky.mvvm.sample.R
import com.sky.mvvm.sample.feature.nav.vm.NavSharedViewModel
import com.sky.mvvm.util.extraFrag
import dagger.hilt.android.AndroidEntryPoint

/**
 * 详情页 Fragment，演示：
 * 1. 读取 Navigation 传递的参数（复用 extraFrag，读取 arguments）
 * 2. 更新图作用域共享数据
 * 3. 回传结果并返回上一页
 */
@AndroidEntryPoint
class SecondFragment : BaseVmFragment<BaseViewModel>() {

    // 图作用域共享 ViewModel（与 FirstFragment 共享同一实例）
    private val sharedViewModel by lazy {
        obtainNavGraphViewModel(NavSharedViewModel::class.java, R.id.nav_graph)
    }

    // 复用 extraFrag 读取 Navigation 通过 arguments 传递的参数
    private var userName: String? by extraFrag("user_name")
    private var age: Int by extraFrag("age", 0)

    override fun layoutId(): Int = R.layout.fragment_second

    override fun initView(savedInstanceState: Bundle?) {
        val tvParam = requireView().findViewById<TextView>(R.id.tv_param)
        val etMessage = requireView().findViewById<EditText>(R.id.et_message)
        val btnBack = requireView().findViewById<Button>(R.id.btn_back)
        val btnGoThird = requireView().findViewById<Button>(R.id.btn_go_third)

        tvParam.text = "收到参数：userName=$userName, age=$age"

        btnBack.clickNoRepeat {
            // 更新共享数据（First 会收到）
            sharedViewModel.updateMessage(etMessage.text.toString())
            // 回传结果并返回上一页
            setNavResult("result_key", "来自 Second 的回传数据")
        }

        // 前进到 Third，演示 navigateTo
        btnGoThird.clickNoRepeat {
            navigateTo(R.id.thirdFragment)
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
