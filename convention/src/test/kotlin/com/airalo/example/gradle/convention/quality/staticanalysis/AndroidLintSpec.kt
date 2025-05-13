package com.airalo.example.gradle.convention.quality.staticanalysis

import com.airalo.example.gradle.config.BuildConfig
import com.airalo.example.gradle.invokeGradleAction
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.Lint
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AndroidLintSpec {
    private lateinit var fakeProject: Project

    @BeforeEach
    fun setup() {
        fakeProject = ProjectBuilder.builder().build()
    }

    @Test
    fun `When apply is called and a project has a AndroidLibrary Plugin it configures the lint task`() {
        // Given
        fakeProject.plugins.apply(BuildConfig.androidLibraryId)
        val extension: Lint = fakeProject.extensions.getByType(LibraryExtension::class.java).lint
        val project: Project = mockk()

        invokeGradleAction(fakeProject) { probe ->
            project.afterEvaluate(probe)
        }

        // When
        AndroidLint.apply(project)

        // Then
        assertTrue(extension.abortOnError, message = "It must be aborted on errors.")
        assertFalse(extension.noLines, message = "It must not use source lines in the report.")
        assertTrue(extension.showAll, message = "It must include all in the report.")
        assertTrue(extension.textReport, message = "It must write a report.")
        assertNull(extension.textOutput, message = "textOutput must be unset.")
        assertFalse(extension.xmlReport, message = "It must not use xml.")
        assertTrue(extension.htmlReport, message = "It must use html.")
        assertTrue(extension.warningsAsErrors, message = "It must use warningsAsErrors.")
        assertTrue(extension.checkTestSources, message = "It must checkTestSources.")
        assertEquals(
            actual = extension.disable,
            expected = setOf("GifUsage", "GradleDependency"),
        )
        assertEquals(
            actual = extension.baseline,
            expected = fakeProject.file("lint-baseline.xml"),
        )
    }

    @Test
    fun `When apply is called and a project has a AndroidApplication Plugin it configures the lint task`() {
        // Given
        fakeProject.plugins.apply(BuildConfig.androidApplicationId)
        val extension: Lint = fakeProject.extensions.getByType(ApplicationExtension::class.java).lint
        val project: Project = mockk()

        invokeGradleAction(fakeProject) { probe ->
            project.afterEvaluate(probe)
        }

        // When
        AndroidLint.apply(project)

        // Then
        assertTrue(extension.abortOnError, message = "It must be aborted on errors.")
        assertFalse(extension.noLines, message = "It must not use source lines in the report.")
        assertTrue(extension.showAll, message = "It must include all in the report.")
        assertTrue(extension.textReport, message = "It must write a report.")
        assertNull(extension.textOutput, message = "textOutput must be unset.")
        assertFalse(extension.xmlReport, message = "It must not use xml.")
        assertTrue(extension.htmlReport, message = "It must use html.")
        assertTrue(extension.warningsAsErrors, message = "It must use warningsAsErrors.")
        assertTrue(extension.checkTestSources, message = "It must checkTestSources.")
        assertEquals(
            actual = extension.disable,
            expected = setOf("GifUsage", "GradleDependency"),
        )
        assertEquals(
            actual = extension.baseline,
            expected = fakeProject.file("lint-baseline.xml"),
        )
    }

    @Test
    fun `It fulfils the StaticAnalysisConventionContract`() {
        assertTrue {
            @Suppress("USELESS_IS_CHECK")
            AndroidLint is StaticAnalysisConventionContract
        }
    }
}
