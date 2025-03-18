package com.sky.mvvm.sample.feature.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.DataBindingHolder
import com.sky.mvvm.core.common.utils.StringUtils
import com.sky.mvvm.core.model.ArticleBean
import com.sky.mvvm.sample.R
import com.sky.mvvm.sample.databinding.ItemArticleListBinding
import jakarta.inject.Inject

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2024/6/5 09:38}</p>
 * <p>{@code description: 文件描述}</p>
 */
class ArticleListAdapter @Inject constructor() : BaseQuickAdapter<ArticleBean, DataBindingHolder<ItemArticleListBinding>>() {
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: DataBindingHolder<ItemArticleListBinding>,
        position: Int,
        item: ArticleBean?
    ) {
        if (item == null) return
        val binding: ItemArticleListBinding = holder.binding

        binding.itemProjectTag1.visibility = if (item.type == 0) View.GONE else View.VISIBLE
        if (StringUtils.isEmpty(item.tags)){
            binding.itemProjectTag2.visibility = View.GONE
        }else{
            for (tagItem in item.tags) {
                if (tagItem.name == "公众号") {
                    binding.itemProjectTag2.visibility = View.VISIBLE
                }
            }
        }
        binding.itemProjectShareUser.text = if (StringUtils.isEmpty(item.shareUser)) item.author else item.shareUser
        binding.itemProjectTitle.text = item.title
        binding.itemProjectSuperChapterName.text = "${item.superChapterName}/${item.chapterName}"
        binding.itemProjectDate.text = item.niceDate
        binding.executePendingBindings()
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): DataBindingHolder<ItemArticleListBinding> {
        return DataBindingHolder(R.layout.item_article_list, parent)
    }


}