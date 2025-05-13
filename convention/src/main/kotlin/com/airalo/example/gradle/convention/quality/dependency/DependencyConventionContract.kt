package com.airalo.example.gradle.convention.quality.dependency

import org.gradle.api.Project

interface DependencyConventionContract {
    fun apply(project: Project)
}
