package com.airalo.example.gradle.convention.openapi.infrastructure

import com.airalo.example.gradle.config.BuildConfig
import com.airalo.example.gradle.convention.IntegrationSetupTest
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class OpenApiInfrastructureIntegration : IntegrationSetupTest() {
    @TempDir
    private lateinit var buildFile: File

    @BeforeEach
    override fun setUp() {
        super.setUp()

        buildFile = File(projectDir, "build.gradle.kts").also { it.createNewFile() }

        val buildFileContent = OpenApiInfrastructureIntegration::class.java.getResource(
            "/integration/openapi/build.gradle.txt",
        )?.readText()

        buildFile.writeText(buildFileContent!!)
    }

    @Test
    fun `When a compilation is triggered it generated the OpenapiInfrastructure code`() {
        // When
        val result = GradleRunner.create()
            .withProjectDir(projectDir)
            .withPluginClasspath()
            .withEnvironment(
                mutableMapOf("ANDROID_HOME" to BuildConfig.androidSdk),
            )
            .withArguments("compileDebugKotlinAndroid")
            .build()

        // Then
        assertEquals(
            actual = result.task(":compileDebugKotlinAndroid")?.outcome,
            expected = TaskOutcome.SUCCESS,
        )

        assertTrue {
            File(
                projectDir,
                "build/generated/openapi/src/commonMain/kotlin/org/openapitools/client/infrastructure/ApiClient.kt",
            ).exists()
        }
    }
}
