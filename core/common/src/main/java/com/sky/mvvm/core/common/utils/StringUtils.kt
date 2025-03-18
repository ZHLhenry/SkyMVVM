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
            when (it) {
                is String -> return it.trim().isEmpty() || it.trim() == "null"
                is Iterable<*> -> return !it.iterator().hasNext()
                is Array<*> -> return it.size == 0
                is Map<*, *> -> return it.isEmpty()
                is Number, is Date -> return false
                else -> return false
            }
        }
        return true
    }
}