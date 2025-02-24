package com.sky.mvvm.build_logic.convention

object AppConfig {
    /** 项目构建版本信息 **/
    const val minSdk = 28
    const val targetSdk = 35
    const val compileSdk = 35

    /** app版本信息 **/
    const val appName = "SkyMVVM"
    const val versionCode = 100
    const val versionName = "1.0.0"

    /** jks信息 **/
    const val storeFile = "../SkyMVVM.jks"
    const val storePassword = "skymvvm123456"
    const val keyAlias = "SkyMVVM"
    const val applicationId = "com.sky.mvvm.sample"

    /** 其他配置 **/
    const val enableViewBinding = true
    const val enableDataBinding = true
    const val enableBuildConfig = true

}