import com.sky.mvvm.build_logic.convention.AppConfig

plugins {
    alias(libs.plugins.sky.android.application)
    alias(libs.plugins.sky.android.application.flavors)
    alias(libs.plugins.sky.android.hilt)
    id("kotlin-kapt")
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // 沉浸式
    implementation("com.geyifeng.immersionbar:immersionbar:3.2.2")
    implementation("com.geyifeng.immersionbar:immersionbar-ktx:3.2.2")
    //数据存储
    implementation("com.tencent:mmkv:2.1.0")
    //Permissions
    implementation("com.github.getActivity:XXPermissions:20.0")
    //refresh
    implementation("io.github.scwang90:refresh-layout-kernel:3.0.0-alpha")      //核心必须依赖
    implementation("io.github.scwang90:refresh-header-classics:3.0.0-alpha")    //经典刷新头
    implementation("io.github.scwang90:refresh-footer-classics:3.0.0-alpha")    //经典加载


//    implementation(project(":SkyMVVMLib"))
    implementation(libs.skymvvm)
}