package com.sky.mvvm.base.container

import android.os.Bundle
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.sky.mvvm.R
import com.sky.mvvm.base.activity.BaseVmActivity
import com.sky.mvvm.base.viewmodel.BaseViewModel

/**
 * @Class: BaseContainerActivity
 * @Author: Henry
 * @Date: 2026/11/14
 * @Description: 单 Activity + 多 Fragment 模式的宿主基类，基于官方 androidx.navigation 实现。
 *               内置 FragmentContainerView(NavHostFragment)，子类只需实现 [navGraphId] 返回导航图。
 *
 * 使用示例：
 * ```kotlin
 * @AndroidEntryPoint
 * class MainActivity : BaseContainerActivity<MainViewModel>() {
 *     override fun navGraphId() = R.navigation.nav_graph
 *     override fun onContainerReady(savedInstanceState: Bundle?) {
 *         // 容器就绪后做额外初始化
 *     }
 * }
 * ```
 */
abstract class BaseContainerActivity<VM : BaseViewModel> : BaseVmActivity<VM>() {

    private lateinit var navHostFragment: NavHostFragment

    /**
     * 导航控制器，通过 lazy 延迟到 [NavHostFragment] 就绪后获取
     */
    val navController: NavController by lazy { navHostFragment.navController }

    /**
     * 使用库内置容器布局（仅含 FragmentContainerView）
     */
    override fun layoutId(): Int = R.layout.sky_mvvmlib_activity_container

    /**
     * 导航图资源 ID
     */
    abstract fun navGraphId(): Int

    override fun initView(savedInstanceState: Bundle?) {
        val container = findViewById<FragmentContainerView>(R.id.sky_mvvmlib_fragment_container)
        navHostFragment = NavHostFragment.create(navGraphId())
        supportFragmentManager.beginTransaction()
            .replace(container.id, navHostFragment)
            .setPrimaryNavigationFragment(navHostFragment)
            .commitNow()
        onContainerReady(savedInstanceState)
    }

    /**
     * 容器就绪后的回调，子类可重写做额外初始化
     */
    open fun onContainerReady(savedInstanceState: Bundle?) {}

    /**
     * Toolbar 返回箭头导航：优先走 [navController] 返回栈，无历史时回退到系统默认行为。
     * 设置 Toolbar 后，返回箭头自动生效。
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    /**
     * 容器 Activity 默认不处理 Loading 弹窗，子类按需重写
     */
    override fun showLoading(message: String) {}

    override fun dismissLoading() {}

    override fun createObserver() {}
}
