package com.airalo.example.gradle.convention.quality.dependency

import com.airalo.example.gradle.config.BuildConfig
import com.mikepenz.aboutlibraries.plugin.AboutLibrariesExtension
import java.io.File
import org.gradle.api.Project

internal object LicenseCompliance : DependencyConventionContract {
    private fun Project.configureAboutLibraries() {
        extensions.getByType(AboutLibrariesExtension::class.java).apply {
            registerAndroidTasks = false
            fetchRemoteLicense = true
            fetchRemoteFunding = false
            prettyPrint = true
            configPath = File(rootDir, "gradle/license/about/configuration").absolutePath
        }
    }

    override fun apply(project: Project) {
        project.subprojects.forEach { subbproject ->
            subbproject.plugins.apply(BuildConfig.licenseComplianceId)
            subbproject.configureAboutLibraries()
        }
    }
}
