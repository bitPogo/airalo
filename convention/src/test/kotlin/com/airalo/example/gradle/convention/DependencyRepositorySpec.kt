package com.airalo.example.gradle.convention

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlin.test.assertTrue
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.junit.jupiter.api.Test

class DependencyRepositorySpec {
    @Test
    fun `When configure is called it adds dependency repositories to the given project`() {
        // Given
        val customUrl = slot<Action<MavenArtifactRepository>>()
        val repositoryHandler: RepositoryHandler = mockk(relaxed = true) {
            every { maven(capture(customUrl)) } returns mockk()
        }
        val project: Project = mockk {
            every { repositories } returns repositoryHandler
        }

        // When
        DependencyRepository.configure(project)

        // Then
        verify(exactly = 1) {
            repositoryHandler.google()
        }
        verify(exactly = 1) {
            repositoryHandler.gradlePluginPortal()
        }
        verify(exactly = 1) {
            repositoryHandler.mavenCentral()
        }
        customUrl.captured.execute(
            mockk {
                every { setUrl("https://maven.pkg.jetbrains.space/public/p/compose/dev") } just Runs
                every { setUrl("https://maven.appspector.com/artifactory/android-sdk") } just Runs
            },
        )
    }

    @Test
    fun `It fulfils DependencyRepositoryContract`() {
        assertTrue {
            @Suppress("USELESS_IS_CHECK")
            DependencyRepository is DependencyRepositoryProvider
        }
    }
}
