package com.sky.mvvm.sample.feature.nav

import com.sky.mvvm.base.container.BaseContainerActivity
import com.sky.mvvm.sample.R
import com.sky.mvvm.sample.feature.nav.vm.NavContainerViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * 单 Activity + 多 Fragment 示例宿主 Activity。
 * 继承 BaseContainerActivity，只需实现 [navGraphId] 返回导航图即可。
 */
@AndroidEntryPoint
class NavContainerActivity : BaseContainerActivity<NavContainerViewModel>() {

    override fun navGraphId(): Int = R.navigation.nav_graph
}
