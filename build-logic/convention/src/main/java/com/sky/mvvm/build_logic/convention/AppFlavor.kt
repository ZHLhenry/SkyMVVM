package com.sky.mvvm.build_logic.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.ProductFlavor
import com.android.build.gradle.internal.dsl.InternalCommonExtension

@Suppress("EnumEntryName")
enum class FlavorDimension {
    contentType
}

// The content for the app can either come from local static data which is useful for demo
// purposes, or from a production backend server which supplies up-to-date, real content.
// These two product flavors reflect this behaviour.
@Suppress("EnumEntryName")
enum class AppFlavor(
    val dimension: FlavorDimension,
    val applicationIdSuffix: String? = null,
    val appIcon: String? = null
) {
    dev(FlavorDimension.contentType, appIcon = "@mipmap/ic_launcher"),
    uat(FlavorDimension.contentType, appIcon = "@mipmap/ic_launcher"),
    prod(FlavorDimension.contentType, appIcon = "@mipmap/ic_launcher")
}

fun configureFlavors(
    commonExtension: ApplicationExtension,
    flavorConfigurationBlock: ProductFlavor.(flavor: AppFlavor) -> Unit = {},
) {
    (commonExtension as InternalCommonExtension).setFlavorDimensions(
        mutableListOf(FlavorDimension.contentType.name))
    commonExtension.apply {
        productFlavors {
            AppFlavor.entries.forEach { flavor ->
                create(flavor.name) {
                    flavor.appIcon?.let { appIcon ->
                        manifestPlaceholders["app_icon"] = appIcon
                    }
                    dimension = flavor.dimension.name
                    flavorConfigurationBlock(this, flavor)
                    if (flavor.applicationIdSuffix != null) {
                        applicationIdSuffix = flavor.applicationIdSuffix
                    }
                }
            }
        }
    }
}

fun configureFlavors(
    commonExtension: LibraryExtension,
    flavorConfigurationBlock: ProductFlavor.(flavor: AppFlavor) -> Unit = {},
) {
    (commonExtension as InternalCommonExtension).setFlavorDimensions(
        mutableListOf(FlavorDimension.contentType.name))
    commonExtension.apply {
        productFlavors {
            AppFlavor.entries.forEach { flavor ->
                create(flavor.name) {
                    flavor.appIcon?.let { appIcon ->
                        manifestPlaceholders["app_icon"] = appIcon
                    }
                    dimension = flavor.dimension.name
                    flavorConfigurationBlock(this, flavor)
                }
            }
        }
    }
}