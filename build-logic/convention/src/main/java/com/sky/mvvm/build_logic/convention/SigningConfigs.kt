package com.sky.mvvm.build_logic.convention

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project

internal fun Project.applySigningConfigs(
    applicationExtension: ApplicationExtension,
) {
    applicationExtension.apply {
        signingConfigs {
            getByName("debug") {
                storeFile = file(AppConfig.storeFile)
                storePassword = AppConfig.storePassword
                keyAlias = AppConfig.keyAlias
                keyPassword = AppConfig.storePassword
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
                    abiFilters.addAll(arrayOf("arm64-v8a", "x86"))
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
                    abiFilters.addAll(arrayOf("arm64-v8a", "armeabi-v7a"))
                }
            }
        }
    }
}