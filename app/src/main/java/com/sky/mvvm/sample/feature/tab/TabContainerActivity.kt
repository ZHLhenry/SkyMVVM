package com.sky.mvvm.sample.feature.tab

import android.annotation.SuppressLint
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sky.mvvm.base.container.BaseTabContainerActivity
import com.sky.mvvm.sample.R
import com.sky.mvvm.sample.feature.tab.vm.TabContainerViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * 多 Tab 容器示例：通过「多个 NavHostFragment + show/hide」切换页面。
 * 每个 Tab 拥有独立导航图与返回栈，切换时视图不销毁。
 */
@AndroidEntryPoint
class TabContainerActivity : BaseTabContainerActivity<TabContainerViewModel>() {

    override fun layoutId(): Int = R.layout.activity_tab_container

    override fun containerId(): Int = R.id.nav_host_container

    @SuppressLint("ResourceType")
    override fun tabItems(): List<TabItem> = listOf(
        TabItem("home", R.navigation.nav_tab_home),
        TabItem("mine", R.navigation.nav_tab_mine)
    )

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        findViewById<BottomNavigationView>(R.id.bottom_nav).setOnItemSelectedListener { item ->
            showTab(tagOf(item.itemId))
            true
        }
    }

    /**
     * menuItemId → TabItem.tag 映射
     */
    private fun tagOf(menuItemId: Int): String = when (menuItemId) {
        R.id.menu_tab_home -> "home"
        R.id.menu_tab_mine -> "mine"
        else -> "home"
    }
}
