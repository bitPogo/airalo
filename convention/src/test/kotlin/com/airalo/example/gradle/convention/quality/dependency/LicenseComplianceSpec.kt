package com.airalo.example.gradle.convention.quality.dependency

import com.airalo.example.gradle.config.BuildConfig
import com.appmattus.kotlinfixture.kotlinFixture
import com.mikepenz.aboutlibraries.plugin.AboutLibrariesExtension
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LicenseComplianceSpec {
    private val fixture = kotlinFixture()
    private lateinit var fakeProject: Project

    @BeforeEach
    fun setup() {
        fakeProject = ProjectBuilder.builder().build()
    }

    @Test
    fun `When apply is called it applies the about libraries Gradle Plugin for all subprojects`() {
        // Given
        fakeProject.plugins.apply(BuildConfig.licenseComplianceId)
        val pluginContainer: PluginContainer = mockk(relaxed = true)
        val project: Project = spyk(fakeProject) {
            every { plugins } returns pluginContainer
        }
        val rootProject: Project = mockk {
            every { subprojects } returns setOf(project)
        }

        // When
        LicenseCompliance.apply(rootProject)

        // Then
        verify(exactly = 1) {
            pluginContainer.apply(BuildConfig.licenseComplianceId)
        }
    }

    @Test
    fun `When apply is called it disables the autogeneration and sets the report directory`() {
        // Given
        val rootProjectDir: File = fixture()
        fakeProject.plugins.apply(BuildConfig.licenseComplianceId)
        val extension: AboutLibrariesExtension = fakeProject.extensions.getByType(AboutLibrariesExtension::class.java)
        val project: Project = spyk(fakeProject) {
            every { rootDir } returns rootProjectDir
        }
        val rootProject: Project = mockk {
            every { subprojects } returns setOf(project)
        }

        // When
        LicenseCompliance.apply(rootProject)

        // Then
        assertFalse(actual = extension.registerAndroidTasks)
        assertFalse(actual = extension.fetchRemoteFunding)
        assertTrue(extension.prettyPrint)
        assertTrue(extension.fetchRemoteLicense)
        assertEquals(
            actual = extension.configPath,
            expected = File(rootProjectDir, "gradle/license/about/configuration").absolutePath,
        )
    }

    @Test
    fun `It fulfils DependencyConventionContract`() {
        assertTrue {
            @Suppress("USELESS_IS_CHECK")
            LicenseCompliance is DependencyConventionContract
        }
    }
}
