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
        maven {
            isAllowInsecureProtocol = true
            credentials {
                username = "read_henry001"
                password = "read_henry001"
            }
            url = uri("http://47.122.117.117:9001/repository/maven-releases/")
        }
        maven("${rootDir}/build/repo")
    }
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "SkyMVVM"
include(":app")
include(":SkyMVVMLib")
