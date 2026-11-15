package com.sky.mvvm.base.nav

import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import java.io.Serializable

/**
 * @Class: FragmentNavExt
 * @Author: Henry
 * @Date: 2026/11/14
 * @Description: Navigation 场景下的 Fragment 扩展，封装导航跳转、返回栈操作、图作用域 ViewModel、结果回传
 */

/**
 * 跳转到指定 destination（携带参数）
 *
 * 使用示例：
 * ```kotlin
 * navigateTo(R.id.secondFragment, "user_name" to "Henry", "age" to 25)
 * ```
 *
 * @param destinationId 目标 destination 的 id
 * @param params 参数键值对，支持 Int/Long/Float/Double/Boolean/String/CharSequence/Bundle/Parcelable/Serializable
 */
fun Fragment.navigateTo(@IdRes destinationId: Int, vararg params: Pair<String, Any>) {
    findNavController().navigate(destinationId, params.toBundle())
}

/**
 * 返回上一页
 *
 * @return 是否成功弹出
 */
fun Fragment.popBack(): Boolean = findNavController().popBackStack()

/**
 * 返回到指定 destination
 *
 * 使用示例：
 * ```kotlin
 * popTo(R.id.firstFragment)          // 返回到 firstFragment，firstFragment 保留在栈中
 * popTo(R.id.firstFragment, true)    // inclusive=true 时 firstFragment 也被弹出
 * ```
 *
 * @param destinationId 目标 destination 的 id
 * @param inclusive 是否包含目标 destination 本身
 * @return 是否成功弹出
 */
fun Fragment.popTo(@IdRes destinationId: Int, inclusive: Boolean = false): Boolean =
    findNavController().popBackStack(destinationId, inclusive)

/**
 * 返回到导航图根节点（startDestination）
 *
 * @return 是否成功弹出
 */
fun Fragment.popToRoot(): Boolean =
    findNavController().popBackStack(findNavController().graph.startDestinationId, false)

/**
 * 获取导航图作用域的 ViewModel（同一导航图内的所有 destination 共享同一实例）
 *
 * 使用示例：
 * ```kotlin
 * private val sharedViewModel by lazy {
 *     obtainNavGraphViewModel(SharedViewModel::class.java, R.id.nav_graph)
 * }
 * ```
 *
 * @param vmClass ViewModel 类型
 * @param navGraphId 导航图 ID（对应 nav_graph.xml 中 <navigation> 的 id）
 */
fun <T : ViewModel> Fragment.obtainNavGraphViewModel(vmClass: Class<T>, @IdRes navGraphId: Int): T {
    val entry = findNavController().getBackStackEntry(navGraphId)
    return ViewModelProvider(
        entry.viewModelStore,
        entry.defaultViewModelProviderFactory,
        entry.defaultViewModelCreationExtras
    )[vmClass]
}

/**
 * 设置结果并返回上一页（替代 startActivityForResult，适用于单 Activity + Navigation 场景）
 *
 * 使用示例（SecondFragment 中）：
 * ```kotlin
 * setNavResult("result_key", "返回的数据")
 * ```
 *
 * @param key 结果键
 * @param value 结果值
 */
fun <T> Fragment.setNavResult(key: String, value: T) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, value)
    findNavController().popBackStack()
}

/**
 * 设置结果到指定 destination 并返回到该页面（跨层返回，如 C 直接 popTo 回 A）
 *
 * 与 [setNavResult] 的区别：前者只能把结果传给「紧邻的上一页」；
 * 本方法通过 [androidx.navigation.NavController.getBackStackEntry] 显式定位目标 destination，
 * 可跨越中间层（如 A → B → C，从 C 直接携带参数回到 A）。
 *
 * 使用示例（C 中）：
 * ```kotlin
 * setNavResultTo(R.id.fragmentA, "result_key", "带回 A 的数据")
 * ```
 *
 * 接收方（A）与 [observeNavResult] 用法完全一致。
 *
 * @param destinationId 目标 destination 的 id
 * @param key 结果键
 * @param value 结果值
 * @param inclusive 是否连同目标 destination 一起弹出（默认 false，目标保留）
 */
fun <T> Fragment.setNavResultTo(
    @IdRes destinationId: Int,
    key: String,
    value: T,
    inclusive: Boolean = false
) {
    findNavController().getBackStackEntry(destinationId).savedStateHandle[key] = value
    findNavController().popBackStack(destinationId, inclusive)
}

/**
 * 观察返回结果（配合 [setNavResult] / [setNavResultTo] 使用）
 *
 * 一次性消费：收到后自动清理，避免重复触发。
 * 观察的是当前 destination 自身的 SavedStateHandle，因此无论结果来自「上一页返回」还是「跨层返回」，
 * 接收方用法完全一致。
 *
 * 使用示例（接收方，如 FirstFragment 中）：
 * ```kotlin
 * observeNavResult<String>("result_key") { value ->
 *     value?.let { /* 处理返回结果 */ }
 * }
 * ```
 *
 * @param key 结果键（与发送方 setNavResult / setNavResultTo 的 key 一致）
 * @param observer 结果回调，无结果时为 null
 */
fun <T> Fragment.observeNavResult(key: String, observer: (T?) -> Unit) {
    findNavController().currentBackStackEntry?.savedStateHandle?.let { handle ->
        handle.getLiveData<T>(key).observe(viewLifecycleOwner) { value ->
            observer(value)
            handle.remove<T>(key)
        }
    }
}

/**
 * 将键值对转换为 Bundle，按值类型调用对应的 put 方法
 */
private fun Array<out Pair<String, Any>>.toBundle(): Bundle {
    return Bundle(size).apply {
        for ((key, value) in this@toBundle) {
            when (value) {
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is Float -> putFloat(key, value)
                is Double -> putDouble(key, value)
                is Boolean -> putBoolean(key, value)
                is String -> putString(key, value)
                is CharSequence -> putCharSequence(key, value)
                is Bundle -> putBundle(key, value)
                is Parcelable -> putParcelable(key, value)
                is Serializable -> putSerializable(key, value)
                else -> throw IllegalArgumentException(
                    "不支持的参数类型：${value::class.java.name}"
                )
            }
        }
    }
}
