/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package com.airalo.example.gradle.convention.android

import com.airalo.example.gradle.convention.DependencyRepositoryProvider
import com.airalo.example.gradle.convention.android.ConfigurationContract.ParameterlessConfigurator
import io.mockk.clearMocks
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertTrue
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

// Taken from https://github.com/bitPogo/gradle-plugins/tree/main/antibytes-android-configuration
class LibraryWithComposeConfigurationSpec {
    private val compileTaskConfiguration: ParameterlessConfigurator = mockk(relaxed = true)
    private val toolChainConfiguration: ParameterlessConfigurator = mockk(relaxed = true)
    private val qualityMetric: Plugin<Project> = mockk(relaxed = true)
    private val repositories: DependencyRepositoryProvider = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        clearMocks(
            compileTaskConfiguration,
            toolChainConfiguration,
            qualityMetric,
            repositories,
        )
    }

    @Test
    fun `When apply is called with a Project, it applies the Android and Compose Conventions`() {
        // Given
        val project: Project = mockk()
        val androidLibraryConvention: Plugin<Project> = mockk(relaxed = true)
        val composeConventions: Plugin<Project> = mockk(relaxed = true)

        // When
        LibraryWithComposeConfiguration(
            androidLibraryConvention = androidLibraryConvention,
            composeConvention = composeConventions,
        ).apply(project)

        // Then
        verify(exactly = 1) { androidLibraryConvention.apply(project) }
        verify(exactly = 1) { composeConventions.apply(project) }
    }

    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = LibraryWithComposeConfiguration()

        @Suppress("KotlinConstantConditions")
        assertTrue(plugin is Plugin<*>)
    }
}
