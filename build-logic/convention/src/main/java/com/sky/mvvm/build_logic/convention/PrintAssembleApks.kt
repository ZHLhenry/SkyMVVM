package com.sky.mvvm.build_logic.convention

import com.android.build.gradle.AppExtension
import org.gradle.api.Project
import java.io.File
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

internal fun Project.configurePrintAssembleApksTask(extension: AppExtension) {
    val apkPaths = mutableListOf<String>()
    var parentDir: File? = null
    extension.applicationVariants.all {
        val variant = this
        this.outputs.all {
            // 获取应用相关信息
            val appName = AppConfig.appName
            val flavorName = variant.flavorName ?: ""
            val buildTypeName = variant.buildType.name
            val versionName = variant.versionName ?: AppConfig.versionName
            val versionCode = variant.versionCode
            outputFile?.let { originalFile ->
                if (originalFile.name.endsWith(".apk")) {
                    // 生成新的文件名
                    val newFileName =
                        "${appName}_${flavorName}_${buildTypeName}_v${versionName}_${versionCode}_${getApkBuildTime()}.apk"
                    // 构建新文件路径
                    this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
                    outputFileName = newFileName
                    // 保存路径以供打印
                    apkPaths.add(outputFile.absolutePath)
                }
            }

            outputFile.parentFile.takeIf { it.exists() && buildTypeName == "release" }
                ?.let {
                    parentDir = it
                }

            val path =
                "> PrintTask APK output path for $flavorName: file: open ${outputFile.absolutePath}"
            apkPaths.add(path)
        }
    }

    tasks.register("openLatestApkInFinder") {
        doLast {
            parentDir?.let { PrintAssembleApksUtil().openFile(it) }
        }
    }

    tasks.matching { it.name.startsWith("assemble") }.configureEach {
        finalizedBy("openLatestApkInFinder")
    }

}

internal fun getApkBuildTime(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
    val time = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"))
    return time.format(formatter)
}

@Suppress("DEPRECATION")
internal class PrintAssembleApksUtil {

    private fun isMac(): Boolean {
        return System.getProperty("os.name").startsWith("Mac")
    }

    private fun isWindows(): Boolean {
        return System.getProperty("os.name").startsWith("Window")
    }

    fun openFile(file: File) {
        try {
            if (isMac()) {
                println("----------------${file}-------------------")
                Runtime.getRuntime().exec("open $file")
            } else {
                val cmd = "explorer /select, " + file.absolutePath
                Runtime.getRuntime().exec(cmd)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}