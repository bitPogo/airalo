package com.airalo.example.gradle.convention.koin

import com.airalo.example.gradle.config.BuildConfig
import io.mockk.clearMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import java.util.Optional
import kotlin.test.assertTrue
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class KoinSetupSpec {
    private val koinKspCompilerProvider: Optional<Provider<MinimalExternalModuleDependency>> = Optional.of(mockk(relaxed = true))
    private val koinBomProvider: Optional<Provider<MinimalExternalModuleDependency>> = Optional.of(mockk(relaxed = true))
    private val koinBundleProvider: Optional<Provider<ExternalModuleDependencyBundle>> = Optional.of(mockk(relaxed = true))
    private val fakeProject: Project = ProjectBuilder.builder().build()
    private val extensionContainer: ExtensionContainer = spyk(fakeProject.extensions)
    private val project: Project = spyk(fakeProject)

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
            every { findLibrary("convention-koin-annotations-compiler") } returns koinKspCompilerProvider
            every { findBundle("convention-koin") } returns koinBundleProvider
            every { findLibrary("koin-bom") } returns koinBomProvider
        }
    }

    @Test
    fun `When apply is called it invokes KSP`() {
        // Given
        val pluginContainer: PluginContainer = mockk(relaxed = true)

        every { project.plugins } returns pluginContainer

        // When
        Koin().apply(project)

        // Then
        verify {
            pluginContainer.apply(BuildConfig.kspId)
        }
    }

    @Test
    fun `Given it is JVM Library project When apply is called it sets up the KSP`() {
        // Given
        val dependencies: DependencyHandler = mockk(relaxed = true)
        every { project.dependencies } returns dependencies

        // When
        Koin().apply(project)

        // Then
        verify(exactly = 1) {
            dependencies.add("ksp", koinKspCompilerProvider.get())
        }
    }

    @Test
    fun `Given it is Android Library project When apply is called it sets up the KSP`() {
        // Given
        val dependencies: DependencyHandler = mockk(relaxed = true)
        every { project.dependencies } returns dependencies

        // When
        Koin().apply(project)

        // Then
        verify(exactly = 1) {
            dependencies.add("ksp", koinKspCompilerProvider.get())
        }
    }

    @Test
    fun `Given it is Android Application project When apply is called it sets up the KSP`() {
        // Given
        val dependencies: DependencyHandler = mockk(relaxed = true)
        every { project.dependencies } returns dependencies

        // When
        Koin().apply(project)

        // Then
        verify(exactly = 1) {
            dependencies.add("ksp", koinKspCompilerProvider.get())
        }
    }

    @Test
    fun `Given it is not a KMP Projects When apply is called it invokes Dependencies`() {
        // Given
        val dependencyScope: DependencyHandlerScope = mockk {
            every { add(any(), any()) } returns mockk()
        }
        every { dependencyScope.platform(any<Provider<MinimalExternalModuleDependency>>()) } returns koinBomProvider.get()
        every { project.dependencies } returns dependencyScope

        // When
        Koin().apply(project)

        // Then
        verify(exactly = 1) {
            dependencyScope.add("implementation", koinBundleProvider.get())
        }
        verify(exactly = 1) {
            dependencyScope.add("implementation", koinBomProvider.get())
        }
    }

    @Test
    fun `Given it is a JVM Projects When apply is called it hooks in the sourceSets`() {
        // Given
        project.plugins.apply(BuildConfig.javaLibraryId)
        project.plugins.apply(BuildConfig.kotlinJvmId)
        val sourceDirs: SourceDirectorySet = mockk(relaxed = true)
        val jvmExtension: KotlinJvmProjectExtension = mockk {
            every { sourceSets.getByName("main") } returns mockk {
                every { kotlin } returns sourceDirs
            }
        }
        every {
            extensionContainer.getByType(KotlinJvmProjectExtension::class.java)
        } returns jvmExtension

        // When
        Koin().apply(project)

        // Then
        verify {
            sourceDirs.srcDir("build/generated/ksp/main/kotlin")
        }
    }

    @Test
    fun `Given it is a KMP Projects When apply is called it does nothing`() {
        // Given
        every { project.plugins } returns mockk {
            every { hasPlugin(BuildConfig.kmpId) } returns true
        }
        every { project.dependencies } returns mockk(relaxed = true)

        // When
        Koin().apply(project)

        // Then
        confirmVerified(project.dependencies)
    }

    @Test
    fun `It fulfils Plugin`() {
        assertTrue {
            @Suppress("USELESS_IS_CHECK")
            Koin() is Plugin<*>
        }
    }
}
