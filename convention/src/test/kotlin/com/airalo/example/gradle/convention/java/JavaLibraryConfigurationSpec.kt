package com.airalo.example.gradle.convention.java

import com.airalo.example.gradle.config.BuildConfig
import com.airalo.example.gradle.convention.DependencyRepositoryProvider
import com.airalo.example.gradle.convention.junit.JunitConfiguration
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import kotlin.test.assertTrue
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.PluginContainer
import org.gradle.jvm.toolchain.JavaToolchainSpec
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class JavaLibraryConfigurationSpec {
    private val toolChainConfigurator: ParameterlessConfiguratorContract = mockk(relaxed = true)
    private val qualityMetric: Plugin<Project> = mockk(relaxed = true)
    private val repositories: DependencyRepositoryProvider = mockk(relaxed = true)
    private val junit: JunitConfiguration = mockk(relaxed = true)
    private val koin: Plugin<Project> = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        clearMocks(
            toolChainConfigurator,
            qualityMetric,
            repositories,
            junit,
            koin,
        )
    }

    @Test
    fun `It fulfils Plugin`() {
        val plugin: Any = JavaLibraryConfiguration()

        @Suppress("KotlinConstantConditions")
        assertTrue(plugin is Plugin<*>)
    }

    @Test
    fun `When apply is called with a Project, it applies the Java Library Plugin if it is not applied`() {
        // Given
        val pluginContainer: PluginContainer = mockk(relaxed = true) {
            every { hasPlugin(any<String>()) } returns false
        }
        val project: Project = mockk(relaxed = true) {
            every { plugins } returns pluginContainer
            every { extensions.getByType(JavaPluginExtension::class.java) } returns mockk(relaxed = true)
        }

        // When
        JavaLibraryConfiguration(
            toolChainConfigurator = toolChainConfigurator,
            qualityMetric = qualityMetric,
            repositoryConfiguration = repositories,
            junitConfiguration = junit,
            koin = koin,
        ).apply(project)

        // Then
        verify(exactly = 1) { pluginContainer.apply(BuildConfig.javaLibraryId) }
        verify(exactly = 1) { pluginContainer.apply(BuildConfig.kotlinJvmId) }
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
            every { extensions.getByType(JavaPluginExtension::class.java) } returns mockk(relaxed = true)
        }

        // When
        JavaLibraryConfiguration(
            toolChainConfigurator = toolChainConfigurator,
            qualityMetric = qualityMetric,
            repositoryConfiguration = repositories,
            junitConfiguration = junit,
            koin = koin,
        ).apply(project)

        // Then
        verify(exactly = 0) { pluginContainer.apply(BuildConfig.javaLibraryId) }
        verify(exactly = 0) { pluginContainer.apply(BuildConfig.kotlinJvmId) }
    }

    @Test
    fun `When apply is called with a Project, it calls to the ToolChain configurator`() {
        // Given
        val pluginContainer: PluginContainer = mockk(relaxed = true) {
            every { hasPlugin(any<String>()) } returns true
        }
        val project: Project = mockk(relaxed = true) {
            every { plugins } returns pluginContainer
            every { extensions.getByType(JavaPluginExtension::class.java) } returns mockk(relaxed = true)
        }

        // When
        JavaLibraryConfiguration(
            toolChainConfigurator = toolChainConfigurator,
            qualityMetric = qualityMetric,
            repositoryConfiguration = repositories,
            junitConfiguration = junit,
            koin = koin,
        ).apply(project)

        // Then
        verify(exactly = 1) { toolChainConfigurator.configure(project) }
    }

    @Test
    fun `When apply is called with a Project, it setups the repositories`() {
        // Given
        val pluginContainer: PluginContainer = mockk(relaxed = true) {
            every { hasPlugin(any<String>()) } returns true
        }
        val project: Project = mockk(relaxed = true) {
            every { plugins } returns pluginContainer
            every { extensions.getByType(JavaPluginExtension::class.java) } returns mockk(relaxed = true)
        }

        // When
        JavaLibraryConfiguration(
            toolChainConfigurator = toolChainConfigurator,
            qualityMetric = qualityMetric,
            repositoryConfiguration = repositories,
            junitConfiguration = junit,
            koin = koin,
        ).apply(project)

        // Then
        verify(exactly = 1) { repositories.configure(project) }
        verify(exactly = 1) { junit.configure(project) }
        verify(exactly = 1) { koin.apply(project) }
    }

    @Test
    fun `When apply is called with a Project, it applies the Quality Plugin after Java Plugin`() {
        // Given
        val pluginContainer: PluginContainer = mockk(relaxed = true) {
            every { hasPlugin(any<String>()) } returns false
        }
        val project: Project = mockk {
            every { plugins } returns pluginContainer
            every { extensions.getByType(JavaPluginExtension::class.java) } returns mockk(relaxed = true)
        }

        // When
        JavaLibraryConfiguration(
            toolChainConfigurator = toolChainConfigurator,
            qualityMetric = qualityMetric,
            repositoryConfiguration = repositories,
            junitConfiguration = junit,
            koin = koin,
        ).apply(project)

        // Then
        verify(exactly = 1) { qualityMetric.apply(project) }
        verifyOrder {
            pluginContainer.apply(BuildConfig.javaLibraryId)
            qualityMetric.apply(project)
        }
    }

    @Test
    fun `Given apply is called it sets up the compatibility and the toolchain`() {
        // Given
        val version = JavaVersion.toVersion(BuildConfig.javaToolchainVersion)
        val jvmToolchain: JavaToolchainSpec = mockk(relaxed = true)
        val javaPluginExtension: JavaPluginExtension = mockk(relaxed = true) {
            every { toolchain } returns jvmToolchain
        }
        val project: Project = mockk {
            every { plugins } returns mockk(relaxed = true)
            every { extensions.getByType(JavaPluginExtension::class.java) } returns javaPluginExtension
        }

        // When
        JavaLibraryConfiguration(
            toolChainConfigurator = toolChainConfigurator,
            qualityMetric = qualityMetric,
            repositoryConfiguration = repositories,
            junitConfiguration = junit,
            koin = koin,
        ).apply(project)

        // Then
        verify(exactly = 1) { javaPluginExtension.sourceCompatibility = version }
        verify(exactly = 1) { javaPluginExtension.targetCompatibility = version }
        verify(exactly = 1) { javaPluginExtension.withJavadocJar() }
        verify(exactly = 1) { javaPluginExtension.withSourcesJar() }
    }

    @Test
    fun `Given apply is called it sets up the compatibility and the toolchain for KMP`() {
        // Given
        val version = JavaVersion.toVersion(BuildConfig.javaToolchainVersion)
        val jvmToolchain: JavaToolchainSpec = mockk(relaxed = true)
        val javaPluginExtension: JavaPluginExtension = mockk(relaxed = true) {
            every { toolchain } returns jvmToolchain
        }
        val pluginContainer: PluginContainer = mockk(relaxed = true) {
            every { hasPlugin(any<String>()) } returns false
            every { hasPlugin(BuildConfig.kotlinMultiplatformId) } returns true
        }
        val project: Project = mockk(relaxed = true) {
            every { plugins } returns pluginContainer
            every { extensions.getByType(JavaPluginExtension::class.java) } returns mockk(relaxed = true)
        }

        // When
        JavaLibraryConfiguration(
            toolChainConfigurator = toolChainConfigurator,
            qualityMetric = qualityMetric,
            repositoryConfiguration = repositories,
            junitConfiguration = junit,
            koin = koin,
        ).apply(project)

        // Then
        verify(exactly = 0) { javaPluginExtension.sourceCompatibility = version }
        verify(exactly = 0) { javaPluginExtension.targetCompatibility = version }
        verify(exactly = 0) { javaPluginExtension.withJavadocJar() }
        verify(exactly = 0) { javaPluginExtension.withSourcesJar() }
    }
}
