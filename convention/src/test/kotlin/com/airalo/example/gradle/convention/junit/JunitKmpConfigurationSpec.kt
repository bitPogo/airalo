package com.airalo.example.gradle.convention.junit

import com.airalo.example.gradle.config.BuildConfig
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import java.util.Optional
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.junit.jupiter.api.Test

class JunitKmpConfigurationSpec {
    private val junitProvider: Optional<Provider<ExternalModuleDependencyBundle>> = Optional.of(mockk(relaxed = true))
    private val commonProvider: Optional<Provider<MinimalExternalModuleDependency>> = Optional.of(mockk(relaxed = true))
    private val extensionContainer: ExtensionContainer = mockk {
        every { getByType(VersionCatalogsExtension::class.java).named("testDependencyCatalog") } returns mockk<VersionCatalog> {
            every { findLibrary("kotlin-common") } returns commonProvider
            every { findBundle("convention-junit5") } returns junitProvider
        }
    }

    @Test
    fun `When configure is called and the project is Android it adds Junit5 Dependencies`() {
        // Given
        val dependencyHandler: KotlinDependencyHandler = mockk(relaxed = true)
        val executor = slot<KotlinDependencyHandler.() -> Unit>()
        val kmpExtension: KotlinMultiplatformExtension = mockk {
            every { sourceSets.getByName("androidUnitTest") } returns mockk {
                every { dependencies(capture(executor)) } answers {
                    executor.captured.invoke(dependencyHandler)
                }
            }

            every { sourceSets.getByName("commonTest") } returns mockk {
                every { dependencies(capture(executor)) } answers {
                    executor.captured.invoke(dependencyHandler)
                }
            }
        }
        every {
            extensionContainer.getByType(KotlinMultiplatformExtension::class.java)
        } returns kmpExtension

        val project: Project = mockk {
            every { extensions } returns extensionContainer
            every { plugins.hasPlugin(BuildConfig.kmpId) } returns true
            every { plugins.hasPlugin(BuildConfig.androidLibraryId) } returns true
            every { tasks } returns mockk(relaxed = true)
        }

        // When
        JunitKmpConfiguration.configure(project)

        // Then
        verify(exactly = 1) { dependencyHandler.implementation(commonProvider.get()) }
        verify(exactly = 1) { dependencyHandler.implementation(junitProvider.get()) }
    }

    @Test
    fun `When configure is called and the project is Jvm it adds Junit5 Dependencies`() {
        // Given
        val dependencyHandler: KotlinDependencyHandler = mockk(relaxed = true)
        val executor = slot<KotlinDependencyHandler.() -> Unit>()
        val kmpExtension: KotlinMultiplatformExtension = mockk {
            every { sourceSets.getByName("jvmTest") } returns mockk {
                every { dependencies(capture(executor)) } answers {
                    executor.captured.invoke(dependencyHandler)
                }
            }

            every { sourceSets.getByName("commonTest") } returns mockk {
                every { dependencies(capture(executor)) } answers {
                    executor.captured.invoke(dependencyHandler)
                }
            }
        }
        every {
            extensionContainer.getByType(KotlinMultiplatformExtension::class.java)
        } returns kmpExtension

        val project: Project = mockk {
            every { extensions } returns extensionContainer
            every { plugins.hasPlugin(BuildConfig.kmpId) } returns true
            every { plugins.hasPlugin(BuildConfig.androidLibraryId) } returns false
            every { tasks } returns mockk(relaxed = true)
        }

        // When
        JunitKmpConfiguration.configure(project)

        // Then
        verify(exactly = 1) { dependencyHandler.implementation(junitProvider.get()) }
        verify(exactly = 1) { dependencyHandler.implementation(junitProvider.get()) }
    }

    @Test
    fun `When configure is called sets up Junit5 for the test task`() {
        // Given
        val testTask: org.gradle.api.tasks.testing.Test = mockk(relaxed = true)
        val testTaskConfigurator = slot<Action<org.gradle.api.tasks.testing.Test>>()
        val project: Project = mockk {
            every { extensions } returns extensionContainer
            every { plugins } returns mockk(relaxed = true)

            every {
                tasks.withType(
                    org.gradle.api.tasks.testing.Test::class.java,
                    capture(testTaskConfigurator),
                )
            } answers {
                testTaskConfigurator.captured.invoke(testTask)
                mockk()
            }
        }

        every {
            extensionContainer.getByType(KotlinMultiplatformExtension::class.java)
        } returns mockk {
            every { sourceSets.getByName(any()) } returns mockk {
                every { dependencies(any<KotlinDependencyHandler.() -> Unit>()) } returns mockk(relaxed = true)
            }
        }

        // When
        JunitKmpConfiguration.configure(project)

        // Then
        verify(exactly = 1) { testTask.useJUnitPlatform() }
    }
}
