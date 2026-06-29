import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import java.io.FileInputStream
import java.util.Properties

class AndroidMavenPublishConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("maven-publish")

            val localProperties = Properties().apply {
                val localPropertiesFile = rootProject.file("local.properties")
                if (localPropertiesFile.exists()) {
                    load(FileInputStream(localPropertiesFile))
                }
            }

            // 循环打印输出 localProperties 的信息
            localProperties.forEach { (key, value) ->
                println("Property: $key = $value")
            }

            val mavenCentralUserName = localProperties.getProperty("mavenCentral.username")
            val mavenCentralPassword = localProperties.getProperty("mavenCentral.password")
            val mavenCentralGroupId = localProperties.getProperty("mavenCentral.groupId")
            val mavenCentralArtifactId = localProperties.getProperty("mavenCentral.artifactId")
            val mavenCentralVersion = localProperties.getProperty("mavenCentral.version")
            val mavenCentralRepoUrl = localProperties.getProperty("mavenCentral.repoUrl")

            val android = extensions.getByType(LibraryExtension::class.java)

            val generateSourcesJar = tasks.register("generateSourcesJar", Jar::class.java) {
                archiveClassifier.set("sources")
                from(android.sourceSets["main"].java.directories)
            }

            val generateJavadocJar = tasks.register("generateJavadocJar", Jar::class.java) {
                archiveClassifier.set("javadoc")
                from(android.sourceSets["main"].java.directories)
            }

            afterEvaluate {
                extensions.configure<PublishingExtension> {
                    publications {
                        create("release", MavenPublication::class.java) {
                            groupId = mavenCentralGroupId
                            artifactId = mavenCentralArtifactId
                            version = mavenCentralVersion

                            artifact(tasks.getByName("bundleReleaseAar"))
                            artifact(generateSourcesJar)
                            artifact(generateJavadocJar)

                            pom.withXml {
                                val dependenciesNode = asNode().appendNode("dependencies")
                                configurations["implementation"].allDependencies.forEach { dependency ->
                                    if (dependency.version != "unspecified" && dependency.name != "unspecified") {
                                        val dependencyNode = dependenciesNode.appendNode("dependency")
                                        dependencyNode.appendNode("groupId", dependency.group)
                                        dependencyNode.appendNode("artifactId", dependency.name)
                                        dependencyNode.appendNode("version", dependency.version)
                                    }
                                }
                            }
                        }
                    }

                    repositories {
                        maven {
                            isAllowInsecureProtocol = true
                            url = uri(mavenCentralRepoUrl)
                            credentials {
                                username = mavenCentralUserName
                                password = mavenCentralPassword
                            }
                        }
                    }
                }
            }
        }
    }
}
