package com.sky.mvvm.sample.feature.home.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.sky.mvvm.base.viewmodel.BaseViewModel
import com.sky.mvvm.core.common.net.ApiService
import com.sky.mvvm.core.model.ArticleBean
import com.sky.mvvm.core.model.ArticleResponseBean
import com.sky.mvvm.ext.apiRequest
import com.sky.mvvm.network.state.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/3/3 19:18}</p>
 * <p>{@code description: 文件描述}</p>
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val application: Application,
    private val apiService: ApiService
) : BaseViewModel() {
    var articleListResult = MutableLiveData<ResultState<ArticleResponseBean>>()
    var articleListTopResult = MutableLiveData<ResultState<MutableList<ArticleBean>>>()

    /**
     * 首页文章请求
     */
    fun apiArticleListData(currentPage: Int = 0) {
        apiRequest(
            application.applicationContext,
            { apiService.getEntryAndExitDataApi(currentPage) },
            articleListResult,
            isShowDialog = true
        )
    }

    /**
     * 获取置顶文章数据
     */
    fun apiTopAritrilList() {
        apiRequest(
            application.applicationContext,
            { apiService.getTopAritrilList() },
            articleListTopResult,
            isShowDialog = true
        )
    }
}