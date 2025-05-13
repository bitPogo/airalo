package com.airalo.example.gradle.convention

import org.gradle.api.Project

interface DependencyRepositoryProvider {
    fun configure(project: Project)
}

internal object DependencyRepository : DependencyRepositoryProvider {
    override fun configure(project: Project) {
        project.repositories.apply {
            google()
            gradlePluginPortal()
            mavenCentral()
            maven {
                setUrl("https://maven.pkg.jetbrains.space/public/p/compose/dev")
            }
            maven {
                setUrl("https://maven.appspector.com/artifactory/android-sdk")
            }
        }
    }
}
