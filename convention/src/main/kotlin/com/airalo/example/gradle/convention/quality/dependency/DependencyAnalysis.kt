package com.airalo.example.gradle.convention.quality.dependency

import com.airalo.example.gradle.config.BuildConfig
import javax.inject.Inject
import org.gradle.api.Plugin
import org.gradle.api.Project

class DependencyAnalysis(
    private val conventionProvider: List<DependencyConventionContract>,
) : Plugin<Project> {
    @Inject
    constructor() : this(
        listOf(
            DependencyHealth,
            LicenseCheck,
            LicenseCompliance,
        ),
    )

    override fun apply(target: Project) {
        target.plugins.apply(BuildConfig.owaspId)
        conventionProvider.forEach { convention -> convention.apply(target) }
    }
}
