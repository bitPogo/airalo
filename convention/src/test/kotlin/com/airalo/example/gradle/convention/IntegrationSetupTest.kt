package com.airalo.example.gradle.convention

import java.io.File
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.io.TempDir

abstract class IntegrationSetupTest {
    @TempDir
    protected lateinit var projectDir: File

    @Suppress("MemberVisibilityCanBePrivate")
    protected lateinit var settingsFile: File

    @BeforeEach
    open fun setUp() {
        settingsFile = File(projectDir, "settings.gradle.kts").also { it.createNewFile() }

        val settingsFileContent = IntegrationSetupTest::class.java.getResource(
            "/integration/settings.gradle.txt",
        )?.readText()

        settingsFile.writeText(settingsFileContent!!)

        File(projectDir, "gradle").mkdir()

        File(projectDir, "gradle/dependency.versions.toml").apply {
            val content = IntegrationSetupTest::class.java.getResource(
                "/integration/dependency.versions.toml",
            )?.readText()

            createNewFile()
            writeText(content!!)
        }

        File(projectDir, "gradle/test.dependency.versions.toml").apply {
            val content = IntegrationSetupTest::class.java.getResource(
                "/integration/test.dependency.versions.toml",
            )?.readText()

            createNewFile()
            writeText(content!!)
        }
    }
}
