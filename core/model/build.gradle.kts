plugins {
    alias(libs.plugins.sky.android.library.common)
    alias(libs.plugins.sky.android.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.sky.mvvm.core.model"
}
dependencies {
    implementation(libs.moshi)
    ksp(libs.moshi.codegen)
}


