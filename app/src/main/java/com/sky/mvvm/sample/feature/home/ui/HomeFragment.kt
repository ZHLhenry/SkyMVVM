package com.sky.mvvm.sample.feature.home.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter4.QuickAdapterHelper
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.sky.mvvm.core.common.base.BaseFragment
import com.sky.mvvm.core.common.ext.init
import com.sky.mvvm.core.common.utils.PageUtils
import com.sky.mvvm.core.common.utils.PageUtils.*
import com.sky.mvvm.core.common.utils.StringUtils
import com.sky.mvvm.core.model.ArticleBean
import com.sky.mvvm.ext.parseState
import com.sky.mvvm.network.manager.NetState
import com.sky.mvvm.sample.databinding.FragmentHomeBinding
import com.sky.mvvm.sample.feature.home.adapter.ArticleListAdapter
import com.sky.mvvm.sample.feature.home.vm.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/3/3 19:17}</p>
 * <p>{@code description: 文件描述}</p>
 */
@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {
    @Inject
    lateinit var mArticleListAdapter: ArticleListAdapter
    private lateinit var helper: QuickAdapterHelper
    private lateinit var pageUtils: PageUtils<MutableList<ArticleBean>>

    private var articleListTopResult: MutableList<ArticleBean> = mutableListOf()

    override fun initView(savedInstanceState: Bundle?) {
        addLoadingObserve(mViewModel)
        mDatabind.statelayout.showLoading()
        mViewModel.apiTopAritrilList()

        mDatabind.rvArticleList.init(LinearLayoutManager(activity), mArticleListAdapter, false)
        helper = QuickAdapterHelper.Builder(mArticleListAdapter).build()
        mDatabind.rvArticleList.adapter = helper.adapter

        pageUtils = PageUtils<MutableList<ArticleBean>>(mDatabind.refreshHome).apply {
            setPageUtilListener(object : PageUtilListener<MutableList<ArticleBean>>{
                override fun onPagePullDataFinish(data: MutableList<ArticleBean>?) {
                    mArticleListAdapter.submitList(data)
                }

                override fun onPageLoadMoreDataFinish(data: MutableList<ArticleBean>?) {
                    if (data != null) {
                        mArticleListAdapter.addAll(data)
                    }
                }
            })
        }

        mDatabind.refreshHome.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                pageUtils.resetNowPage()
                mDatabind.refreshHome.autoRefresh()
                mViewModel.apiTopAritrilList()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mViewModel.apiTopAritrilList()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    override fun onNetworkStateChanged(netState: NetState) {
        super.onNetworkStateChanged(netState)
    }

    override fun createObserver() {
        super.createObserver()
        mViewModel.articleListTopResult.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, {
                articleListTopResult = it
                mViewModel.apiArticleListData(pageUtils.getNowPage())
            }, {
                mViewModel.apiArticleListData(pageUtils.getNowPage())
            })
        }
        mViewModel.articleListResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
                val articleListBean: MutableList<ArticleBean>
                if (pageUtils.getNowPage() == 0 && !StringUtils.isEmpty(articleListTopResult)) {
                    articleListTopResult.addAll(it.datas)
                    articleListBean = articleListTopResult
                } else {
                    articleListBean = it.datas
                }
                if (StringUtils.isEmpty(articleListBean)) {
                    mDatabind.statelayout.showEmpty()
                } else {
                    mDatabind.statelayout.showContent()
                }
                pageUtils.setTotalPageAndData(it.pageCount, articleListBean)
            }, {
                mDatabind.statelayout.showError(
                    hintText = "(${it.errCode})${it.errorMsg}"
                )
            })
        }
    }
}
