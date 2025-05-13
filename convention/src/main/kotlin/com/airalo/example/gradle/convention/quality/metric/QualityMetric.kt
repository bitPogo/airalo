package com.airalo.example.gradle.convention.quality.metric

import com.airalo.example.gradle.config.BuildConfig
import com.airalo.example.gradle.convention.GradleUtilApiContract
import com.airalo.example.gradle.convention.PlatformContextResolver
import com.airalo.example.gradle.convention.hasAndroidApplication
import com.airalo.example.gradle.convention.hasAndroidLibrary
import com.airalo.example.gradle.convention.hasJvmLibrary
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import javax.inject.Inject
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal class QualityMetric(
    private val contextResolver: GradleUtilApiContract.PlatformContextResolver,
) : Plugin<Project> {
    @Inject
    constructor() : this(contextResolver = PlatformContextResolver)

    private fun Project.useJacoco() {
        extensions.getByType(KoverProjectExtension::class.java).useJacoco(BuildConfig.jacocoVersion)
    }

    private fun CommonExtension<*, *, *, *, *, *>.resolveDebugVariant(): String {
        val variant = productFlavors.firstOrNull()?.name ?: ""

        return "${variant}Debug".replaceFirstChar { it.lowercase() }
    }

    private fun Project.configureBaseVariants(variantName: String) {
        if (variantName != "debug") {
            extensions.getByType(KoverProjectExtension::class.java).apply {
                currentProject {
                    createVariant("custom") {
                        addWithDependencies(variantName)
                    }
                }
            }
        }
    }

    private fun enableCoverage(androidExtension: CommonExtension<*, *, *, *, *, *>) {
        androidExtension.apply {
            testCoverage.jacocoVersion = BuildConfig.jacocoVersion
            buildTypes.forEach { type ->
                if (type.name != "release") {
                    type.enableAndroidTestCoverage = true
                }
            }
        }
    }

    private fun Project.configureAndroid(androidExtension: CommonExtension<*, *, *, *, *, *>) {
        useJacoco()
        androidExtension.apply(::enableCoverage)
        afterEvaluate {
            configureBaseVariants(
                androidExtension.resolveDebugVariant(),
            )
        }
    }

    private fun Project.configureAndroidApplication() {
        configureAndroid(
            extensions.getByType(ApplicationExtension::class.java),
        )
    }

    private fun Project.configureAndroidLibrary() {
        configureAndroid(
            extensions.getByType(LibraryExtension::class.java),
        )
    }

    private fun Project.configureJvmLibrary() {
        useJacoco()
    }

    private fun Project.configureReport() {
        extensions.getByType(KoverProjectExtension::class.java).apply {
            reports {
                total {
                    xml {
                        onCheck.set(false)
                        xmlFile.set(file("build/reports/kover/result.xml"))
                    }

                    html {
                        onCheck.set(false)
                        htmlDir.set(file("build/reports/kover/report"))
                    }
                }
            }
        }
    }

    private fun Project.configureProjectRoot() {
        useJacoco()
        dependencies {
            subprojects.forEach { subProject ->
                if (subProject.file("build.gradle.kts").exists() && !subProject.file(".koverignore").exists()) {
                    add("kover", subProject)
                }
            }
        }
        configureReport()
    }

    override fun apply(target: Project) {
        val context = contextResolver.getType(target)

        when {
            target.rootProject == target -> {
                target.plugins.apply(BuildConfig.koverId)
                target.configureProjectRoot()
            }
            context.hasAndroidApplication() -> {
                target.plugins.apply(BuildConfig.koverId)
                target.configureAndroidApplication()
            }
            context.hasAndroidLibrary() -> {
                target.plugins.apply(BuildConfig.koverId)
                target.configureAndroidLibrary()
            }
            context.hasJvmLibrary() -> {
                target.plugins.apply(BuildConfig.koverId)
                target.configureJvmLibrary()
            }
        }
    }
}
