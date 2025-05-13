package com.airalo.example.gradle.convention.junit

import com.airalo.example.gradle.convention.isKmp
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.provider.Provider

object JunitConfiguration : JunitConfigurationSkeleton() {
    private fun Project.setupPlainJvm(junit: Provider<ExternalModuleDependencyBundle>) {
        dependencies.apply {
            add("testImplementation", junit)
        }
    }

    fun configure(project: Project) {
        if (!project.isKmp()) {
            val junitBundle = project.resolveJunitBundle()

            project.setupPlainJvm(junitBundle)
            project.setupJunitTestTasks()
        }
    }
}
