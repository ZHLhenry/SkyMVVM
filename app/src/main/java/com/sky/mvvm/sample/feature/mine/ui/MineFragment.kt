package com.sky.mvvm.sample.feature.mine.ui

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import com.sky.mvvm.core.common.base.BaseFragment
import com.sky.mvvm.sample.databinding.FragmentMineBinding
import com.sky.mvvm.sample.feature.mine.vm.MineViewModel
import com.sky.mvvm.sample.feature.other.ui.LoginActivity
import com.sky.widget.iconfont.SkyIconFontsLib
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
        // 通过 SkyIconFontsLib.style() 解析 {prefix_name} 格式的图标标记
        mDatabind.tvIcon.text = SkyIconFontsLib.style(
            SpannableString("${SkyIconFontsLib.getFormattedIconName("skyshouye")} 首页")
        )
    }
}