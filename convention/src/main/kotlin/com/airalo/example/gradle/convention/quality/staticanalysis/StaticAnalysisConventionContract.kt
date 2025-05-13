package com.airalo.example.gradle.convention.quality.staticanalysis

import org.gradle.api.Project

interface StaticAnalysisConventionContract {
    fun apply(project: Project)
}
