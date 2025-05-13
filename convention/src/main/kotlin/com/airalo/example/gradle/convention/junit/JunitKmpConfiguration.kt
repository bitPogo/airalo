package com.airalo.example.gradle.convention.junit

import com.airalo.example.gradle.convention.isAndroidLibrary
import com.airalo.example.gradle.convention.testDependencyCatalog
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

object JunitKmpConfiguration : JunitConfigurationSkeleton() {
    private fun Project.determineTestSet(): String {
        return if (isAndroidLibrary()) {
            "androidUnitTest"
        } else {
            "jvmTest"
        }
    }

    private fun Project.resolveCommonTest(): Provider<MinimalExternalModuleDependency> {
        return testDependencyCatalog.findLibrary("kotlin-common").get()
    }

    private fun Project.setupKmp(junit: Provider<ExternalModuleDependencyBundle>) {
        extensions.getByType(KotlinMultiplatformExtension::class.java).apply {
            sourceSets.getByName("commonTest").dependencies {
                implementation(this@setupKmp.resolveCommonTest())
            }

            sourceSets.getByName(determineTestSet()).dependencies {
                implementation(junit)
            }
        }
    }

    fun configure(project: Project) {
        val junitBundle = project.resolveJunitBundle()

        project.setupKmp(junitBundle)
        project.setupJunitTestTasks()
    }
}
