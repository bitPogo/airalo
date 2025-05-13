package com.airalo.example.gradle.convention.quality.staticanalysis

import com.airalo.example.gradle.config.BuildConfig
import com.appmattus.kotlinfixture.kotlinFixture
import io.gitlab.arturbosch.detekt.Detekt as DetektTask
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.TaskCollection
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DetektSpec {
    private val fixture = kotlinFixture()
    private lateinit var fakeProject: Project

    @BeforeEach
    fun setup() {
        fakeProject = ProjectBuilder.builder().build()
    }

    @Test
    fun `When apply is called it applies the detekt gradle plugin`() {
        // Given
        val pluginContainer: PluginContainer = mockk(relaxed = true)
        val project: Project = mockk(relaxed = true) {
            every { plugins } returns pluginContainer
        }

        // When
        Detekt.apply(project)

        // Then
        verify(exactly = 1) {
            pluginContainer.apply(BuildConfig.detektId)
        }
    }

    @Test
    fun `When apply is called it configures the DetektExtension`() {
        // Given
        val rootProjectDir: File = fixture()
        fakeProject.plugins.apply(BuildConfig.detektId)
        val detektExtension: DetektExtension = fakeProject.extensions.getByType(DetektExtension::class.java)
        val project: Project = spyk(fakeProject) {
            every { rootDir } returns rootProjectDir
        }

        // When
        Detekt.apply(project)

        // Then
        assertEquals(
            actual = detektExtension.toolVersion,
            expected = BuildConfig.detektVersion,
        )
        assertTrue(detektExtension.buildUponDefaultConfig)
        assertFalse(detektExtension.allRules)
        assertTrue(detektExtension.autoCorrect)
        assertEquals(
            actual = detektExtension.config.asPath.toString(),
            expected = File(rootProjectDir, "gradle/detekt/config.yml").absolutePath,
        )
        assertEquals(
            actual = detektExtension.baseline?.absolutePath,
            expected = File(rootProjectDir, "gradle/detekt/baseline.yml").absolutePath,
        )
    }

    @Test
    fun `When apply is called it configures the DetektTask`() {
        // Given
        fakeProject.plugins.apply(BuildConfig.detektId)
        fakeProject.tasks.create("detekt2", DetektTask::class.java)
        val detektTasks: TaskCollection<DetektTask> = fakeProject.tasks.withType(DetektTask::class.java)

        // When
        Detekt.apply(fakeProject)

        // Then
        detektTasks.forEach { detektTask ->
            assertEquals(
                actual = detektTask.excludes.toList().sorted(),
                expected = listOf(
                    "**/.gradle/**",
                    "**/.idea/**",
                    "**/build/**",
                    "build/",
                    "**/buildSrc/**",
                    ".github/**",
                    "gradle/**",
                    "**/example/**",
                    "**/test/resources/**",
                    "**/build.gradle.kts",
                    "**/settings.gradle.kts",
                    "**/Dangerfile.df.kts",
                ).sorted(),
            )

            assertTrue(detektTask.reports.html.required.get(), message = "html must be enabled")
            assertTrue(detektTask.reports.xml.required.get(), message = "xml must be enabled")
            assertFalse(detektTask.reports.txt.required.get(), message = "txt must be disabled")
            assertFalse(detektTask.reports.md.required.get(), message = "markdown must be disabled")
        }
    }

    @Test
    fun `When apply is called it configures the BaselineTask`() {
        // Given
        fakeProject.plugins.apply(BuildConfig.detektId)
        fakeProject.tasks.create("detekt2", DetektCreateBaselineTask::class.java)
        val detektTasks: TaskCollection<DetektCreateBaselineTask> = fakeProject.tasks.withType(
            DetektCreateBaselineTask::class.java,
        )

        // When
        Detekt.apply(fakeProject)

        // Then
        detektTasks.forEach { baselineTask ->
            assertEquals(
                actual = baselineTask.excludes.toList().sorted(),
                expected = listOf(
                    "**/.gradle/**",
                    "**/.idea/**",
                    "**/build/**",
                    "**/gradle/wrapper/**",
                    ".github/**",
                    "assets/**",
                    "docs/**",
                    "gradle/**",
                    "**/example/**",
                    "**/*.adoc",
                    "**/*.md",
                    "**/gradlew",
                    "**/LICENSE",
                    "**/.java-version",
                    "**/gradlew.bat",
                    "**/*.png",
                    "**/*.properties",
                    "**/*.pro",
                    "**/*.sq",
                    "**/*.xml",
                    "**/*.yml",
                ).sorted(),
            )
        }
    }

    @Test
    fun `It fulfils the StaticAnalysisConventionContract`() {
        assertTrue {
            @Suppress("USELESS_IS_CHECK")
            Detekt is StaticAnalysisConventionContract
        }
    }
}
