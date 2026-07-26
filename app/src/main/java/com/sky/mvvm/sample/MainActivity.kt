package com.sky.mvvm.sample

import android.os.Bundle
import com.google.android.material.badge.BadgeDrawable
import com.sky.widget.iconfont.SkyIconFontsLib
import com.sky.mvvm.core.common.base.BaseActivity
import com.sky.mvvm.sample.adapter.MainTabAdapter
import com.sky.mvvm.sample.databinding.ActivityMainBinding
import com.sky.mvvm.sample.feature.home.ui.HomeFragment
import com.sky.mvvm.sample.feature.mine.ui.MineFragment
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.view.get
import androidx.core.view.size

@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {
    private lateinit var fragmentAdapter: MainTabAdapter

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.mainBnv.enableAnimation(false)
        mDatabind.mainBnv.enableItemHorizontalTranslation(false)
        // 使用 Android-Iconics 设置底部导航图标
        setupIconicsIcons()
        fragmentAdapter = MainTabAdapter(this)
        fragmentAdapter.addFragment(HomeFragment())
        fragmentAdapter.addFragment(MineFragment())
        mDatabind.mainVp.adapter = fragmentAdapter
        mDatabind.mainBnv.setupWithViewPager2(mDatabind.mainVp)
        mDatabind.mainVp.isUserInputEnabled = false
        val bd: BadgeDrawable = mDatabind.mainBnv.realView.getOrCreateBadge(R.id.menu_home)
        bd.number = 9899
        bd.horizontalOffset = 12
        bd.verticalOffset = 2
    }

    /**
     * 使用 Android-Iconics 动态设置底部导航栏图标
     * 通过 typeface.getIcon() 按名称查找，支持自定义 TTF 的图标名称
     */
    private fun setupIconicsIcons() {
        val menu = mDatabind.mainBnv.menu
        for (i in 0 until menu.size) {
            val item = menu[i]
            val iconName = when (item.itemId) {
//                R.id.menu_home -> "testSkyshouye"
//                R.id.menu_project -> "testSkyxiangmu"
//                R.id.menu_plaza -> "testSkyguangchang"
//                R.id.menu_system_tree -> "testSkytestSkytixi"
//                R.id.menu_mine -> "testSkywode"
                R.id.menu_home -> "skyshouye"
                R.id.menu_project -> "skyxiangmu"
                R.id.menu_plaza -> "skyguangchang"
                R.id.menu_system_tree -> "skytixi"
                R.id.menu_mine -> "skywode"
                else -> null
            }
            if (iconName != null) {
                item.icon = SkyIconFontsLib.drawable(this, iconName)
            }
        }
        // 配合 SkyBottomNavigationView 调整图标大小
        mDatabind.mainBnv.setIconSize(24f, 24f)
    }

}