package com.sky.mvvm.core.common.widget

import android.content.Context
import android.util.AttributeSet
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2024/6/13 16:36}</p>
 * <p>{@code description: 文件描述}</p>
 */
class CustomRefreshLayout : SmartRefreshLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    val isEnableRefresh: Boolean
        get() = mEnableRefresh

    val isEnableLoadMore: Boolean
        get() = mEnableLoadMore
}