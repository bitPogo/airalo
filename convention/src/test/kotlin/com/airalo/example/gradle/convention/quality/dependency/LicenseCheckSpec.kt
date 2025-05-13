package com.airalo.example.gradle.convention.quality.dependency

import com.airalo.example.gradle.config.BuildConfig
import com.appmattus.kotlinfixture.kotlinFixture
import com.github.jk1.license.LicenseReportExtension
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LicenseCheckSpec {
    private val fixture = kotlinFixture()
    private lateinit var fakeProject: Project

    @BeforeEach
    fun setup() {
        fakeProject = ProjectBuilder.builder().build()
    }

    @Test
    fun `When apply is called it applies the Licence Report Gradle Plugin`() {
        // Given
        fakeProject.plugins.apply(BuildConfig.licenseCheckId)
        val pluginContainer: PluginContainer = mockk(relaxed = true)
        val project: Project = spyk(fakeProject) {
            every { plugins } returns pluginContainer
        }

        // When
        LicenseCheck.apply(project)

        // Then
        verify(exactly = 1) {
            pluginContainer.apply(BuildConfig.licenseCheckId)
        }
    }

    @Test
    fun `When apply is called it configures the allowed list and filters for transitive dependencies`() {
        // Given
        val rootProjectDir: File = fixture()
        fakeProject.plugins.apply(BuildConfig.licenseCheckId)
        val extension: LicenseReportExtension = fakeProject.extensions.getByType(LicenseReportExtension::class.java)
        val project: Project = spyk(fakeProject) {
            every { rootDir } returns rootProjectDir
        }

        // When
        LicenseCheck.apply(project)

        // Then
        assertEquals(
            actual = (extension.allowedLicensesFile as File).absolutePath,
            expected = File(rootProjectDir, "gradle/license/allowed.json").absolutePath,
        )
        assertEquals(
            actual = extension.configurations,
            expected = LicenseReportExtension.ALL,
        )
        assertEquals(
            actual = extension.excludes.toList(),
            expected = listOf("org.jetbrains.compose.ui:ui-uikit-uikitsimarm64"),
        )
        assertEquals(
            actual = extension.filters.size,
            expected = 0,
        )
    }

    @Test
    fun `It fulfils DependencyConventionContract`() {
        assertTrue {
            @Suppress("USELESS_IS_CHECK")
            LicenseCheck is DependencyConventionContract
        }
    }
}
