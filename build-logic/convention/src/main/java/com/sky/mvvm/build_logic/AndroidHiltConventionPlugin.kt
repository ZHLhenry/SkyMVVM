import com.sky.mvvm.build_logic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.devtools.ksp")
                apply("dagger.hilt.android.plugin")
            }

            dependencies {
                "implementation"(libs.findLibrary("hilt-android").get())
                "ksp"(libs.findLibrary("hilt-compiler").get())
                "kspAndroidTest"(libs.findLibrary("hilt-compiler").get())
                "kspTest"(libs.findLibrary("hilt-compiler").get())
                // Hilt Gradle 插件在 KSP 场景下仍会向 javac 注入一些仅用于 KAPT 的内部选项，
                // 而 javac 端没有 Hilt 处理器，导致“以下选项未被任何处理程序识别”的警告。
                // 引入一个无操作处理器，仅用于声明识别这些选项以消除警告。
                "annotationProcessor"(project(":hilt-noop-processor"))
            }
        }
    }

}