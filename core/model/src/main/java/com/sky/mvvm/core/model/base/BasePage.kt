package com.sky.mvvm.core.model.base

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2024/6/6 11:22}</p>
 * <p>{@code description: 文件描述}</p>
 */
open class BasePage{
    var curPage: Int = 0
    var offset: Int = 0
    var over: Boolean = true
    var pageCount: Int = 0
    var size: Int = 0
    var total: Int = 0
}


