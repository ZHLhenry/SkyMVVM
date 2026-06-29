package com.sky.mvvm.build_logic.convention

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.BuiltArtifactsLoader
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

internal fun Project.configurePrintAssembleApksTask(extension: ApplicationAndroidComponentsExtension) {
    extension.onVariants { variant ->
        val flavorName = variant.flavorName ?: ""
        val buildTypeName = variant.buildType ?: ""

        val apkFolder = variant.artifacts.get(SingleArtifact.APK)
        val loader = variant.artifacts.getBuiltArtifactsLoader()

        val taskName = "renameAndOpen${variant.name.replaceFirstChar { it.uppercase() }}Apk"
        tasks.register(taskName, RenameAndOpenApkTask::class.java) {
            this.apkDirectory.set(apkFolder)
            this.builtArtifactsLoader.set(loader)
            this.appName.set(AppConfig.appName)
            this.flavorName.set(flavorName)
            this.buildTypeName.set(buildTypeName)
            this.openInFinder.set(buildTypeName == "release")
        }

        tasks.matching { it.name == "assemble${variant.name.replaceFirstChar { it.uppercase() }}" }
            .configureEach {
                finalizedBy(taskName)
            }
    }
}

internal abstract class RenameAndOpenApkTask : DefaultTask() {

    @get:InputDirectory
    abstract val apkDirectory: DirectoryProperty

    @get:Internal
    abstract val builtArtifactsLoader: Property<BuiltArtifactsLoader>

    @get:Input
    abstract val appName: Property<String>

    @get:Input
    abstract val flavorName: Property<String>

    @get:Input
    abstract val buildTypeName: Property<String>

    @get:Input
    abstract val openInFinder: Property<Boolean>

    @TaskAction
    fun taskAction() {
        val dir = apkDirectory.get()
        val builtArtifacts = builtArtifactsLoader.get().load(dir) ?: return

        builtArtifacts.elements.forEach { artifact ->
            val originalFile = File(artifact.outputFile)
            if (originalFile.name.endsWith(".apk")) {
                val versionName = artifact.versionName ?: AppConfig.versionName
                val versionCode = artifact.versionCode ?: AppConfig.versionCode
                val newFileName =
                    "${appName.get()}_${flavorName.get()}_${buildTypeName.get()}_v${versionName}_${versionCode}_${getApkBuildTime()}.apk"
                val renamedFile = File(originalFile.parentFile, newFileName)
                // 复制一份，原始 app-dev-debug.apk 保留
                originalFile.copyTo(renamedFile, overwrite = true)
                // 重命名会移走原始文件
                // originalFile.renameTo(renamedFile)
                println("> Copied APK: ${renamedFile.absolutePath}")
            }
        }

        if (openInFinder.get() && dir.asFile.exists()) {
            PrintAssembleApksUtil.openFile(dir.asFile)
        }
    }
}

internal fun getApkBuildTime(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
    val time = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"))
    return time.format(formatter)
}

internal object PrintAssembleApksUtil {

    private fun isMac(): Boolean {
        return System.getProperty("os.name").startsWith("Mac")
    }

    private fun isWindows(): Boolean {
        return System.getProperty("os.name").startsWith("Window")
    }

    fun openFile(file: File) {
        try {
            if (isMac()) {
                println("----------------$file-------------------")
                ProcessBuilder("open", file.absolutePath).start()
            } else {
                ProcessBuilder("explorer", "/select,", file.absolutePath).start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}