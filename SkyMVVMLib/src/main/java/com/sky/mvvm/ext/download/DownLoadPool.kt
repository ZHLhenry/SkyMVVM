package com.sky.mvvm.ext.download

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import java.util.concurrent.ConcurrentHashMap

/**
 * @Class: DownLoadPool
 * @Author: Henry
 * @Date: 2025/2/23 10:36
 * @Description: 下载任务池，管理下载协程、路径、监听器的映射关系
 */

object DownLoadPool {

    private val scopeMap: ConcurrentHashMap<String, CoroutineScope> = ConcurrentHashMap()

    //下载位置
    private val pathMap: ConcurrentHashMap<String, String> = ConcurrentHashMap()

    //监听
    private val listenerHashMap: ConcurrentHashMap<String, OnDownLoadListener> = ConcurrentHashMap()

    //主动取消的下载标识，用于在协程取消出口回调onDownLoadCancel
    private val cancelledKeys: MutableSet<String> = ConcurrentHashMap.newKeySet()

    //已成功/失败结束的下载标识，避免结束后再回调取消
    private val finishedKeys: MutableSet<String> = ConcurrentHashMap.newKeySet()

    fun add(key: String, job: CoroutineScope) {
        scopeMap[key] = job
    }

    //监听
    fun add(key: String, loadListener: OnDownLoadListener) {
        listenerHashMap[key] = loadListener
    }

    //下载位置
    fun add(key: String, path: String) {
        pathMap[key] = path
    }

    fun remove(key: String) {
        pause(key)
        scopeMap.remove(key)
        listenerHashMap.remove(key)
        pathMap.remove(key)
        ShareDownLoadUtil.remove(key)
    }

    fun pause(key: String) {
        val scope = scopeMap[key]
        if (scope != null && scope.isActive) {
            scope.cancel()
        }
    }

    fun removeExitSp(key: String) {
        scopeMap.remove(key)
    }

    //标记下载被主动取消
    fun markCancelled(key: String) {
        cancelledKeys.add(key)
    }

    //消费取消标记
    fun consumeCancelled(key: String): Boolean {
        return cancelledKeys.remove(key)
    }

    //标记下载已结束（成功或失败）
    fun markFinished(key: String) {
        finishedKeys.add(key)
    }

    //是否已结束（成功或失败）
    fun isFinished(key: String): Boolean {
        return finishedKeys.contains(key)
    }

    //清理上一次任务遗留的取消/结束标记
    fun resetState(key: String) {
        cancelledKeys.remove(key)
        finishedKeys.remove(key)
    }

    fun getScopeFromKey(key: String): CoroutineScope? {
        return scopeMap[key]
    }

    fun getListenerFromKey(key: String): OnDownLoadListener? {
        return listenerHashMap[key]
    }

    fun getPathFromKey(key: String): String? {
        return pathMap[key]
    }

    fun getListenerMap(): ConcurrentHashMap<String, OnDownLoadListener> {
        return listenerHashMap
    }

}