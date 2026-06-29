@file:Suppress("DEPRECATION")

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.sky.mvvm.build.logic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "sky.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "sky.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidCommonLibrary") {
            id = "sky.android.library.common"
            implementationClass = "AndroidCommonLibraryConventionPlugin"
        }
        register("androidHilt") {
            id = "sky.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidPublish") {
            id = "sky.android.publish"
            implementationClass = "AndroidMavenPublishConventionPlugin"
        }
        register("androidFlavors") {
            id = "sky.android.application.flavors"
            implementationClass = "AndroidApplicationFlavorsConventionPlugin"
        }
        register("jvmLibrary") {
            id = "sky.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}