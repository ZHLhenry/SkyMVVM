package com.sky.mvvm.build_logic.convention

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project
import java.util.Properties

internal fun Project.applySigningConfigs(
    applicationExtension: ApplicationExtension,
) {
    val localProperties = Properties().apply {
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            load(localPropertiesFile.inputStream())
        }
    }

    val jksStoreFile = localProperties.getProperty("app.storeFile", "../SkyMVVM.jks")
    val jksStorePassword = localProperties.getProperty("app.storePassword", "")
    val jksKeyAlias = localProperties.getProperty("app.keyAlias", "")

    applicationExtension.apply {
        signingConfigs {
            getByName("debug") {
                storeFile = file(jksStoreFile)
                storePassword = jksStorePassword
                keyAlias = jksKeyAlias
                keyPassword = jksStorePassword
            }
        }

        defaultConfig {
            applicationId = AppConfig.applicationId
            versionCode = AppConfig.versionCode
            versionName = AppConfig.versionName
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            vectorDrawables {
                useSupportLibrary = true
            }
        }

        buildTypes {
            getByName("debug") {
                applicationIdSuffix = AppBuildType.DEBUG.applicationIdSuffix
                isMinifyEnabled = false
                signingConfig = signingConfigs.getByName("debug")
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
                ndk {
                    abiFilters.addAll(arrayOf("arm64-v8a", "armeabi-v7a", "x86", "x86_64"))
                }
            }
            getByName("release") {
                applicationIdSuffix = AppBuildType.RELEASE.applicationIdSuffix
                isMinifyEnabled = false
                signingConfig = signingConfigs.getByName("debug")
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
                ndk {
                    abiFilters.addAll(arrayOf("arm64-v8a", "armeabi-v7a", "x86", "x86_64"))
                }
            }
        }
    }
}
