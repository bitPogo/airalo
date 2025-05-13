package com.airalo.example.gradle.convention.quality.staticanalysis

import com.airalo.example.gradle.convention.isAndroidApplication
import com.airalo.example.gradle.convention.isAndroidLibrary
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project

// find help under: https://googlesamples.github.io/android-custom-lint-rules/checks/severity.md.html
internal object AndroidLint : StaticAnalysisConventionContract {
    private fun Project.configureAndroidLinter(extension: CommonExtension<*, *, *, *, *, *>) {
        extension.lint.apply {
            abortOnError = true
            noLines = false
            showAll = true
            textReport = true
            xmlReport = false
            htmlReport = true
            warningsAsErrors = true
            checkTestSources = true
            disable.addAll(listOf("GifUsage", "GradleDependency"))
            baseline = this@configureAndroidLinter.file("lint-baseline.xml")
        }
    }

    override fun apply(project: Project) {
        project.afterEvaluate {
            when {
                isAndroidLibrary() -> {
                    configureAndroidLinter(extensions.getByType(LibraryExtension::class.java))
                }
                isAndroidApplication() -> {
                    configureAndroidLinter(extensions.getByType(ApplicationExtension::class.java))
                }
            }
        }
    }
}
