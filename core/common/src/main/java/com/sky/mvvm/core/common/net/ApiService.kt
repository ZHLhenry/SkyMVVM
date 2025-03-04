package com.sky.mvvm.core.common.net

import com.sky.mvvm.core.model.ArticleBean
import com.sky.mvvm.core.model.ArticleResponseBean
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * <p>{@code className: ApiService}</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2024/6/4 16:51}</p>
 * <p>{@code description: 文件描述}</p>
 */
interface ApiService {

    /**
     * 首页文章列表
     */
    @GET("article/list/{currentPage}/json")
    suspend fun getEntryAndExitDataApi(
        @Path("currentPage") currentPage: Int = 0,
        @Query("page_size") pageSize: Int = 5
    ): ApiResponse<ArticleResponseBean>

    /**
     * 获取置顶文章集合数据
     */
    @GET("article/top/json")
    suspend fun getTopAritrilList(): ApiResponse<ArrayList<ArticleBean>>

}