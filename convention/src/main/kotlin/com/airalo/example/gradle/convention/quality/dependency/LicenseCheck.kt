package com.airalo.example.gradle.convention.quality.dependency

import com.airalo.example.gradle.config.BuildConfig
import com.github.jk1.license.LicenseReportExtension
import java.io.File
import org.gradle.api.Project

internal object LicenseCheck : DependencyConventionContract {
    override fun apply(project: Project) {
        project.plugins.apply(BuildConfig.licenseCheckId)
        project.extensions.getByType(LicenseReportExtension::class.java).apply {
            allowedLicensesFile = File(project.rootDir, "gradle/license/allowed.json")
            excludes = arrayOf("org.jetbrains.compose.ui:ui-uikit-uikitsimarm64")
            /*
            see: https://github.com/jk1/Gradle-License-Report/issues/302
            filters = arrayOf(
                ExcludeTransitiveDependenciesFilter()
            )*/
            configurations = LicenseReportExtension.ALL
        }
    }
}
