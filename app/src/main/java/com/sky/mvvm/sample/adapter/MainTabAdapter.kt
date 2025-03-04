package com.sky.mvvm.sample.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/3/3 16:48}</p>
 * <p>{@code description: 文件描述}</p>
 */
@Suppress("DEPRECATION")
class MainTabAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private var fragments: MutableList<Class<*>>? = null

    init {
        if (fragments == null) {
            fragments = ArrayList()
        }
    }

    fun addFragment(fragment: Fragment) {
        if (fragments != null) {
            fragments?.add(fragment.javaClass)
        }
    }

    override fun createFragment(position: Int): Fragment {
        try {
            return fragments!![position].newInstance() as Fragment
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        }
        return null!!
    }

    override fun getItemCount(): Int {
        return fragments!!.size
    }
}