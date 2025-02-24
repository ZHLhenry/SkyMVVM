import com.android.build.gradle.LibraryExtension
import com.sky.mvvm.build_logic.convention.AppConfig
import com.sky.mvvm.build_logic.convention.AppFlavor
import com.sky.mvvm.build_logic.convention.configureFlavors
import com.sky.mvvm.build_logic.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidCommonLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = AppConfig.targetSdk
                configureFlavors(this) { flavor ->
                    buildConfigField(
                        "Boolean",
                        "isProd",
                        "${flavor == AppFlavor.prod}"
                    )
                    buildConfigField(
                        "String",
                        "versionName",
                        "\"${AppConfig.versionName}\""
                    )
                }
            }
            dependencies {
                add("testImplementation", kotlin("test"))
                add("androidTestImplementation", kotlin("test"))
            }
        }
    }
}