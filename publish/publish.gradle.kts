plugins {
    `maven-publish`
    signing
    id("org.jetbrains.dokka")
}

tasks.register<Jar>("androidJavadocJar") {
    dependsOn(tasks.named("dokkaJavadoc"))
    archiveClassifier.set("javadoc")
    from(tasks.named("dokkaJavadoc").map { it.outputs.files })
}

tasks.register<Jar>("androidSourcesJar") {
    archiveClassifier.set("sources")
    from(android.sourceSets["main"].java.srcDirs)
    from(android.sourceSets["main"].kotlin.srcDirs)
}

tasks.withType<org.jetbrains.dokka.gradle.DokkaTaskPartial>().configureEach {
    pluginsMapConfiguration.set(
        mapOf("org.jetbrains.dokka.base.DokkaBase" to """{ "separateInheritedMembers": true}""")
    )
}

android {
    publishing {
        singleVariant("release") {
            withJavadocJar()
            withSourcesJar()
        }
    }
}

signing {
    sign(configurations.archives.get())
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                groupId = "io.nerdythings"
                artifactId = project.findProperty("artifactId") as String
                version = project.findProperty("releaseVersion") as String

                from(components["release"])

                pom {
                    name.set(project.findProperty("artifactId") as String)
                    description.set(project.findProperty("libraryDescription") as String)
                    url.set("https://nerdythings.io/")
                    licenses {
                        license {
                            name.set("The Apache Software License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("gektor650")
                            name.set("Eugene Tkachenko")
                            email.set("gektor650@gmail.com")
                        }
                        developer {
                            id.set("sunhumann2010")
                            name.set("Hanna Tkachenko")
                            email.set("sunhumann2010@gmail.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:github.com/itkacher/OkHttpProfiler.git")
                        developerConnection.set("scm:git:ssh://github.com/itkacher/OkHttpProfiler.git")
                        url.set("https://nerdythings.io/")
                    }
                }
            }
        }
        repositories {
            maven {
                url = uri("${rootDir}/releases/${project.findProperty("artifactId")}")
            }
        }
    }
}