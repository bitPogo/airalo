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
class ApplicationConfigurationSpec {
    private val applicationConfigurator: ParameterlessConfigurator = mockk(relaxed = true)
    private val compileTaskConfiguration: ParameterlessConfigurator = mockk(relaxed = true)
    private val toolChainConfiguration: ParameterlessConfigurator = mockk(relaxed = true)
    private val qualityMetric: Plugin<Project> = mockk(relaxed = true)
    private val repositories: DependencyRepositoryProvider = mockk(relaxed = true)
    private val composeConfiguration: Plugin<Project> = mockk(relaxed = true)
    private val junitConfiguration: JunitConfiguration = mockk(relaxed = true)
    private val koin: Plugin<Project> = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        clearMocks(
            applicationConfigurator,
            compileTaskConfiguration,
            toolChainConfiguration,
            qualityMetric,
            repositories,
            composeConfiguration,
            junitConfiguration,
            koin,
        )
    }

    @Suppress("KotlinConstantConditions")
    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = ApplicationConfiguration()

        assertTrue(plugin is Plugin<*>)
    }

    @Test
    fun `When apply is called with a Project, it applies the Android Application if it is not applied`() {
        // Given
        val pluginContainer: PluginContainer = mockk(relaxed = true) {
            every { hasPlugin(any<String>()) } returns false
        }
        val project: Project = mockk(relaxed = true) {
            every { plugins } returns pluginContainer
        }

        // When
        ApplicationConfiguration(
            applicationConfigurator = applicationConfigurator,
            compileTaskConfiguration = compileTaskConfiguration,
            toolChainConfiguration = toolChainConfiguration,
            qualityMetric = qualityMetric,
            repositoryConfiguration = repositories,
            composeConfiguration = composeConfiguration,
            junitConfiguration = junitConfiguration,
            koin = koin,
        ).apply(project)

        // Then
        verify(exactly = 1) { pluginContainer.apply(BuildConfig.androidApplicationId) }
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
        ApplicationConfiguration(
            applicationConfigurator = applicationConfigurator,
            compileTaskConfiguration = compileTaskConfiguration,
            toolChainConfiguration = toolChainConfiguration,
            qualityMetric = qualityMetric,
            repositoryConfiguration = repositories,
            composeConfiguration = composeConfiguration,
            junitConfiguration = junitConfiguration,
            koin = koin,
        ).apply(project)

        // Then
        verify(exactly = 1) { pluginContainer.apply(BuildConfig.androidApplicationId) }
        verify(exactly = 0) { pluginContainer.apply(BuildConfig.kotlinAndroidId) }
    }

    @Test
    fun `When apply is called with a Project, it applies not the Android Application if it is applied`() {
        // Given
        val pluginContainer: PluginContainer = mockk(relaxed = true) {
            every { hasPlugin(any<String>()) } returns true
        }
        val project: Project = mockk(relaxed = true) {
            every { plugins } returns pluginContainer
        }

        // When
        ApplicationConfiguration(
            applicationConfigurator = applicationConfigurator,
            compileTaskConfiguration = compileTaskConfiguration,
            toolChainConfiguration = toolChainConfiguration,
            qualityMetric = qualityMetric,
            repositoryConfiguration = repositories,
            composeConfiguration = composeConfiguration,
            junitConfiguration = junitConfiguration,
            koin = koin,
        ).apply(project)

        // Then
        verify(exactly = 0) { pluginContainer.apply(BuildConfig.androidApplicationId) }
        verify(exactly = 0) { pluginContainer.apply(BuildConfig.kotlinAndroidId) }
    }

    @Test
    fun `When apply is called with a Project, it applies the Quality Plugin after Android Application`() {
        // Given
        val pluginContainer: PluginContainer = mockk(relaxed = true) {
            every { hasPlugin(any<String>()) } returns false
        }
        val project: Project = mockk(relaxed = true) {
            every { plugins } returns pluginContainer
        }

        // When
        ApplicationConfiguration(
            applicationConfigurator = applicationConfigurator,
            compileTaskConfiguration = compileTaskConfiguration,
            toolChainConfiguration = toolChainConfiguration,
            qualityMetric = qualityMetric,
            repositoryConfiguration = repositories,
            composeConfiguration = composeConfiguration,
            junitConfiguration = junitConfiguration,
            koin = koin,
        ).apply(project)

        // Then
        verify(exactly = 1) { qualityMetric.apply(project) }
        verifyOrder {
            pluginContainer.apply(BuildConfig.androidApplicationId)
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
        ApplicationConfiguration(
            applicationConfigurator = applicationConfigurator,
            compileTaskConfiguration = compileTaskConfiguration,
            toolChainConfiguration = toolChainConfiguration,
            qualityMetric = qualityMetric,
            repositoryConfiguration = repositories,
            composeConfiguration = composeConfiguration,
            junitConfiguration = junitConfiguration,
            koin = koin,
        ).apply(project)

        // Then
        verify(exactly = 1) { applicationConfigurator.configure(project) }
        verify(exactly = 1) { compileTaskConfiguration.configure(project) }
        verify(exactly = 1) { toolChainConfiguration.configure(project) }
        verify(exactly = 1) { repositories.configure(project) }
        verify(exactly = 1) { composeConfiguration.apply(project) }
        verify(exactly = 1) { junitConfiguration.configure(project) }
        verify(exactly = 1) { koin.apply(project) }
    }
}
