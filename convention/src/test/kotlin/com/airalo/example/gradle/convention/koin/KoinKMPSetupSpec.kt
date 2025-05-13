package com.airalo.example.gradle.convention.koin

import com.airalo.example.gradle.config.BuildConfig
import com.airalo.example.gradle.convention.GradleUtilApiContract
import com.airalo.example.gradle.convention.GradleUtilApiContract.PlatformContext
import com.airalo.example.gradle.invokeGradleAction
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import java.util.Optional
import kotlin.test.assertTrue
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.internal.project.DefaultProject
import org.gradle.api.internal.tasks.TaskContainerInternal
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class KoinKMPSetupSpec {
    private val koinKspCompilerProvider: Optional<Provider<MinimalExternalModuleDependency>> = Optional.of(mockk(relaxed = true))
    private val koinBundleProvider: Optional<Provider<ExternalModuleDependencyBundle>> = Optional.of(mockk(relaxed = true))
    private val koinBomProvider: Optional<Provider<MinimalExternalModuleDependency>> = Optional.of(mockk(relaxed = true))
    private val fakeProject = ProjectBuilder.builder().build() as DefaultProject
    private val extensionContainer: ExtensionContainer = spyk(fakeProject.extensions)
    private val project: DefaultProject = spyk(fakeProject)

    @BeforeEach
    fun setup() {
        clearMocks(project)

        every {
            (project as Project).extensions
        } returns extensionContainer
        every {
            (project as Project).dependencies
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
        every {
            extensionContainer.getByType(KotlinMultiplatformExtension::class.java)
        } returns mockk {
            every { sourceSets.getByName(any()) } returns mockk(relaxed = true)
        }

        // When
        KoinKMP(mockk(relaxed = true)).apply(project)

        // Then
        verify {
            pluginContainer.apply(BuildConfig.kspId)
        }
    }

    @Test
    fun `Given it is multiplatform JVM project When apply is called it sets up the KSP`() {
        // Given
        every {
            extensionContainer.getByType(KotlinMultiplatformExtension::class.java)
        } returns mockk {
            every { sourceSets.getByName(any()) } returns mockk(relaxed = true)
        }

        val dependencies: DependencyHandler = mockk(relaxed = true)
        every { project.plugins } returns mockk(relaxed = true)
        every { project.dependencies } returns dependencies

        val platformContextResolver = GradleUtilApiContract.PlatformContextResolver {
            setOf(
                PlatformContext.JVM_LIBRARY_KMP,
            )
        }

        // When
        KoinKMP(platformContextResolver).apply(project)

        // Then
        verify {
            dependencies.add("kspCommonMainMetadata", koinKspCompilerProvider.get())
        }
        verify {
            dependencies.add("kspJvm", koinKspCompilerProvider.get())
        }
    }

    @Test
    fun `Given it is multiplatform Android project When apply is called it sets up the KSP`() {
        // Given
        every {
            extensionContainer.getByType(KotlinMultiplatformExtension::class.java)
        } returns mockk {
            every { sourceSets.getByName(any()) } returns mockk(relaxed = true)
        }

        val dependencies: DependencyHandler = mockk(relaxed = true)
        every { project.plugins } returns mockk(relaxed = true)
        every { project.dependencies } returns dependencies

        val platformContextResolver = GradleUtilApiContract.PlatformContextResolver {
            setOf(
                PlatformContext.ANDROID_LIBRARY_KMP,
            )
        }

        // When
        KoinKMP(platformContextResolver).apply(project)

        // Then
        verify {
            dependencies.add("kspCommonMainMetadata", koinKspCompilerProvider.get())
        }
        verify {
            dependencies.add("kspAndroidRelease", koinKspCompilerProvider.get())
        }
        verify {
            dependencies.add("kspAndroidDebug", koinKspCompilerProvider.get())
        }
    }

    @Test
    fun `Given it is a KMP Projects When apply is called it invokes Dependencies`() {
        // Given
        val dependencyHandler: KotlinDependencyHandler = mockk(relaxed = true)
        val executor = slot<KotlinDependencyHandler.() -> Unit>()
        val kmpExtension: KotlinMultiplatformExtension = mockk {
            every { sourceSets.getByName(any()) } returns mockk(relaxed = true)

            every { sourceSets.getByName("commonMain") } returns mockk {
                every { dependencies(capture(executor)) } answers {
                    executor.captured.invoke(dependencyHandler)
                }
            }
        }
        val dependencyScope: DependencyHandlerScope = mockk {
            every { add(any(), any()) } returns mockk()
        }
        every {
            dependencyScope.platform(any<Provider<MinimalExternalModuleDependency>>())
        } returns koinBomProvider.get()
        every {
            extensionContainer.getByType(KotlinMultiplatformExtension::class.java)
        } returns kmpExtension

        every { project.plugins } returns mockk(relaxed = true)
        every { project.dependencies } returns dependencyScope

        val platformContextResolver = GradleUtilApiContract.PlatformContextResolver {
            setOf(
                PlatformContext.ANDROID_LIBRARY_KMP,
            )
        }

        // When
        KoinKMP(platformContextResolver).apply(project)

        // Then
        verify(exactly = 1) {
            dependencyHandler.implementation(koinBundleProvider.get())
        }
        verify(exactly = 1) {
            dependencyHandler.implementation(koinBomProvider.get())
        }
    }

    @Test
    fun `Given it is a KMP Projects When apply is called it adds the sourceDirs for Android`() {
        // Given
        val sourceDirs: SourceDirectorySet = mockk(relaxed = true)
        val kmpExtension: KotlinMultiplatformExtension = mockk {
            every { sourceSets.getByName("commonMain") } returns mockk(relaxed = true)

            every { sourceSets.getByName("androidMain") } returns mockk {
                every { kotlin } returns sourceDirs
            }
        }
        every {
            extensionContainer.getByType(KotlinMultiplatformExtension::class.java)
        } returns kmpExtension

        every { project.plugins } returns mockk(relaxed = true)
        every { project.dependencies } returns mockk(relaxed = true)

        val platformContextResolver = GradleUtilApiContract.PlatformContextResolver {
            setOf(
                PlatformContext.ANDROID_LIBRARY_KMP,
            )
        }

        // When
        KoinKMP(platformContextResolver).apply(project)

        // Then
        verify {
            sourceDirs.srcDir("build/generated/ksp/android/androidDebug/kotlin")
        }
    }

    @Test
    fun `Given it is a KMP Projects When apply is called it adds the sourceDirs for Jvm`() {
        // Given
        val sourceDirs: SourceDirectorySet = mockk(relaxed = true)
        val kmpExtension: KotlinMultiplatformExtension = mockk {
            every { sourceSets.getByName("commonMain") } returns mockk(relaxed = true)

            every { sourceSets.getByName("jvmMain") } returns mockk {
                every { kotlin } returns sourceDirs
            }
        }
        every {
            extensionContainer.getByType(KotlinMultiplatformExtension::class.java)
        } returns kmpExtension

        every { project.plugins } returns mockk(relaxed = true)
        every { project.dependencies } returns mockk(relaxed = true)

        val platformContextResolver = GradleUtilApiContract.PlatformContextResolver {
            setOf(
                PlatformContext.JVM_LIBRARY_KMP,
            )
        }

        // When
        KoinKMP(platformContextResolver).apply(project)

        // Then
        verify {
            sourceDirs.srcDir("build/generated/ksp/jvm/kotlin")
        }
    }

    @Test
    fun `Given it is a KMP Projects When apply is called it wires the ksp Task dependencies for Android`() {
        // Given
        val releaseTaskProvider: TaskProvider<Task> = mockk()
        val releaseTask: Task = mockk(relaxed = true)
        val taskContainer: TaskContainerInternal = mockk(relaxed = true)
        val platformContextResolver = GradleUtilApiContract.PlatformContextResolver {
            setOf(
                PlatformContext.ANDROID_LIBRARY_KMP,
            )
        }
        every {
            extensionContainer.getByType(KotlinMultiplatformExtension::class.java)
        } returns mockk {
            every { sourceSets.getByName(any()) } returns mockk(relaxed = true)
        }

        every { (project as Project).tasks } returns taskContainer
        every {
            taskContainer.named("kspReleaseKotlinAndroid")
        } returns releaseTaskProvider
        invokeGradleAction(
            releaseTask,
        ) { task ->
            releaseTaskProvider.configure(task)
        }

        // When
        KoinKMP(platformContextResolver).apply(project)
        project.evaluate()

        // Then
        verify {
            releaseTask.dependsOn("kspDebugKotlinAndroid")
        }
    }

    @Test
    fun `It fulfils Plugin`() {
        assertTrue {
            @Suppress("USELESS_IS_CHECK")
            KoinKMP() is Plugin<*>
        }
    }
}
