package com.airalo.example.gradle.convention.android

import com.airalo.example.gradle.config.BuildConfig
import com.airalo.example.gradle.convention.compose.configureCompose
import com.airalo.example.gradle.convention.compose.setupMetric
import com.airalo.example.gradle.convention.compose.setupReport
import com.airalo.example.gradle.convention.dependencyCatalog
import com.airalo.example.gradle.convention.testDependencyCatalog
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class ComposeConfiguration : Plugin<Project> {
    private fun Project.addDependencies() {
        dependencies {
            add("implementation", dependencyCatalog.findBundle("convention-compose").get())
            add("testImplementation", testDependencyCatalog.findBundle("convention-roborazzi").get())
            add("debugImplementation", dependencyCatalog.findBundle("convention-debug-compose").get())
            add("androidTestImplementation", testDependencyCatalog.findBundle("convention-instrumented-compose").get())
        }
    }

    override fun apply(target: Project) {
        target.plugins.apply(BuildConfig.composeCompilerId)
        target.plugins.apply(BuildConfig.roborazziId)
        val extension = target.extensions.getByType(CommonExtension::class.java)
        target.addDependencies()
        extension.configureCompose()
        target.setupMetric()
        target.setupReport()
    }
}
