package com.airalo.example.gradle.convention.quality.dependency

import com.airalo.example.gradle.config.BuildConfig
import com.airalo.example.gradle.invokeGradleAction
import com.autonomousapps.DependencyAnalysisExtension
import com.autonomousapps.extension.Issue
import com.autonomousapps.extension.IssueHandler
import com.autonomousapps.extension.ProjectIssueHandler
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DependencyHealthSpec {
    private lateinit var rootProject: Project
    private lateinit var subProject: Project

    @BeforeEach
    fun setUp() {
        rootProject = ProjectBuilder.builder().build()
        subProject = ProjectBuilder.builder().withParent(rootProject).build()
    }

    @Test
    fun `When apply is called it applies the DependencyAnalysis Plugin to the root project`() {
        // When
        DependencyHealth.apply(rootProject)

        // Then
        assertTrue(
            rootProject.plugins.hasPlugin(BuildConfig.dependencyHealthId),
        )
    }

    @Test
    fun `When apply is called it applies the DependencyAnalysis Plugin to subprojects`() {
        // When
        DependencyHealth.apply(rootProject)

        // Then
        assertTrue(
            subProject.plugins.hasPlugin(BuildConfig.dependencyHealthId),
        )
    }

    @Test
    fun `When apply is called it applies it configures the DependencyAnalysisExtension`() {
        // Given
        val issueHandler: IssueHandler = mockk()
        val projectIssues: ProjectIssueHandler = mockk()
        val extension: DependencyAnalysisExtension = mockk()
        val onUnusedDependencies: Issue = mockk(relaxed = true)
        val onUsedTransitiveDependencies: Issue = mockk(relaxed = true)
        val onIncorrectConfiguration: Issue = mockk(relaxed = true)
        val onRedundantPlugins: Issue = mockk(relaxed = true)
        val onCompileOnly: Issue = mockk(relaxed = true)
        val onRuntimeOnly: Issue = mockk(relaxed = true)
        val onUnusedAnnotationProcessors: Issue = mockk(relaxed = true)

        val project: Project = mockk(relaxed = true) {
            every { extensions.getByType(DependencyAnalysisExtension::class.java) } returns extension
        }

        invokeGradleAction(
            issueHandler,
        ) { probe ->
            extension.issues(probe)
        }
        invokeGradleAction(projectIssues) { probe ->
            issueHandler.all(probe)
        }

        invokeGradleAction(onUnusedDependencies) { probe ->
            projectIssues.onUnusedDependencies(probe)
        }
        invokeGradleAction(onIncorrectConfiguration) { probe ->
            projectIssues.onIncorrectConfiguration(probe)
        }
        invokeGradleAction(onUsedTransitiveDependencies) { probe ->
            projectIssues.onUsedTransitiveDependencies(probe)
        }
        invokeGradleAction(onRedundantPlugins) { probe ->
            projectIssues.onRedundantPlugins(probe)
        }
        invokeGradleAction(onCompileOnly) { probe ->
            projectIssues.onCompileOnly(probe)
        }
        invokeGradleAction(onRuntimeOnly) { probe ->
            projectIssues.onRuntimeOnly(probe)
        }
        invokeGradleAction(onUnusedAnnotationProcessors) { probe ->
            projectIssues.onUnusedAnnotationProcessors(probe)
        }
        // When
        DependencyHealth.apply(project)

        // Then
        verify(exactly = 1) { onUnusedDependencies.severity("fail") }
        verify(exactly = 1) {
            onUnusedDependencies.exclude(
                "org.jetbrains.kotlin:kotlin-stdlib",
                "org.junit.jupiter:junit-jupiter-api",
                "io.insert-koin:koin-core",
                "io.insert-koin:koin-annotations",
            )
        }
        verify(exactly = 1) { onIncorrectConfiguration.severity("warn") }
        verify(exactly = 1) { onUsedTransitiveDependencies.severity("ignore") }
        verify(exactly = 1) { onRedundantPlugins.severity("fail") }
        verify(exactly = 1) {
            onRedundantPlugins.exclude(
                "org.jetbrains.kotlin.jvm",
                "java-library",
            )
        }
        verify(exactly = 1) { onCompileOnly.severity("fail") }
        verify(exactly = 1) { onRuntimeOnly.severity("fail") }
        verify(exactly = 1) { onUnusedAnnotationProcessors.severity("fail") }

        confirmVerified(
            onUnusedDependencies,
            onIncorrectConfiguration,
            onUsedTransitiveDependencies,
            onRedundantPlugins,
            onCompileOnly,
            onRuntimeOnly,
            onUnusedAnnotationProcessors,
        )
    }

    @Test
    fun `DependencyHealthConfig fulfils the DependencyConventionContract`() {
        assertTrue {
            @Suppress("USELESS_IS_CHECK")
            DependencyHealth is DependencyConventionContract
        }
    }
}
