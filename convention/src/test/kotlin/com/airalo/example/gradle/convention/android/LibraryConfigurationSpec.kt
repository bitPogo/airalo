/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package com.airalo.example.gradle.convention.android

import com.airalo.example.gradle.config.BuildConfig
import com.airalo.example.gradle.convention.DependencyRepositoryProvider
import com.airalo.example.gradle.convention.android.ConfigurationContract.ParameterlessConfigurator
import com.airalo.example.gradle.convention.junit.JunitConfiguration
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import kotlin.test.assertTrue
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

// Taken from https://github.com/bitPogo/gradle-plugins/tree/main/antibytes-android-configuration
class LibraryConfigurationSpec {
    private val libraryConfigurator: ParameterlessConfigurator = mockk(relaxed = true)
    private val compileTaskConfiguration: ParameterlessConfigurator = mockk(relaxed = true)
    private val toolChainConfiguration: ParameterlessConfigurator = mockk(relaxed = true)
    private val qualityMetric: Plugin<Project> = mockk(relaxed = true)
    private val repositories: DependencyRepositoryProvider = mockk(relaxed = true)
    private val junitConfiguration: JunitConfiguration = mockk(relaxed = true)
    private val koin: Plugin<Project> = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        clearMocks(
            libraryConfigurator,
            compileTaskConfiguration,
            toolChainConfiguration,
            qualityMetric,
            repositories,
            junitConfiguration,
            koin,
        )
    }

    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = LibraryConfiguration()

        @Suppress("KotlinConstantConditions")
        assertTrue(plugin is Plugin<*>)
    }

    @Test
    fun `When apply is called with a Project, it applies the Android Library if it is not applied`() {
        // Given
        val pluginContainer: PluginContainer = mockk(relaxed = true) {
            every { hasPlugin(any<String>()) } returns false
        }
        val project: Project = mockk(relaxed = true) {
            every { plugins } returns pluginContainer
        }

        // When
        LibraryConfiguration(
            libraryConfigurator = libraryConfigurator,
            compileTaskConfiguration = compileTaskConfiguration,
            toolChainConfiguration = toolChainConfiguration,
            qualityMetric = qualityMetric,
            repositoryConfiguration = repositories,
            junitConfiguration = junitConfiguration,
            koin = koin,
        ).apply(project)

        // Then
        verify(exactly = 1) { pluginContainer.apply(BuildConfig.androidLibraryId) }
        verify(exactly = 1) { pluginContainer.apply(BuildConfig.kotlinAndroidId) }
    }

    @Test
    fun `When apply is called with a Project, it does not applies the Kotlin Android Plugin if the Multiplatform Plugin was already applied`() {
        // Given
        val pluginContainer: PluginContainer = mockk(relaxed = true) {
            every { hasPlugin(any<String>()) } returns false
            every { hasPlugin(BuildConfig.kotlinMultiplatformId) } returns true
        }
        val project: Project = mockk(relaxed = true) {
            every { plugins } returns pluginContainer
        }

        // When
        LibraryConfiguration(
            libraryConfigurator = libraryConfigurator,
            compileTaskConfiguration = compileTaskConfiguration,
            toolChainConfiguration = toolChainConfiguration,
            qualityMetric = qualityMetric,
            repositoryConfiguration = repositories,
            junitConfiguration = junitConfiguration,
            koin = koin,
        ).apply(project)

        // Then
        verify(exactly = 1) { pluginContainer.apply(BuildConfig.androidLibraryId) }
        verify(exactly = 0) { pluginContainer.apply(BuildConfig.kotlinAndroidId) }
    }

    @Test
    fun `When apply is called with a Project, it applies not the Android Library if it is applied`() {
        // Given
        val pluginContainer: PluginContainer = mockk(relaxed = true) {
            every { hasPlugin(any<String>()) } returns true
        }
        val project: Project = mockk(relaxed = true) {
            every { plugins } returns pluginContainer
        }

        // When
        LibraryConfiguration(
            libraryConfigurator = libraryConfigurator,
            compileTaskConfiguration = compileTaskConfiguration,
            toolChainConfiguration = toolChainConfiguration,
            qualityMetric = qualityMetric,
            repositoryConfiguration = repositories,
            junitConfiguration = junitConfiguration,
            koin = koin,
        ).apply(project)

        // Then
        verify(exactly = 0) { pluginContainer.apply(BuildConfig.androidLibraryId) }
        verify(exactly = 0) { pluginContainer.apply(BuildConfig.kotlinAndroidId) }
    }

    @Test
    fun `When apply is called with a Project, it applies the Quality Plugin after Android Library`() {
        // Given
        val pluginContainer: PluginContainer = mockk(relaxed = true) {
            every { hasPlugin(any<String>()) } returns false
        }
        val project: Project = mockk(relaxed = true) {
            every { plugins } returns pluginContainer
        }

        // When
        LibraryConfiguration(
            libraryConfigurator = libraryConfigurator,
            compileTaskConfiguration = compileTaskConfiguration,
            toolChainConfiguration = toolChainConfiguration,
            qualityMetric = qualityMetric,
            repositoryConfiguration = repositories,
            junitConfiguration = junitConfiguration,
            koin = koin,
        ).apply(project)

        // Then
        verify(exactly = 1) { qualityMetric.apply(project) }
        verifyOrder {
            pluginContainer.apply(BuildConfig.androidLibraryId)
            qualityMetric.apply(project)
        }
    }

    @Test
    fun `When apply is called with a Project, it configures Android`() {
        // Given
        val pluginContainer: PluginContainer = mockk(relaxed = true) {
            every { hasPlugin(any<String>()) } returns true
        }
        val project: Project = mockk(relaxed = true) {
            every { plugins } returns pluginContainer
        }

        // When
        LibraryConfiguration(
            libraryConfigurator = libraryConfigurator,
            compileTaskConfiguration = compileTaskConfiguration,
            toolChainConfiguration = toolChainConfiguration,
            qualityMetric = qualityMetric,
            repositoryConfiguration = repositories,
            junitConfiguration = junitConfiguration,
            koin = koin,
        ).apply(project)

        // Then
        verify(exactly = 1) { libraryConfigurator.configure(project) }
        verify(exactly = 1) { compileTaskConfiguration.configure(project) }
        verify(exactly = 1) { toolChainConfiguration.configure(project) }
        verify(exactly = 1) { junitConfiguration.configure(project) }
        verify(exactly = 1) { koin.apply(project) }
    }
}
