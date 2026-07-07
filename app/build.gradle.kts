plugins {
    alias(libs.plugins.sky.android.application)
    alias(libs.plugins.sky.android.application.flavors)
    alias(libs.plugins.sky.android.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.sky.mvvm.sample"
    flavorDimensions += "contentType"
    productFlavors {
        create("dev") {
            dimension = "contentType"
            manifestPlaceholders["app_icon"] = "@mipmap/ic_launcher"
        }
        create("uat") {
            dimension = "contentType"
            manifestPlaceholders["app_icon"] = "@mipmap/ic_launcher"
        }
        create("prod") {
            dimension = "contentType"
            manifestPlaceholders["app_icon"] = "@mipmap/ic_launcher"
        }
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
}