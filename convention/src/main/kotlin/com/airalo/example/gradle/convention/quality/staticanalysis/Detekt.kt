package com.airalo.example.gradle.convention.quality.staticanalysis

import com.airalo.example.gradle.config.BuildConfig
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import java.io.File
import org.gradle.api.Project

internal object Detekt : StaticAnalysisConventionContract {
    private fun Project.configureExtension() {
        extensions.configure(DetektExtension::class.java) {
            toolVersion = BuildConfig.detektVersion
            buildUponDefaultConfig = true
            allRules = false
            config.setFrom(File(this@configureExtension.rootDir.absolutePath, "gradle/detekt/config.yml"))
            baseline = File(this@configureExtension.rootDir.absolutePath, "gradle/detekt/baseline.yml")
            autoCorrect = true
        }
    }

    private fun Project.configureTask() {
        tasks.withType(Detekt::class.java).configureEach {
            exclude(
                "**/.gradle/**",
                "**/.idea/**",
                "build/",
                "**/build/**",
                "**/buildSrc/**",
                ".github/**",
                "gradle/**",
                "**/example/**",
                "**/test/resources/**",
                "**/build.gradle.kts",
                "**/settings.gradle.kts",
                "**/Dangerfile.df.kts",
            )

            reports.apply {
                xml.required.set(true)
                html.required.set(true)
                md.required.set(false)
                txt.required.set(false)
            }
        }
    }

    private fun Project.configureBaselineTask() {
        tasks.withType(DetektCreateBaselineTask::class.java).configureEach {
            exclude(
                "**/.gradle/**",
                "**/.idea/**",
                "**/build/**",
                "**/gradle/wrapper/**",
                ".github/**",
                "assets/**",
                "docs/**",
                "gradle/**",
                "**/example/**",
                "**/*.adoc",
                "**/*.md",
                "**/gradlew",
                "**/LICENSE",
                "**/.java-version",
                "**/gradlew.bat",
                "**/*.png",
                "**/*.properties",
                "**/*.pro",
                "**/*.sq",
                "**/*.xml",
                "**/*.yml",
            )
        }
    }

    override fun apply(project: Project) {
        project.plugins.apply(BuildConfig.detektId)
        project.configureExtension()
        project.configureTask()
        project.configureBaselineTask()
    }
}
