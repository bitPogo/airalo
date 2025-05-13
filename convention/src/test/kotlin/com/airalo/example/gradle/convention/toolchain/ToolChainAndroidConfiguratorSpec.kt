package com.airalo.example.gradle.convention.toolchain

import com.airalo.example.gradle.config.BuildConfig
import com.airalo.example.gradle.convention.android.ConfigurationContract
import com.airalo.example.gradle.invokeGradleAction
import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlin.test.assertTrue
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskContainer
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaLauncher
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.jvm.toolchain.JavaToolchainSpec
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinJavaToolchain
import org.junit.jupiter.api.Test

class ToolChainAndroidConfiguratorSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `When configure is called it setup up the ToolChain for the JavaExtension`() {
        // Given
        val jvmToolchain: JavaToolchainSpec = mockk(relaxed = true)
        val javaPluginExtension: JavaPluginExtension = mockk(relaxed = true) {
            every { toolchain } returns jvmToolchain
        }
        val project: Project = mockk {
            every { tasks } returns mockk(relaxed = true)
            every { plugins } returns mockk(relaxed = true)
            every { extensions.getByType(JavaPluginExtension::class.java) } returns javaPluginExtension
        }

        // When
        ToolChainAndroidConfigurator.configure(project)

        // Then
        verify(exactly = 1) { jvmToolchain.languageVersion.set(JavaLanguageVersion.of(BuildConfig.androidToolchainVersion)) }
    }

    @Test
    fun `When configure is called ignores all explict Jvm ToolChains`() {
        // Given
        val jvmTask: KotlinCompile = mockk {
            every { name } returns "${fixture<String>()}KotlinJvm"
        }
        val taskContainer: TaskContainer = mockk()
        val project: Project = mockk {
            every { tasks } returns taskContainer
            every { extensions.getByType(JavaPluginExtension::class.java) } returns mockk(relaxed = true)
        }

        invokeGradleAction(
            jvmTask,
            mockk(),
        ) { probe ->
            taskContainer.withType(KotlinCompile::class.java, probe)
        }

        // When
        ToolChainAndroidConfigurator.configure(project)

        // Then
        verify(exactly = 2) { jvmTask.name }
        confirmVerified(jvmTask)
    }

    @Test
    fun `When configure is called ignores all implicit Jvm ToolChains`() {
        // Given
        val jvmTask: KotlinCompile = mockk {
            every { name } returns fixture<String>()
        }
        val taskContainer: TaskContainer = mockk()
        val project: Project = mockk {
            every { tasks } returns taskContainer
            every { extensions.getByType(JavaPluginExtension::class.java) } returns mockk(relaxed = true)
        }

        invokeGradleAction(
            jvmTask,
            mockk(),
        ) { probe ->
            taskContainer.withType(KotlinCompile::class.java, probe)
        }

        // When
        ToolChainAndroidConfigurator.configure(project)

        // Then
        verify(exactly = 2) { jvmTask.name }
        confirmVerified(jvmTask)
    }

    @Test
    fun `When configure is called configures all plain Jvm ToolChains`() {
        // Given
        val toolChain: KotlinJavaToolchain.JavaToolchainSetter = mockk(relaxed = true)
        val provider: Provider<JavaLauncher> = mockk()
        val jvmTask: KotlinCompile = mockk(relaxed = true) {
            every { name } returns "${fixture<String>()}Kotlin"
            every { kotlinJavaToolchain.toolchain } returns toolChain
        }
        val taskContainer: TaskContainer = mockk()
        val toolchainConfiguration = slot<Action<in JavaToolchainSpec>>()
        val toolchainService: JavaToolchainService = mockk {
            every { launcherFor(capture(toolchainConfiguration)) } returns provider
        }
        val project: Project = mockk {
            every { tasks } returns taskContainer
            every { extensions.getByType(JavaToolchainService::class.java) } returns toolchainService
            every { extensions.getByType(JavaPluginExtension::class.java) } returns mockk(relaxed = true)
        }

        invokeGradleAction(
            jvmTask,
            mockk(),
        ) { probe ->
            taskContainer.withType(KotlinCompile::class.java, probe)
        }

        // When
        ToolChainAndroidConfigurator.configure(project)

        // Then
        verify(exactly = 1) { toolChain.use(provider) }
        val proof: JavaToolchainSpec = mockk(relaxed = true)
        toolchainConfiguration.captured.execute(proof)
        verify(exactly = 1) {
            proof.languageVersion.set(JavaLanguageVersion.of(BuildConfig.androidToolchainVersion))
        }
    }

    @Test
    fun `When configure is called configures all plain Android ToolChains`() {
        // Given
        val toolChain: KotlinJavaToolchain.JavaToolchainSetter = mockk(relaxed = true)
        val provider: Provider<JavaLauncher> = mockk()
        val jvmTask: KotlinCompile = mockk(relaxed = true) {
            every { name } returns "${fixture<String>()}KotlinAndroid"
            every { kotlinJavaToolchain.toolchain } returns toolChain
        }
        val taskContainer: TaskContainer = mockk()
        val toolchainConfiguration = slot<Action<in JavaToolchainSpec>>()
        val toolchainService: JavaToolchainService = mockk {
            every { launcherFor(capture(toolchainConfiguration)) } returns provider
        }
        val project: Project = mockk {
            every { tasks } returns taskContainer
            every { extensions.getByType(JavaToolchainService::class.java) } returns toolchainService
            every { extensions.getByType(JavaPluginExtension::class.java) } returns mockk(relaxed = true)
        }

        invokeGradleAction(
            jvmTask,
            mockk(),
        ) { probe ->
            taskContainer.withType(KotlinCompile::class.java, probe)
        }

        // When
        ToolChainAndroidConfigurator.configure(project)

        // Then
        verify(exactly = 1) { toolChain.use(provider) }
        val proof: JavaToolchainSpec = mockk(relaxed = true)
        toolchainConfiguration.captured.execute(proof)
        verify(exactly = 1) {
            proof.languageVersion.set(JavaLanguageVersion.of(BuildConfig.androidToolchainVersion))
        }
    }

    @Test
    fun `It fulfils ParameterlessConfigurator`() {
        val configurator = ToolChainAndroidConfigurator as Any

        assertTrue { configurator is ConfigurationContract.ParameterlessConfigurator }
    }
}
