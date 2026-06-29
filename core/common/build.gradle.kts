import com.sky.mvvm.build_logic.convention.AppConfig

plugins {
    alias(libs.plugins.sky.android.library.common)
    alias(libs.plugins.sky.android.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.sky.mvvm.core.common"
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
    //recyclerview
    api("androidx.recyclerview:recyclerview:1.3.2")
    //dialog
    api("com.afollestad.material-dialogs:lifecycle:3.3.0")
    //BaseAdapter
    api("io.github.cymchad:BaseRecyclerViewAdapterHelper4:4.1.6")
    // 沉浸式
    api("com.geyifeng.immersionbar:immersionbar:3.2.2")
    api("com.geyifeng.immersionbar:immersionbar-ktx:3.2.2")
    //数据存储
    api("com.tencent:mmkv:2.1.0")
    //refresh
    api("io.github.scwang90:refresh-layout-kernel:3.0.0-alpha")
    api("io.github.scwang90:refresh-header-classics:3.0.0-alpha")
    api("io.github.scwang90:refresh-footer-classics:3.0.0-alpha")
    //Toaster
    api("com.github.getActivity:Toaster:12.8")
    //TitleBar
    api("com.github.getActivity:TitleBar:10.6")
    //Permissions
    api("com.github.getActivity:XXPermissions:20.0")
    //Log
    api("com.elvishew:xlog:1.11.1")
    //Chucker
    devApi(libs.okhttp.chucker)
    uatApi(libs.okhttp.chucker.release)
    prodApi(libs.okhttp.chucker.release)
    //Moshi
    api(libs.moshi)
    ksp(libs.moshi.codegen)
    api(libs.moshi.converter)

//    api(project(":SkyMVVMLib"))
    api(libs.skymvvm)
    api(libs.skymultistatelayout)

    implementation(projects.core.model)
}