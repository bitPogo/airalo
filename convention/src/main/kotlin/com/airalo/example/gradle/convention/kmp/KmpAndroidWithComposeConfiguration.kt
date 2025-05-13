package com.airalo.example.gradle.convention.kmp

import com.airalo.example.gradle.config.BuildConfig
import com.airalo.example.gradle.convention.compose.configureCompose
import com.airalo.example.gradle.convention.compose.setupMetric
import com.airalo.example.gradle.convention.compose.setupReport
import com.airalo.example.gradle.convention.testDependencyCatalog
import com.android.build.api.dsl.LibraryExtension
import javax.inject.Inject
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpAndroidWithComposeConfiguration(
    private val kmpAndroidConvention: Plugin<Project>,
) : Plugin<Project> {
    @Inject
    constructor() : this(kmpAndroidConvention = KmpAndroidConfiguration())

    private fun KotlinMultiplatformExtension.addDependencies(project: Project) {
        sourceSets.getByName("androidUnitTest").dependencies {
            implementation(project.testDependencyCatalog.findBundle("convention-roborazzi").get())
        }
    }

    private fun Project.correctCompose() {
        afterEvaluate {
            tasks.named("explodeCodeSourceDebug") {
                mustRunAfter("generateActualResourceCollectorsForAndroidMain")
            }

            tasks.named("explodeCodeSourceRelease") {
                mustRunAfter("generateActualResourceCollectorsForAndroidMain")
            }
        }
    }

    override fun apply(target: Project) {
        kmpAndroidConvention.apply(target)
        target.plugins.apply(BuildConfig.composeCompilerId)
        target.plugins.apply(BuildConfig.jbComposeId)
        target.plugins.apply(BuildConfig.roborazziId)

        val androidExtension = target.extensions.getByType(LibraryExtension::class.java)
        val kotlinExtension = target.extensions.getByType(KotlinMultiplatformExtension::class.java)

        androidExtension.configureCompose()
        target.setupMetric()
        target.setupReport()
        kotlinExtension.addDependencies(target)
        target.correctCompose()
    }
}
