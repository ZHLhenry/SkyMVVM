package com.sky.mvvm.core.model

import com.sky.mvvm.core.model.base.BasePage
import com.squareup.moshi.JsonClass

/**
 * <p>{@code className: ArticleListBean}</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2024/6/4 16:55}</p>
 * <p>{@code description: 文件描述}</p>
 */
@JsonClass(generateAdapter = true)
data class ArticleResponseBean(
    val datas: MutableList<ArticleBean>,
): BasePage()
@JsonClass(generateAdapter = true)
data class ArticleBean(
    val adminAdd: Boolean,
    val apkLink: String,
    val audit: Int,
    val author: String,
    val canEdit: Boolean,
    val chapterId: Int,
    val chapterName: String,
    val collect: Boolean,
    val courseId: Int,
    val desc: String,
    val descMd: String,
    val envelopePic: String,
    val fresh: Boolean,
    val host: String,
    val id: Int,
    val isAdminAdd: Boolean,
    val link: String,
    val niceDate: String,
    val niceShareDate: String,
    val origin: String,
    val prefix: String,
    val projectLink: String,
    val publishTime: Long,
    val realSuperChapterId: Int,
    val selfVisible: Int,
    val shareDate: Long,
    val shareUser: String,
    val superChapterId: Int,
    val superChapterName: String,
    val tags: List<Tag>,
    val title: String,
    val type: Int,
    val userId: Int,
    val visible: Int,
    val zan: Int
)
@JsonClass(generateAdapter = true)
data class Tag(
    val name: String,
    val url: String
)