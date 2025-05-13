package com.airalo.example.gradle.convention.quality.staticanalysis

import com.airalo.example.gradle.config.BuildConfig
import com.diffplug.gradle.spotless.FormatExtension
import com.diffplug.gradle.spotless.SpotlessExtension
import java.io.File
import org.gradle.api.Project

internal object Spotless : StaticAnalysisConventionContract {
    private fun FormatExtension.configure() {
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }

    override fun apply(project: Project) {
        project.plugins.apply(BuildConfig.spotlessId)

        project.allprojects {
            repositories.gradlePluginPortal()
        }

        project.extensions.getByType(SpotlessExtension::class.java).apply {
            kotlin {
                target("**/*.kt")
                targetExclude("buildSrc/build/", "**/buildSrc/build/", "**/src/test/resources/**/*.kt", "**/build/**/*.kt")
                ktlint(BuildConfig.ktlintVersion).apply {
                    setEditorConfigPath(File(project.rootDir, ".editorconfig"))
                }
                configure()
            }

            kotlinGradle {
                target("**/*.gradle.kts")
                targetExclude("**/build/**/*")
                ktlint(BuildConfig.ktlintVersion).setEditorConfigPath(File(project.rootDir, ".editorconfig"))
                configure()
            }

            format("misc") {
                target("**/*.md", "**/.gitignore")
                configure()
            }
        }
    }
}
