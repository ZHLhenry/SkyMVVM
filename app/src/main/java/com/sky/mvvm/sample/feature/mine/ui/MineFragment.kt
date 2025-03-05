package com.sky.mvvm.sample.feature.mine.ui

import android.content.Intent
import android.os.Bundle
import com.sky.mvvm.core.common.base.BaseFragment
import com.sky.mvvm.sample.databinding.FragmentMineBinding
import com.sky.mvvm.sample.feature.mine.vm.MineViewModel
import com.sky.mvvm.sample.feature.other.ui.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/3/4 20:27}</p>
 * <p>{@code description: 文件描述}</p>
 */
@AndroidEntryPoint
class MineFragment : BaseFragment<MineViewModel, FragmentMineBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        addLoadingObserve(mViewModel)
        mDatabind.statelayout.showContent()
        mDatabind.btnLogin.setOnClickListener {
            startActivity(Intent(requireContext(),LoginActivity::class.java))
        }
    }
}