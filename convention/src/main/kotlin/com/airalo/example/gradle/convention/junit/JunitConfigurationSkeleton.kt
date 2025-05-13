package com.airalo.example.gradle.convention.junit

import com.airalo.example.gradle.convention.testDependencyCatalog
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.testing.Test

abstract class JunitConfigurationSkeleton {
    protected fun Project.setupJunitTestTasks() {
        tasks.withType(Test::class.java) {
            useJUnitPlatform()
        }
    }

    protected fun Project.resolveJunitBundle(): Provider<ExternalModuleDependencyBundle> {
        return testDependencyCatalog.findBundle("convention-junit5").get()
    }
}
