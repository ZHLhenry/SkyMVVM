import com.sky.mvvm.build_logic.convention.AppConfig

plugins {
    alias(libs.plugins.sky.android.application)
    alias(libs.plugins.sky.android.application.flavors)
    alias(libs.plugins.sky.android.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.sky.mvvm.sample"
    buildFeatures {
        dataBinding = AppConfig.enableDataBinding
        viewBinding = AppConfig.enableViewBinding
        buildConfig = AppConfig.enableBuildConfig
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
}