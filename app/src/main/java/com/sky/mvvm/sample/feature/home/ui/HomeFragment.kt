package com.sky.mvvm.sample.feature.home.ui

import android.os.Bundle
import com.hjq.toast.Toaster
import com.sky.mvvm.core.common.base.BaseFragment
import com.sky.mvvm.ext.parseState
import com.sky.mvvm.network.manager.NetState
import com.sky.mvvm.sample.databinding.FragmentHomeBinding
import com.sky.mvvm.sample.feature.home.vm.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/3/3 19:17}</p>
 * <p>{@code description: 文件描述}</p>
 */
@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        addLoadingObserve(mViewModel)
        mDatabind.statelayout.showLoading()
        mViewModel.apiArticleListData(1)
    }

    override fun onNetworkStateChanged(netState: NetState) {
        super.onNetworkStateChanged(netState)
        mDatabind.tvHomeNetwork.text = "当前网络连接状态:${netState.isSuccess}"
    }

    override fun createObserver() {
        super.createObserver()
        mViewModel.articleListResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
//                    if (pageUtil.getNowPage() == 0 && !StringUtils.isEmpty(articleListTopResult)) {
//                        articleListTopResult.addAll(it.datas)
//                        articleListBean = articleListTopResult
//                    } else {
//                        articleListBean = it.datas
//                    }
//                    pageUtil.setTotalPage(it.pageCount);
//                    finishRefreshAndLoadMore()
//                    if (StringUtils.isEmpty(articleListBean)) {
//                        mDatabind.statelayout.showEmpty()
//                    } else {
                mDatabind.statelayout.showContent()
//                    }
            }, {
//                    finishRefreshAndLoadMore()
                mDatabind.statelayout.showError(
                    hintText = "(${it.errCode})${it.errorMsg}"
                )
            })
        }
    }
}