package com.airalo.example.gradle.convention.android

import com.airalo.example.gradle.config.BuildConfig
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import java.io.File
import java.util.Optional
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.api.internal.project.DefaultProject
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ComposeSpec {
    private val fakeProject: Project = ProjectBuilder.builder().build().also {
        it.plugins.apply(BuildConfig.androidApplicationId)
        it.plugins.apply(BuildConfig.kotlinAndroidId)
        it.plugins.apply(BuildConfig.composeCompilerId)
        it.extensions.getByType(ApplicationExtension::class.java).apply {
            compileSdk = 34
            namespace = "test"
        }
    }
    private val extensionContainer: ExtensionContainer = spyk(fakeProject.extensions)
    private val project: Project = spyk(fakeProject)
    private val composeProvider: Optional<Provider<ExternalModuleDependencyBundle>> = mockk(relaxed = true)
    private val composeDebugProvider: Optional<Provider<ExternalModuleDependencyBundle>> = mockk(relaxed = true)
    private val composeInstrumentedTestProvider: Optional<Provider<ExternalModuleDependencyBundle>> = mockk(relaxed = true)
    private val roborazziTestProvider: Optional<Provider<ExternalModuleDependencyBundle>> = mockk(relaxed = true)
    private val composeVersion: Optional<VersionConstraint> = Optional.of(
        mockk {
            every { this@mockk.toString() } returns "1.5.14"
        },
    )

    @BeforeEach
    fun setup() {
        clearMocks(project)

        every {
            project.extensions
        } returns extensionContainer
        every {
            project.dependencies
        } returns mockk(relaxed = true)

        every {
            extensionContainer.getByType(VersionCatalogsExtension::class.java).named("dependencyCatalog")
        } returns mockk<VersionCatalog> {
            every { findVersion("compose-compiler") } returns composeVersion
            every { findBundle("convention-compose") } returns composeProvider
            every { findBundle("convention-debug-compose") } returns composeDebugProvider
        }
        every {
            extensionContainer.getByType(VersionCatalogsExtension::class.java).named("testDependencyCatalog")
        } returns mockk<VersionCatalog> {
            every { findBundle("convention-instrumented-compose") } returns composeInstrumentedTestProvider
            every { findBundle("convention-roborazzi") } returns roborazziTestProvider
        }
    }

    @Test
    fun `When configure is called it applies the Compiler Plugin`() {
        // Given
        val pluginContainer: PluginContainer = mockk(relaxed = true)
        every { project.plugins } returns pluginContainer

        // When
        ComposeConfiguration().apply(project)

        // Then
        verify(exactly = 1) {
            pluginContainer.apply(BuildConfig.composeCompilerId)
            pluginContainer.apply(BuildConfig.roborazziId)
        }
    }

    @Test
    fun `When configure is called it adds compose dependencies`() {
        // Given
        val dependencyScope: DependencyHandlerScope = mockk(relaxed = true)
        every { project.dependencies } returns dependencyScope

        project.extensions.getByType(CommonExtension::class.java)

        // When
        ComposeConfiguration().apply(project)

        // Then
        verify(exactly = 1) {
            dependencyScope.add("implementation", composeProvider.get())
        }
        verify(exactly = 1) {
            dependencyScope.add("testImplementation", roborazziTestProvider.get())
        }
        verify(exactly = 1) {
            dependencyScope.add("debugImplementation", composeDebugProvider.get())
        }
        verify(exactly = 1) {
            dependencyScope.add("androidTestImplementation", composeInstrumentedTestProvider.get())
        }
    }

    @Test
    fun `When configure is called it sets up compose`() {
        // When
        ComposeConfiguration().apply(project)

        // Then
        val extension = project.extensions.getByType(ApplicationExtension::class.java)

        assertTrue { extension.buildFeatures.compose ?: false }
    }

    @Test
    fun `When configure is called it sets up compose metrics`() {
        // When
        ComposeConfiguration().apply(project)

        // Then
        val metricDir = File(fakeProject.layout.buildDirectory.asFile.get(), "compose-metrics")
        project.afterEvaluate {
            assertEquals(
                actual = project.tasks.withType(KotlinCompile::class.java)
                    .first()
                    .compilerOptions.freeCompilerArgs.get()
                    .toList()
                    .subList(0, 2)
                    .joinToString(),
                expected = "-P, plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${metricDir.absolutePath}",
            )
        }
        (project as DefaultProject).evaluate()
    }

    @Test
    fun `When configure is called it sets up compose metrics report`() {
        // When
        ComposeConfiguration().apply(project)

        // Then
        val metricDir = File(fakeProject.layout.buildDirectory.asFile.get(), "reports/compose")
        project.afterEvaluate {
            assertEquals(
                actual = project.tasks.withType(KotlinCompile::class.java)
                    .first()
                    .compilerOptions.freeCompilerArgs.get()
                    .toList()
                    .subList(2, 4)
                    .joinToString(),
                expected = "-P, plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${metricDir.absolutePath}",
            )
        }
        (project as DefaultProject).evaluate()
    }

    @Test
    fun `It fulfils Plugin`() {
        val configurator = ComposeConfiguration() as Any

        assertTrue {
            @Suppress("KotlinConstantConditions")
            configurator is Plugin<*>
        }
    }
}
