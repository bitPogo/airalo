package com.airalo.example.gradle.convention.android.android

import com.airalo.example.gradle.config.BuildConfig
import com.airalo.example.gradle.convention.android.AndroidConfigurationApiContract.Companion.TARGET_SDK
import com.airalo.example.gradle.convention.android.ConfigurationContract
import com.airalo.example.gradle.invokeGradleAction
import com.android.build.api.dsl.ApplicationBuildFeatures
import com.android.build.api.dsl.ApplicationDefaultConfig
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildFeatures
import com.android.build.api.dsl.VectorDrawables
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.invoke
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AndroidApplicationConfiguratorSpec {
    private val common: CommonAndroidConfigurator = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        clearMocks(common)
    }

    @Test
    fun `It fulfils ParameterlessConfigurator`() {
        val configurator: Any = AndroidApplicationConfigurator(common)

        @Suppress("KotlinConstantConditions")
        assertTrue(configurator is ConfigurationContract.ParameterlessConfigurator)
    }

    @Test
    fun `When configure is called with a Project, it delegates the call to CommonAndroidConfigurator`() {
        // Given
        val common: CommonAndroidConfigurator = mockk(relaxed = true)

        val project: Project = mockk()

        // When
        val extensions: ExtensionContainer = mockk()
        val applicationExtension: ApplicationExtension = mockk(relaxed = true)

        every { project.extensions } returns extensions
        every { extensions.configure(any<Class<Any>>(), any()) } returns mockk()

        invokeGradleAction(applicationExtension) { probe ->
            extensions.configure(ApplicationExtension::class.java, probe)
        }

        // When
        AndroidApplicationConfigurator(common).configure(project)

        // Then
        verify(exactly = 1) {
            common.configure(project, applicationExtension)
        }
    }

    @Test
    fun `When configure is called with a Project and a AndroidApplicationConfiguration, it setups up the DefaultBuildTypeConfiguration`() {
        // Given
        val project: Project = mockk()

        val extensions: ExtensionContainer = mockk()
        val applicationExtension: ApplicationExtension = mockk(relaxed = true)
        val defaultConfiguration: ApplicationDefaultConfig = mockk(relaxed = true)
        val vectorDrawablesConfiguration: VectorDrawables = mockk(relaxed = true)

        every { project.extensions } returns extensions
        every { extensions.configure(any<Class<Any>>(), any()) } returns mockk()

        invokeGradleAction(applicationExtension) { probe ->
            extensions.configure(ApplicationExtension::class.java, probe)
        }

        every { applicationExtension.defaultConfig(captureLambda()) } answers {
            lambda<(ApplicationDefaultConfig) -> Unit>().invoke(defaultConfiguration)
        }

        every { defaultConfiguration.vectorDrawables(captureLambda()) } answers {
            lambda<(VectorDrawables) -> Unit>().invoke(vectorDrawablesConfiguration)
        }

        // When
        AndroidApplicationConfigurator(common).configure(project)

        // Then
        verify(exactly = 1) { defaultConfiguration.targetSdk = TARGET_SDK }
        verify(exactly = 1) { vectorDrawablesConfiguration.useSupportLibrary = true }
    }

    @Test
    fun `When configure is called with a Project and a AndroidApplicationConfiguration, it setups up the BuildFeatures`() {
        // Given
        val project: Project = mockk()

        val extensions: ExtensionContainer = mockk()
        val applicationExtension: ApplicationExtension = mockk(relaxed = true)
        val buildFeaturesConfiguration: ApplicationBuildFeatures = mockk(relaxed = true)

        every { project.extensions } returns extensions
        every { extensions.configure(any<Class<Any>>(), any()) } returns mockk()

        invokeGradleAction(applicationExtension) { probe ->
            extensions.configure(ApplicationExtension::class.java, probe)
        }

        every { applicationExtension.buildFeatures(captureLambda()) } answers {
            lambda<(BuildFeatures) -> Unit>().invoke(buildFeaturesConfiguration)
        }

        // When
        AndroidApplicationConfigurator(common).configure(project)

        // Then
        verify(exactly = 1) { buildFeaturesConfiguration.viewBinding = true }
    }

    @Test
    fun `When configure is called with a Project and a AndroidApplicationConfiguration, it enables Proguard Minification`() {
        // Given
        val project: Project = ProjectBuilder.builder().build().also {
            it.plugins.apply(BuildConfig.androidApplicationId)
        }
        val extension = project.extensions.getByType(ApplicationExtension::class.java).also {
            it.namespace = "test"
        }

        // When
        AndroidApplicationConfigurator(common).configure(project)

        // Then
        val release = extension.buildTypes.named("release").get()
        val debug = extension.buildTypes.named("debug").get()

        assertTrue { release.isMinifyEnabled }
        assertTrue { release.isShrinkResources }

        assertFalse { debug.isMinifyEnabled }
        assertFalse { debug.isShrinkResources }
    }
}
