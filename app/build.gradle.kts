plugins {
    alias(libs.plugins.sky.android.application)
    alias(libs.plugins.sky.android.application.flavors)
    alias(libs.plugins.sky.android.hilt)
}

android {
    namespace = "com.sky.mvvm.sample"
    flavorDimensions += "contentType"
    productFlavors {
        create("dev") {
            dimension = "contentType"
            manifestPlaceholders["app_icon"] = "@mipmap/ic_skymvvm_debug"
        }
        create("uat") {
            dimension = "contentType"
            manifestPlaceholders["app_icon"] = "@mipmap/ic_skymvvm"
        }
        create("prod") {
            dimension = "contentType"
            manifestPlaceholders["app_icon"] = "@mipmap/ic_skymvvm"
        }
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
}