package com.sky.mvvm.sample.feature.nav.ui

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.sky.mvvm.base.fragment.BaseVmFragment
import com.sky.mvvm.base.nav.observeNavResult
import com.sky.mvvm.base.nav.obtainNavGraphViewModel
import com.sky.mvvm.base.viewmodel.BaseViewModel
import com.sky.mvvm.core.common.ext.dismissLoadingExt
import com.sky.mvvm.core.common.ext.showLoadingExt
import com.sky.mvvm.ext.view.clickNoRepeat
import com.sky.mvvm.sample.R
import com.sky.mvvm.sample.feature.nav.vm.NavSharedViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * 首页 Fragment，演示：
 * 1. 图作用域共享 ViewModel
 * 2. 跳转传参
 * 3. 结果回传
 */
@AndroidEntryPoint
class FirstFragment : BaseVmFragment<BaseViewModel>() {

    // 图作用域共享 ViewModel（与 SecondFragment 共享同一实例）
    private val sharedViewModel by lazy {
        obtainNavGraphViewModel(NavSharedViewModel::class.java, R.id.nav_graph)
    }

    override fun layoutId(): Int = R.layout.fragment_first

    override fun initView(savedInstanceState: Bundle?) {
        val tvSharedMessage = requireView().findViewById<TextView>(R.id.tv_shared_message)
        val tvResult = requireView().findViewById<TextView>(R.id.tv_result)
        val btnGoSecond = requireView().findViewById<Button>(R.id.btn_go_second)

        // 观察共享数据
        sharedViewModel.sharedMessage.observe(viewLifecycleOwner) { message ->
            tvSharedMessage.text = "共享消息：$message"
        }

        // 观察 Second 回传的结果（一次性消费）
        observeNavResult<String>("result_key") { value ->
            value?.let { tvResult.text = "返回结果：$it" }
        }

        // 跳转到 Second，携带参数
        btnGoSecond.clickNoRepeat {
            findNavController().navigate(
                R.id.action_first_to_second,
                bundleOf("user_name" to "Henry", "age" to 25)
            )
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
