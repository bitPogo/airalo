package com.airalo.example.gradle.convention.quality.dependency

import com.airalo.example.gradle.config.BuildConfig
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertTrue
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer
import org.junit.jupiter.api.Test

class DependencyAnalysisSpec {
    @Test
    fun `When apply is called it applies Dependency Plugins`() {
        // Given
        val dependencyHealth: DependencyConventionContract = mockk(relaxed = true)
        val dependencyLicenseCheck: DependencyConventionContract = mockk(relaxed = true)
        val dependencyLicenseCompliance: DependencyConventionContract = mockk(relaxed = true)
        val pluginContainer: PluginContainer = mockk(relaxed = true)
        val project: Project = mockk {
            every { plugins } returns pluginContainer
        }

        // When
        DependencyAnalysis(
            listOf(
                dependencyHealth,
                dependencyLicenseCheck,
                dependencyLicenseCompliance,
            ),
        ).apply(project)

        // Then
        verify(exactly = 1) {
            dependencyHealth.apply(project)
        }
        verify(exactly = 1) {
            pluginContainer.apply(BuildConfig.owaspId)
        }
        verify(exactly = 1) {
            dependencyLicenseCheck.apply(project)
        }
        verify(exactly = 1) {
            dependencyLicenseCompliance.apply(project)
        }
    }

    @Test
    fun `It fulfils Plugin`() {
        assertTrue {
            @Suppress("USELESS_IS_CHECK")
            DependencyAnalysis() is Plugin<*>
        }
    }
}
