pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("${rootDir}/build/repo")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
        /** nexus **/
        maven {
            isAllowInsecureProtocol = true
            credentials {
                username = "read_henry001"
                password = "read_henry001"
            }
            url = uri("http://www.zhouhengli.online:9001/repository/maven-releases/")
        }

        /** aliyun **/
        maven {
            url = uri("https://maven.aliyun.com/repository/public")
        }
        maven {
            credentials {
                username = "677b4e5b259532263f6b30a6"
                password = "RnVrdxoghjKo"
            }
            url = uri("https://packages.aliyun.com/6732fc8f356ccaf8531a1487/maven/skymvvm")
        }
        maven("${rootDir}/build/repo")
    }
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "SkyMVVM"
include(":app")
include(":SkyMVVMLib")
include(":core:common")
include(":core:model")