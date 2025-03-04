import com.sky.mvvm.build_logic.convention.AppConfig

plugins {
//    alias(libs.plugins.sky.android.feature)
    alias(libs.plugins.sky.android.library.common)
    alias(libs.plugins.sky.android.hilt)
}

android {
    namespace = "com.sky.mvvm.feature.home"
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

