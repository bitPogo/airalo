package com.airalo.example.gradle.convention.kmp

import com.airalo.example.gradle.config.BuildConfig
import com.android.build.api.dsl.LibraryExtension
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.invoke
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyOrder
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
import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class KmpAndroidWithComposeConfigurationSpec {
    private val fakeProject: Project = ProjectBuilder.builder().build().also {
        it.plugins.apply(BuildConfig.androidLibraryId)
        it.plugins.apply(BuildConfig.kmpId)
        it.plugins.apply(BuildConfig.jbComposeId)
        it.extensions.getByType(LibraryExtension::class.java).apply {
            compileSdk = 34
            namespace = "test"
        }
        it.extensions.getByType(KotlinMultiplatformExtension::class.java).apply {
            androidTarget()
        }
    }
    private val project: Project = spyk(fakeProject)
    private val extensionContainer: ExtensionContainer = spyk(fakeProject.extensions)
    private val composeVersion: Optional<VersionConstraint> = Optional.of(
        mockk {
            every { this@mockk.toString() } returns "1.5.14"
        },
    )
    private val roborazziTestProvider: Optional<Provider<ExternalModuleDependencyBundle>> = mockk(relaxed = true)
    private val sourceSet: KotlinSourceSet = mockk()

    @BeforeEach
    fun setup() {
        clearMocks(project, extensionContainer, sourceSet)

        every {
            project.extensions
        } returns extensionContainer

        val kotlinExtension = project.extensions.getByType(KotlinMultiplatformExtension::class.java)
        val extension = spyk(kotlinExtension) {
            every { sourceSets } returns spyk(kotlinExtension.sourceSets) {
                every { getByName("androidUnitTest") } returns sourceSet
            }
        }

        every { extensionContainer.getByType(KotlinMultiplatformExtension::class.java) } returns extension
        every { sourceSet.dependencies(captureLambda<KotlinDependencyHandler.() -> Unit>()) } just Runs

        every {
            extensionContainer.getByType(VersionCatalogsExtension::class.java).named("dependencyCatalog")
        } returns mockk<VersionCatalog> {
            every { findVersion("compose-compiler") } returns composeVersion
        }
        every {
            extensionContainer.getByType(VersionCatalogsExtension::class.java).named("testDependencyCatalog")
        } returns mockk<VersionCatalog> {
            every { findBundle("convention-roborazzi") } returns roborazziTestProvider
        }

        project.tasks.create("explodeCodeSourceDebug")
        project.tasks.create("explodeCodeSourceRelease")
    }

    @Test
    fun `Given apply is called it applies KmpConvention Plugin, ComposeConvention and the JetbrainsCompose Plugin`() {
        // Given
        val kmpConfiguration: Plugin<Project> = mockk(relaxed = true)
        val pluginContainer: PluginContainer = mockk(relaxed = true)
        every { project.plugins } returns pluginContainer

        // When
        KmpAndroidWithComposeConfiguration(
            kmpAndroidConvention = kmpConfiguration,
        ).apply(project)

        // Then
        verifyOrder {
            kmpConfiguration.apply(project)
            pluginContainer.apply(BuildConfig.jbComposeId)
            pluginContainer.apply(BuildConfig.roborazziId)
        }
    }

    @Test
    fun `When configure is called it adds compose dependencies`() {
        // Given
        clearMocks(sourceSet)
        val dependencyHandler: KotlinDependencyHandler = mockk(relaxed = true)
        every { sourceSet.dependencies(captureLambda<KotlinDependencyHandler.() -> Unit>()) } answers {
            lambda<(KotlinDependencyHandler) -> Unit>().invoke(dependencyHandler)
        }

        // When
        KmpAndroidWithComposeConfiguration(
            kmpAndroidConvention = mockk(relaxed = true),
        ).apply(project)

        // Then
        verify(exactly = 1) {
            dependencyHandler.implementation(roborazziTestProvider.get())
        }
    }

    @Test
    fun `When apply is called it configures Compose`() {
        // When
        KmpAndroidWithComposeConfiguration(
            kmpAndroidConvention = mockk(relaxed = true),
        ).apply(project)
        // Then
        val extension = project.extensions.getByType(LibraryExtension::class.java)

        assertTrue { extension.buildFeatures.compose ?: false }
    }

    @Test
    fun `When configure is called it sets up compose metrics`() {
        // When
        KmpAndroidWithComposeConfiguration(
            kmpAndroidConvention = mockk(relaxed = true),
        ).apply(project)

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
        KmpAndroidWithComposeConfiguration(
            kmpAndroidConvention = mockk(relaxed = true),
        ).apply(project)

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
    fun `When configure is called it fixes compose task dependencies with android`() {
        // When
        KmpAndroidWithComposeConfiguration(
            kmpAndroidConvention = mockk(relaxed = true),
        ).apply(project)

        // Then
        project.afterEvaluate {
            assertEquals(
                actual = project.tasks.named("explodeCodeSourceDebug").get().mustRunAfter.getDependencies(null).map { it.name },
                expected = listOf("generateActualResourceCollectorsForAndroidMain"),
            )

            assertEquals(
                actual = project.tasks.named("explodeCodeSourceRelease").get().mustRunAfter.getDependencies(null).map { it.name },
                expected = listOf("generateActualResourceCollectorsForAndroidMain"),
            )
        }
        (project as DefaultProject).evaluate()
    }

    @Test
    fun `It fulfils Plugin`() {
        assertTrue {
            @Suppress("USELESS_IS_CHECK")
            KmpAndroidWithComposeConfiguration() is Plugin<*>
        }
    }
}
