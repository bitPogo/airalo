package com.airalo.example.gradle.convention.quality.dependency

import com.airalo.example.gradle.config.BuildConfig
import com.autonomousapps.DependencyAnalysisExtension
import org.gradle.api.Project

internal object DependencyHealth : DependencyConventionContract {
    private fun Project.configure() {
        extensions.getByType(DependencyAnalysisExtension::class.java).apply {
            issues {
                all {
                    onUnusedDependencies {
                        severity("fail")
                        exclude(
                            "org.jetbrains.kotlin:kotlin-stdlib",
                            "org.junit.jupiter:junit-jupiter-api",
                            "io.insert-koin:koin-core",
                            "io.insert-koin:koin-annotations",
                        )
                    }
                    onIncorrectConfiguration {
                        severity("warn")
                    }
                    onUsedTransitiveDependencies {
                        severity("ignore")
                    }
                    onRedundantPlugins {
                        severity("fail")
                        exclude(
                            "org.jetbrains.kotlin.jvm",
                            "java-library",
                        )
                    }
                    onCompileOnly {
                        severity("fail")
                    }
                    onRuntimeOnly {
                        severity("fail")
                    }
                    onUnusedAnnotationProcessors {
                        severity("fail")
                    }
                }
            }
        }
    }

    override fun apply(project: Project) {
        project.plugins.apply(BuildConfig.dependencyHealthId)
        project.configure()

        project.subprojects {
            plugins.apply(BuildConfig.dependencyHealthId)
        }
    }
}
