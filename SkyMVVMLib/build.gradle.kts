apply(from = "../uploadArchive.gradle")
plugins {
    alias(libs.plugins.sky.android.library)
    alias(libs.plugins.sky.android.hilt)
    id("kotlin-kapt")
    id("maven-publish")
}

android {
    namespace = "com.sky.mvvm"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    //lifecycle
    api(libs.androidx.lifecycle.process)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.lifecycle.lifecycle.common.java8)
    // viewModel
    api(libs.androidx.lifecycle.lifecycle.viewmodel.ktx)
    // liveData
    api(libs.androidx.lifecycle.lifecycle.livedata.ktx)

    //retrofit
    api(libs.retrofit2.retrofit)
    api(libs.persistentcookiejar)
    api(libs.androidx.databinding.runtime)

    //gson
    api(libs.gsonfactory)
    api(libs.gson)

    //hilt
    api(libs.hilt.android)
    api(libs.hilt.compiler)
}

