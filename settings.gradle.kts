pluginManagement {
    repositories {
        maven {
            url = uri("https://maven.aliyun.com/repository/google")
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/central")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/gradle-plugin")
        }
        maven {
            credentials {
                username = "677b6d7687eb3ab8bcc7ac20"
                password = "Tqmt4VtWBTp)"
            }
            url = uri("https://packages.aliyun.com/6732fc8f356ccaf8531a1487/maven/skybuildlogic")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven {
            url = uri("https://jitpack.io")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/google")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/central")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/public")
        }
        /** aliyun **/
        fun aliyunMaven(repoUrl: String) {
            maven {
                credentials {
                    username = "677b4e5b259532263f6b30a6"
                    password = "RnVrdxoghjKo"
                }
                url = uri(repoUrl)
            }
        }
        aliyunMaven("https://packages.aliyun.com/6732fc8f356ccaf8531a1487/maven/skymvvm")
        aliyunMaven("https://packages.aliyun.com/6732fc8f356ccaf8531a1487/maven/skymultistatelayout")
        aliyunMaven("https://packages.aliyun.com/6732fc8f356ccaf8531a1487/maven/skybuildlogic")
        aliyunMaven("https://packages.aliyun.com/6732fc8f356ccaf8531a1487/maven/skywidget")
        maven("${rootDir}/build/repo")

    }
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "SkyMVVM"
include(":app")
include(":SkyMVVMLib")
include(":core:common")
include(":core:model")