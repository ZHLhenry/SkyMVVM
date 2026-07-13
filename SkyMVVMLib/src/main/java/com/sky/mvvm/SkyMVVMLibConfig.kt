package com.sky.mvvm

import android.content.Context
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.printer.Printer
import okhttp3.Interceptor

/**
 * @Class: SkyMVVMLibConfig
 * @Author: Henry
 * @Date: 2025/6/30
 * @Description: SkyMVVM库配置类，支持XLog日志、SkyFlow事件流、OkHttp日志等模块配置
 */

class SkyMVVMLibConfig private constructor(
    var context: Context,
    var xLogLibEnabled: Boolean,
    var skyFlowLibEnabled: Boolean,
    var xLogConfig: LogConfiguration?,
    var xLogPrinter: Array<out Printer>,
    var okHttpLogLibEnabled: Boolean,
    var okHttpLogConfig: Interceptor?
) {

    class Builder(private val context: Context) {
        private val TAG = "SkyMVVMLibConfig"
        private var xLogLibEnabled = false
        private var skyFlowLibEnabled = false
        private var xLogConfig: LogConfiguration? = null
        private var xLogPrinter: Array<out Printer> = emptyArray()
        private var okHttpLogLibEnabled = false
        private var okHttpLogConfig: Interceptor? = null

        /**
         * 启用 XLog 日志模块。
         *
         * 注意：请在业务模块中添加 XLog 依赖。
         * @param enableXLogLib 是否启用XLog 默认false
         * @param xLogConfig XLog 配置，传 null 则使用内置默认配置（LogLevel.ALL）
         * @param printers 自定义打印类，不传则默认使用AndroidPrinter
         *
         * @see <a href="https://github.com/elvishew/xLog/blob/master/README_ZH.md">XLog 文档</a>
         */
        fun enableXLog(enableXLogLib: Boolean = false, xLogConfig: LogConfiguration? = null, vararg printers: Printer): Builder {
            this.xLogLibEnabled = enableXLogLib
            this.xLogConfig = xLogConfig
            this.xLogPrinter = if (printers.isNotEmpty()) printers else emptyArray()
            return this
        }

        /**
         * 启用 SkyFlow 模块
         *
         * @param enableSkyFlowLib 是否启用SkyFlow 默认false
         *
         * @see [SkyFlow]
         */
        fun enableSkyFlow(enableSkyFlowLib: Boolean = false): Builder {
            this.skyFlowLibEnabled = enableSkyFlowLib
            return this
        }

        /**
         * 启用 OkHttp 日志模块
         *
         * @param enableOkHttpLogLib 是否启用日志拦截器，默认 false
         * @param okHttpLogConfig 自定义日志拦截器配置，传 null 则使用内置默认配置
         *
         * @see <p>使用示例:</br>AndroidLoggingInterceptor.build()</p>
         *
         */
        fun enableOkHttpLogLib(enableOkHttpLogLib: Boolean = false, okHttpLogConfig: Interceptor? = null): Builder {
            this.okHttpLogLibEnabled = enableOkHttpLogLib
            this.okHttpLogConfig = okHttpLogConfig
            return this
        }

        fun build(): SkyMVVMLibConfig {
            return SkyMVVMLibConfig(
                context = context.applicationContext,
                xLogLibEnabled = xLogLibEnabled,
                xLogConfig = xLogConfig,
                xLogPrinter = xLogPrinter,
                skyFlowLibEnabled = skyFlowLibEnabled,
                okHttpLogLibEnabled = okHttpLogLibEnabled,
                okHttpLogConfig = okHttpLogConfig
            )
        }
    }
}
