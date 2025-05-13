package com.airalo.example.gradle.convention.toolchain

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainService
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal abstract class ToolChainSekelton {
    protected fun configure(
        project: Project,
        compileTargetSuffix: String,
        version: JavaLanguageVersion,
    ) {
        project.extensions.getByType(JavaPluginExtension::class.java).apply {
            toolchain.languageVersion.set(version)
        }

        project.tasks.withType(KotlinCompile::class.java) {
            if (name.endsWith("Kotlin") || name.endsWith("Kotlin$compileTargetSuffix")) {
                val launcher = project.extensions.getByType(JavaToolchainService::class.java).launcherFor {
                    languageVersion.set(version)
                }

                this.kotlinJavaToolchain.toolchain.use(launcher)
            }
        }
    }
}
