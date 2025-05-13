package com.airalo.example.gradle.convention.openapi.infrastructure

import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class OpenApiInfrastructureTaskSpec {
    @TempDir
    private lateinit var projectDir: File
    private lateinit var project: Project

    @BeforeEach
    fun setup() {
        project = ProjectBuilder
            .builder()
            .withProjectDir(projectDir)
            .build()
    }

    private fun String.normalizeSource(): String {
        return this
            .replace(Regex("^/\\*\n", RegexOption.MULTILINE), "")
            .replace(Regex("^ +\\*.*", RegexOption.MULTILINE), "")
            .replace(Regex("^\\*/", RegexOption.MULTILINE), "")
            .trimStart()
            .replace(Regex("[\t ]+"), "")
            .replace(Regex("[\n]+"), "\n")
            .trim()
    }

    @Test
    fun `When execute is called it generates the OpenApi Infrastructure only`() {
        // Given
        var expectedDirectory = File(OpenApiInfrastructureTaskSpec::class.java.getResource("/openapi/infrastructure")!!.path)

        // When
        project.tasks.create("sut", OpenApiInfrastructureTask::class.java).execute()

        // Then
        val actualFiles = mutableListOf<String>()
        File(projectDir, "build/generated/openapi").walkTopDown().onEnter { directory ->
            expectedDirectory = File(expectedDirectory, directory.name)
            true
        }.onLeave {
            expectedDirectory = expectedDirectory.parentFile
        }.forEach { actual ->
            if (actual.isFile) {
                val expected = File(expectedDirectory, actual.name)

                assertTrue(expected.exists(), "${actual.absolutePath} should not have been generated.")

                assertEquals(
                    actual = actual.readText().normalizeSource(),
                    expected = expected.readText().normalizeSource(),
                    message = "${actual.absolutePath} contains unexpected content.",
                )

                actualFiles.add(actual.name)
            }
        }

        File(expectedDirectory, "openapi").walkTopDown().forEach { file ->
            if (file.isFile) {
                assertTrue(actualFiles.contains(file.name), "Expected ${file.name} but it was not generated.")
            }
        }
    }

    @Test
    fun `It fulfils Task`() {
        assertTrue { project.tasks.create("sut", OpenApiInfrastructureTask::class.java) is Task }
    }
}
