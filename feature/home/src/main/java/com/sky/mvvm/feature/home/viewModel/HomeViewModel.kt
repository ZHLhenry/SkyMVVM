package com.sky.mvvm.feature.home.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.sky.mvvm.base.viewmodel.BaseViewModel
import com.sky.mvvm.core.common.net.ApiService
import com.sky.mvvm.core.model.ArticleResponseBean
import com.sky.mvvm.ext.request
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
class HomeViewModel @Inject constructor(private val apiService: ApiService) : BaseViewModel() {
    var articleListResult = MutableLiveData<ResultState<ArticleResponseBean>>()
    /**
     * 首页文章请求
     */
    fun apiArticleListData(currentPage: Int = 0) {
//        viewModelScope.launch {
//            val dataFromAPI1 = async(Dispatchers.IO) {
//                apiService.getEntryAndExitDataApi(currentPage)
//            }.await()
//            val dataFromAPI2 = async(Dispatchers.IO) { apiService.getTopAritrilList() }.await()
//            dataFromAPI1.data.datas.addAll(dataFromAPI2.data)
//        }
        Log.e("currentPage==>", currentPage.toString())

        request(
            { apiService.getEntryAndExitDataApi(currentPage) }, articleListResult
        )
    }
}