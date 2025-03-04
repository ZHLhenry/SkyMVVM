import com.sky.mvvm.build_logic.convention.AppConfig
plugins {
    alias(libs.plugins.sky.android.library.common)
    alias(libs.plugins.sky.android.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.sky.mvvm.core.model"
    buildFeatures {
        dataBinding = AppConfig.enableDataBinding
        viewBinding = AppConfig.enableViewBinding
        buildConfig = AppConfig.enableBuildConfig
    }
}
dependencies {
    implementation(libs.moshi)
    ksp(libs.moshi.codegen)
}


