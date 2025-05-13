package com.airalo.example.gradle.convention.kmp

import com.airalo.example.gradle.config.BuildConfig
import com.airalo.example.gradle.convention.junit.JunitKmpConfiguration
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyOrder
import kotlin.test.assertTrue
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer
import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class KmpJvmConfigurationSpec {
    private lateinit var fakeProject: Project
    private val junitConfiguration: JunitKmpConfiguration = mockk(relaxed = true)
    private val koin: Plugin<Project> = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        clearMocks(junitConfiguration, koin)
        fakeProject = ProjectBuilder.builder().build()
    }

    @Test
    fun `Given apply is called it applies Kmp and the AndroidConvention Plugin`() {
        // Given
        fakeProject.plugins.apply(BuildConfig.javaLibraryId)
        fakeProject.plugins.apply(BuildConfig.kmpId)
        val jvmLibraryConfiguration: Plugin<Project> = mockk(relaxed = true)
        val pluginContainer: PluginContainer = mockk(relaxed = true)
        val kmpExtension: KotlinMultiplatformExtension = spyk(fakeProject.extensions.getByType(KotlinMultiplatformExtension::class.java))
        val project: Project = spyk(fakeProject) {
            every { plugins } returns pluginContainer
            every { extensions.getByType(KotlinMultiplatformExtension::class.java) } returns kmpExtension
        }

        // When
        KmpJvmConfiguration(
            jvmLibraryConvention = jvmLibraryConfiguration,
            junitConfiguration = junitConfiguration,
            koin = koin,
        ).apply(project)

        // Then
        verifyOrder {
            pluginContainer.apply(BuildConfig.kmpId)
            project.extensions.getByType(KotlinMultiplatformExtension::class.java)
            jvmLibraryConfiguration.apply(project)
        }
    }

    @Test
    fun `When apply is called it configures Kmp for Android`() {
        // Given
        fakeProject.plugins.apply(BuildConfig.javaLibraryId)
        fakeProject.plugins.apply(BuildConfig.kmpId)
        val kmpExtension: KotlinMultiplatformExtension = spyk(fakeProject.extensions.getByType(KotlinMultiplatformExtension::class.java))
        val jvmLibraryConfiguration: Plugin<Project> = mockk(relaxed = true)
        val pluginContainer: PluginContainer = mockk(relaxed = true)
        val project: Project = spyk(fakeProject) {
            every { plugins } returns pluginContainer
            every { extensions.getByType(KotlinMultiplatformExtension::class.java) } returns kmpExtension
        }

        // When
        KmpJvmConfiguration(
            jvmLibraryConvention = jvmLibraryConfiguration,
            junitConfiguration = junitConfiguration,
            koin = koin,
        ).apply(project)

        // Then
        verify(exactly = 1) { kmpExtension.jvm() }
    }

    @Test
    fun `Given apply is called it configures Junit`() {
        // Given
        fakeProject.plugins.apply(BuildConfig.androidLibraryId)
        fakeProject.plugins.apply(BuildConfig.kmpId)
        val androidLibraryConfiguration: Plugin<Project> = mockk(relaxed = true)
        val pluginContainer: PluginContainer = mockk(relaxed = true)
        val project: Project = spyk(fakeProject) {
            every { plugins } returns pluginContainer
        }

        // When
        KmpJvmConfiguration(
            jvmLibraryConvention = androidLibraryConfiguration,
            junitConfiguration = junitConfiguration,
            koin = koin,
        ).apply(project)

        // Then
        verify(exactly = 1) { junitConfiguration.configure(project) }
        verify(exactly = 1) { koin.apply(project) }
    }

    @Test
    fun `It fulfils Plugin`() {
        assertTrue {
            @Suppress("USELESS_IS_CHECK")
            KmpJvmConfiguration() is Plugin<*>
        }
    }
}
