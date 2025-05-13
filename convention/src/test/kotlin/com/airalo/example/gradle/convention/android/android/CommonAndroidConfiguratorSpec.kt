@file:Suppress("ktlint:standard:max-line-length")

package com.airalo.example.gradle.convention.android.android

import com.airalo.example.gradle.config.BuildConfig
import com.airalo.example.gradle.convention.android.AndroidConfigurationApiContract.Companion.COMPATIBILITY_TARGETS
import com.airalo.example.gradle.convention.android.AndroidConfigurationApiContract.Companion.MIN_SDK
import com.airalo.example.gradle.convention.android.AndroidConfigurationApiContract.Companion.TARGET_SDK
import com.airalo.example.gradle.convention.android.AndroidConfigurationApiContract.Companion.TEST_RUNNER
import com.airalo.example.gradle.convention.android.AndroidConfigurationApiContract.Companion.TEST_RUNNER_ARGUMENTS
import com.android.build.api.dsl.CompileOptions
import com.android.build.api.dsl.LibraryDefaultConfig
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.TestOptions
import com.android.build.api.dsl.UnitTestOptions
import io.mockk.every
import io.mockk.invoke
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test

class CommonAndroidConfiguratorSpec {
    @Test
    fun `When configure is called with a AndroidExtension, it sets the CompileSDK Version`() {
        // Given
        val libraryExtension: LibraryExtension = mockk(relaxed = true)

        // When
        CommonAndroidConfigurator.configure(mockk(), libraryExtension)

        // Then
        verify(exactly = 1) { libraryExtension.compileSdk = TARGET_SDK }
    }

    @Test
    fun `When configure is called with a AndroidExtension and a AndroidConfiguration, it setups up the DefaultBuildTypeConfiguration`() {
        // Given
        val libraryExtension: LibraryExtension = mockk(relaxed = true)
        val defaultConfiguration: LibraryDefaultConfig = mockk(relaxed = true)
        val runnerArguments: MutableMap<String, String> = mutableMapOf()

        every { libraryExtension.defaultConfig(captureLambda()) } answers {
            lambda<(LibraryDefaultConfig) -> Unit>().invoke(defaultConfiguration)
        }

        every { defaultConfiguration.testInstrumentationRunnerArguments } returns runnerArguments

        // When
        CommonAndroidConfigurator.configure(mockk(), libraryExtension)

        // Then
        verify(exactly = 1) { defaultConfiguration.minSdk = MIN_SDK }
        verify(exactly = 1) { defaultConfiguration.testInstrumentationRunner = TEST_RUNNER }
        assertEquals(
            actual = runnerArguments,
            expected = TEST_RUNNER_ARGUMENTS,
        )
    }

    @Test
    fun `When configure is called with a AndroidExtension and a AndroidConfiguration, it setups up the CompileOptions`() {
        // Given
        val libraryExtension: LibraryExtension = mockk(relaxed = true)
        val compileOptions: CompileOptions = mockk(relaxed = true)

        every { libraryExtension.compileOptions(captureLambda()) } answers {
            lambda<(CompileOptions) -> Unit>().invoke(compileOptions)
        }

        // When
        CommonAndroidConfigurator.configure(mockk(), libraryExtension)

        // Then
        verify(exactly = 1) { compileOptions.targetCompatibility = COMPATIBILITY_TARGETS }
        verify(exactly = 1) { compileOptions.sourceCompatibility = COMPATIBILITY_TARGETS }
    }

    @Suppress("UnstableApiUsage")
    @Test
    fun `When configure is called with a AndroidExtension and a AndroidConfiguration, it setups up the testOptions`() {
        // Given
        val libraryExtension: LibraryExtension = mockk(relaxed = true)
        val testOptions: TestOptions = mockk(relaxed = true)
        val unitTests: UnitTestOptions = mockk(relaxed = true)

        every { libraryExtension.testOptions(captureLambda()) } answers {
            lambda<(TestOptions) -> Unit>().invoke(testOptions)
        }
        every { testOptions.unitTests(captureLambda()) } answers {
            lambda<(UnitTestOptions) -> Unit>().invoke(unitTests)
        }

        // When
        CommonAndroidConfigurator.configure(mockk(), libraryExtension)

        // Then
        verify(exactly = 1) { unitTests.isReturnDefaultValues = true }
        verify(exactly = 1) { unitTests.isIncludeAndroidResources = true }
    }

    @Test
    fun `When configure is called it setups up the buildTypes`() {
        // Given
        val project: Project = ProjectBuilder.builder().build().also {
            it.plugins.apply(BuildConfig.androidLibraryId)
        }
        val extension = project.extensions.getByType(LibraryExtension::class.java).also {
            it.namespace = "test"
        }

        // When
        CommonAndroidConfigurator.configure(project, extension)

        // Then
        val release = extension.buildTypes.named("release").get()
        val debug = extension.buildTypes.named("debug").get()

        assertFalse { release.isMinifyEnabled }
        assertFalse { release.isShrinkResources }
        assertTrue { release.proguardFiles.first().absolutePath.contains("proguard-android-optimize.txt") }
        assertEquals(
            actual = release.proguardFiles.last(),
            expected = project.file("proguard-rules.pro"),
        )

        assertFalse { debug.isMinifyEnabled }
        assertFalse { debug.isShrinkResources }
        assertEquals(
            actual = debug.matchingFallbacks,
            expected = listOf("release"),
        )
    }
}
