@file:Suppress("UNCHECKED_CAST")

package com.sky.mvvm.flow

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.sky.mvvm.ext.util.logd
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/3/5 11:01}</p>
 * <p>{@code description: 文件描述}</p>
 */
object SkyFlow {
    private const val TAG = "SkyFlow"
    private val flowMap = ConcurrentHashMap<String, SkyFlow<*>>()
    private val flowStickMap = ConcurrentHashMap<String, SkyFlowStick<*>>()

    fun <T> with(key: String): SkyFlow<T> {
        return flowMap.getOrPut(key) { SkyFlow<T>(key) } as SkyFlow<T>
    }

    fun <T> withStick(key: String): SkyFlowStick<T> {
        return flowStickMap.getOrPut(key) { SkyFlowStick<T>(key) } as SkyFlowStick<T>
    }

    fun clearUnusedFlow() {
        flowMap.keys.removeAll { key ->
            val flow = flowMap[key]
            flow?._events?.subscriptionCount?.value!! <= 0
        }
        flowStickMap.keys.removeAll { key ->
            val flowStick = flowStickMap[key]
            flowStick?._events?.subscriptionCount?.value!! <= 0
        }
    }

    open class SkyFlow<T>(private val key: String) : DefaultLifecycleObserver {
        internal open val _events by lazy {
            MutableSharedFlow<T>(0, 1, BufferOverflow.DROP_OLDEST)
        }
        private val events = _events.asSharedFlow()

        fun register(
            lifecycleOwner: LifecycleOwner? = null,
            scope: CoroutineScope? = null,
            context: CoroutineContext = Dispatchers.Main,
            filter: (T) -> Boolean = { true },
            action: (t: T) -> Unit
        ) {
            val targetScope = lifecycleOwner?.lifecycleScope ?: scope
            requireNotNull(targetScope) { "Either lifecycleOwner or scope must be provided" }

            targetScope.launch(context) {
                events.filter(filter).collect {
                    try {
                        action(it)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        "FlowBus - Error:$e".logd(tag = TAG)
                    }
                }
            }
        }

        suspend fun post(event: T) {
            _events.emit(event)
        }

        fun post(scope: CoroutineScope, event: T) {
            scope.launch {
                _events.emit(event)
            }
        }

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            "Auto Destroy".logd(tag = TAG)
            if (_events.subscriptionCount.value <= 0) {
                flowMap.remove(key)
            }
        }

        fun destroy() {
            "Not Auto Destroy".logd(tag = TAG)
            if (_events.subscriptionCount.value <= 0) {
                flowMap.remove(key)
            }
        }
    }

    class SkyFlowStick<T>(key: String) : SkyFlow<T>(key) {
        override val _events by lazy {
            MutableSharedFlow<T>(1, 1, BufferOverflow.DROP_OLDEST)
        }
    }
}