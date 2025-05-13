package com.airalo.example.gradle.convention.openapi.client

import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class OpenApiClientTaskSpec {
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

    private fun compareFileTree(targetPath: String) {
        var expectedDirectory = File(OpenApiClientTaskSpec::class.java.getResource(targetPath)!!.path)
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
    fun `When execute is called it generates the OpenApi Client for a single Contract`() {
        // Given

        val contract = project.layout.projectDirectory.file("contract.yml").asFile
        val contractContent = OpenApiClientTaskSpec::class.java.getResource("/openapi/client/example.yml")!!.readText()
        contract.createNewFile()
        contract.writeText(contractContent)

        // When
        val sut: OpenApiClientTask = project.tasks.create("sut", OpenApiClientTask::class.java)
        sut.packageName.set("module")
        sut.apiContracts.add(contract)
        sut.execute()

        // Then
        compareFileTree("/openapi/client")
    }

    @Test
    fun `When execute is called it generates the OpenApi Client for a multiple Contracts`() {
        // Given
        val contractContent1 = OpenApiClientTaskSpec::class.java.getResource("/openapi/multiclient/example1.yml")!!.readText()
        val contractContent2 = OpenApiClientTaskSpec::class.java.getResource("/openapi/multiclient/example2.yml")!!.readText()

        val contract1 = project.layout.projectDirectory.file("contract1.yml").asFile
        val contract2 = project.layout.projectDirectory.file("contract2.yml").asFile

        contract1.createNewFile()
        contract1.writeText(contractContent1)

        contract2.createNewFile()
        contract2.writeText(contractContent2)

        // When
        val sut: OpenApiClientTask = project.tasks.create("sut", OpenApiClientTask::class.java)
        sut.packageName.set("module")
        sut.apiContracts.add(contract1)
        sut.apiContracts.add(contract2)
        sut.execute()

        // Then
        compareFileTree("/openapi/multiclient")
    }

    @Test
    fun `It fulfils Task`() {
        assertTrue { project.tasks.create("sut", OpenApiClientTask::class.java) is Task }
    }
}
