package com.sky.mvvm.core.common.utils

import com.sky.mvvm.core.common.widget.CustomRefreshLayout


/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2024/6/13 16:23}</p>
 * <p>{@code description: 分页逻辑集中处理}</p>
 */
class PageUtils<T>(
    private val smartRefreshLayout: CustomRefreshLayout,
    private var pageUtilListener: PageUtilListener<T>? = null
) {
    // 默认当前页码
    private val defaultNowPage = 0

    // 默认每页显示多少条
    private val defaultPageShow = 5

    // 当前页码
    private var nowPage: Int = defaultNowPage

    // 每页显示条数
    private var pageShow: Int = defaultPageShow

    // 总页数
    private var totalPage: Int = 0

    /**
     * 重置当前页码
     */
    fun resetNowPage() {
        nowPage = defaultNowPage
    }

    /**
     * 获取当前页码
     */
    fun getNowPage(): Int = nowPage

    /**
     * 获取每页显示条数
     */
    fun getPageShow(): Int = pageShow

    /**
     * 设置总页数和数据源
     */
    fun setTotalPageAndData(totalPage: Int, data: T) {
        this.totalPage = totalPage
        if (nowPage == defaultNowPage) {
            handleFirstPageLoad(data)
        } else {
            handleSubsequentPageLoad(data)
        }
    }

    /**
     * 处理首次加载数据
     */
    private fun handleFirstPageLoad(data: T) {
        smartRefreshLayout.finishRefresh()
        if (smartRefreshLayout.isEnableLoadMore) {
            smartRefreshLayout.finishLoadMore(true)
        } else {
            smartRefreshLayout.setEnableLoadMore(false)
        }

        if (totalPage <= nowPage) {
            smartRefreshLayout.setNoMoreData(true)
            smartRefreshLayout.finishLoadMore(true)
        } else {
            nowPage++
            smartRefreshLayout.setNoMoreData(false)
            smartRefreshLayout.setEnableLoadMore(true)
        }
        pageUtilListener?.onPagePullDataFinish(data)
    }

    /**
     * 处理后续加载数据
     */
    private fun handleSubsequentPageLoad(data: T) {
        if (totalPage <= nowPage) {
            smartRefreshLayout.setNoMoreData(true)
            smartRefreshLayout.finishLoadMore(true)
        } else {
            nowPage++
            smartRefreshLayout.finishLoadMore(true)
        }

        // 通知监听器加载更多数据完成
        pageUtilListener?.onPageLoadMoreDataFinish(data)
    }

    /**
     * 设置页面监听器
     */
    fun setPageUtilListener(pageUtilListener: PageUtilListener<T>) {
        this.pageUtilListener = pageUtilListener
    }

    /**
     * 页面监听器接口
     */
    interface PageUtilListener<T> {
        fun onPagePullDataFinish(data: T)
        fun onPageLoadMoreDataFinish(data: T)
    }
}