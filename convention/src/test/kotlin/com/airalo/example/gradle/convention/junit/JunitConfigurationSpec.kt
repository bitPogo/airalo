package com.airalo.example.gradle.convention.junit

import com.airalo.example.gradle.config.BuildConfig
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import java.util.Optional
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.junit.jupiter.api.Test

class JunitConfigurationSpec {
    private val junitProvider: Optional<Provider<ExternalModuleDependencyBundle>> = Optional.of(mockk(relaxed = true))
    private val extensionContainer: ExtensionContainer = mockk {
        every { getByType(VersionCatalogsExtension::class.java).named("testDependencyCatalog") } returns mockk<VersionCatalog> {
            every { findBundle("convention-junit5") } returns junitProvider
        }
    }

    @Test
    fun `When configure is called and the project is KMP it does nothing`() {
        // Given
        val dependencyHandler: DependencyHandler = mockk(relaxed = true)
        val project: Project = mockk {
            every { extensions } returns extensionContainer
            every { dependencies } returns dependencyHandler
            every { tasks } returns mockk(relaxed = true)
            every { plugins.hasPlugin(BuildConfig.kmpId) } returns true
        }

        // When
        JunitConfiguration.configure(project)

        // Then
        verify(exactly = 0) { dependencyHandler.add("testImplementation", junitProvider.get()) }
        confirmVerified(project.tasks)
    }

    @Test
    fun `When configure is called adds Junit5 Dependencies`() {
        // Given
        val dependencyHandler: DependencyHandler = mockk(relaxed = true)
        val project: Project = mockk {
            every { extensions } returns extensionContainer
            every { dependencies } returns dependencyHandler
            every { tasks } returns mockk(relaxed = true)
            every { plugins.hasPlugin(BuildConfig.kmpId) } returns false
        }

        // When
        JunitConfiguration.configure(project)

        // Then
        verify(exactly = 1) { dependencyHandler.add("testImplementation", junitProvider.get()) }
    }

    @Test
    fun `When configure is called sets up Junit5 for the test task`() {
        // Given
        val testTask: org.gradle.api.tasks.testing.Test = mockk(relaxed = true)
        val testTaskConfigurator = slot<Action<org.gradle.api.tasks.testing.Test>>()
        val project: Project = mockk {
            every { extensions } returns extensionContainer
            every { plugins.hasPlugin(BuildConfig.kmpId) } returns false
            every { dependencies } returns mockk(relaxed = true)

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
        } returns mockk(relaxed = true)

        // When
        JunitConfiguration.configure(project)

        // Then
        verify(exactly = 1) { testTask.useJUnitPlatform() }
    }
}
