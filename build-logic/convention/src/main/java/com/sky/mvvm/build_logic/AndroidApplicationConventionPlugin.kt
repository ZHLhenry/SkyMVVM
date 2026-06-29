import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.sky.mvvm.build_logic.convention.AppConfig
import com.sky.mvvm.build_logic.convention.applySigningConfigs
import com.sky.mvvm.build_logic.convention.configureKotlinAndroid
import com.sky.mvvm.build_logic.convention.configurePrintApksTask
import com.sky.mvvm.build_logic.convention.configurePrintAssembleApksTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
            }

            extensions.configure<ApplicationExtension> {
                applySigningConfigs(this)
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = AppConfig.targetSdk
                defaultConfig.minSdk = AppConfig.minSdk
                compileSdk = AppConfig.compileSdk
            }
            extensions.configure<ApplicationAndroidComponentsExtension> {
                configurePrintApksTask(this)
                configurePrintAssembleApksTask(this)
            }
        }
    }

}