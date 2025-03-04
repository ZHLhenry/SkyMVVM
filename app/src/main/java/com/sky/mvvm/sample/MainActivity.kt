package com.sky.mvvm.sample

import android.os.Bundle
import com.sky.mvvm.feature.home.ui.HomeFragment
import com.sky.mvvm.sample.adapter.MainTabAdapter
import com.sky.mvvm.core.common.base.BaseActivity
import com.sky.mvvm.sample.databinding.ActivityMainBinding
import com.sky.mvvm.sample.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {
    private lateinit var fragmentAdapter: MainTabAdapter

    override fun initView(savedInstanceState: Bundle?) {
        fragmentAdapter = MainTabAdapter(this)
        fragmentAdapter.addFragment(HomeFragment())
//        fragmentAdapter.addFragment(MineFragment())
        mDatabind.mainVp.adapter = fragmentAdapter
        mDatabind.mainVp.isUserInputEnabled = false
        mDatabind.mainBnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    mDatabind.mainVp.setCurrentItem(0, false)
                }

                R.id.menu_mine -> {
                    mDatabind.mainVp.setCurrentItem(1, false)
                }
            }
            true
        }
    }

}