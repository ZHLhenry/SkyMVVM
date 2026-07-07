plugins {
    alias(libs.plugins.kotlin.parcelize).apply(false)
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.org.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
}

buildscript {
    dependencies {
        classpath(libs.android.gradlePlugin)
    }
}

// skyBuild 配置
extra["skyBuild.appName"] = "SkyMVVM"
extra["skyBuild.applicationId"] = "com.sky.mvvm.sample"
extra["skyBuild.versionCode"] = 100
extra["skyBuild.versionName"] = "1.0.0"
extra["skyBuild.compileSdk"] = 36
extra["skyBuild.minSdk"] = 28
extra["skyBuild.targetSdk"] = 35
extra["skyBuild.enableViewBinding"] = true
extra["skyBuild.enableDataBinding"] = true
extra["skyBuild.enableBuildConfig"] = true
extra["skyBuild.enableCompose"] = false