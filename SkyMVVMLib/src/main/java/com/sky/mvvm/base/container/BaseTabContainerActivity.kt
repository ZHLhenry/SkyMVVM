package com.sky.mvvm.base.container

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.sky.mvvm.base.activity.BaseVmActivity
import com.sky.mvvm.base.viewmodel.BaseViewModel

/**
 * @Class: BaseTabContainerActivity
 * @Author: Henry
 * @Date: 2026/11/14
 * @Description: 多 Tab 容器基类，通过「多个 NavHostFragment + show/hide」切换页面。
 *               与 [BaseContainerActivity]（单 NavHost）不同，本基类各 Tab 拥有独立导航图与返回栈，
 *               切换时视图不销毁（真 show/hide），适用于底部导航、需要保留视图状态的场景。
 *
 * 使用示例：
 * ```kotlin
 * @AndroidEntryPoint
 * class MainActivity : BaseTabContainerActivity<MainViewModel>() {
 *     override fun layoutId() = R.layout.activity_main  // 含 FragmentContainerView + BottomNavigationView
 *     override fun containerId() = R.id.nav_host_container
 *
 *     override fun tabItems() = listOf(
 *         TabItem("home", R.navigation.nav_home),
 *         TabItem("mine", R.navigation.nav_mine)
 *     )
 *
 *     override fun initView(savedInstanceState: Bundle?) {
 *         super.initView(savedInstanceState)
 *         // 底部导航点击时切换 Tab
 *         mBinding.bottomNav.setOnItemSelectedListener { item ->
 *             showTab(tabTagOf(item.itemId))  // 按需将 menuItemId 映射为 TabItem.tag
 *             true
 *         }
 *     }
 * }
 * ```
 */
abstract class BaseTabContainerActivity<VM : BaseViewModel> : BaseVmActivity<VM>() {

    /**
     * 单个 Tab 的描述：tag 为唯一标识，navGraphId 为该 Tab 的导航图
     */
    data class TabItem(val tag: String, @param:IdRes val navGraphId: Int)

    private val navHostFragments = LinkedHashMap<String, NavHostFragment>()
    private var currentTabTag: String? = null

    /**
     * Tab 列表，顺序即展示顺序，首个为默认 Tab
     */
    abstract fun tabItems(): List<TabItem>

    /**
     * 承载所有 NavHostFragment 的容器 View 的 id
     */
    abstract fun containerId(): Int

    override fun initView(savedInstanceState: Bundle?) {
        // 默认展示第一个 Tab
        tabItems().firstOrNull()?.let { showTab(it.tag) }
    }

    /**
     * 切换到指定 Tab（show/hide，不销毁视图）
     *
     * @param tag TabItem.tag
     */
    protected fun showTab(tag: String) {
        if (tag == currentTabTag) return
        val item = tabItems().firstOrNull { it.tag == tag } ?: return

        val transaction = supportFragmentManager.beginTransaction()

        // 隐藏当前 Tab
        currentTabTag?.let { navHostFragments[it]?.let { f -> transaction.hide(f) } }

        // 显示目标 Tab，不存在则先 add
        val target = navHostFragments[tag] ?: NavHostFragment.create(item.navGraphId).also {
            navHostFragments[tag] = it
            transaction.add(containerId(), it, tag)
        }
        transaction.show(target)
        transaction.setPrimaryNavigationFragment(target)
        transaction.commitNow()

        currentTabTag = tag
    }

    /**
     * 获取指定 Tab 的导航控制器
     */
    fun navControllerOf(tag: String): NavController? = navHostFragments[tag]?.navController

    /**
     * Toolbar 返回箭头导航：走当前 Tab 的返回栈
     */
    override fun onSupportNavigateUp(): Boolean {
        val current = currentTabTag?.let { navControllerOf(it) }
        return (current?.navigateUp() ?: false) || super.onSupportNavigateUp()
    }

    /**
     * 容器 Activity 默认不处理 Loading 弹窗，子类按需重写
     */
    override fun showLoading(message: String) {}

    override fun dismissLoading() {}

    override fun createObserver() {}
}
