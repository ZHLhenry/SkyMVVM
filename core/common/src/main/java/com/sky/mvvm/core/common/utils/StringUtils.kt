package com.sky.mvvm.core.common.utils

import java.util.Date

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/3/10 11:32}</p>
 * <p>{@code description: 文件描述}</p>
 */
object StringUtils {
    /**
     * 判空
     */
    fun isEmpty(obj: Any?): Boolean {
        obj?.let {
            return when (it) {
                is String -> it.trim().isEmpty() || it.trim() == "null"
                is Iterable<*> -> !it.iterator().hasNext()
                is Array<*> -> it.isEmpty()
                is Map<*, *> -> it.isEmpty()
                is Number, is Date -> false
                else -> false
            }
        }
        return true
    }
}