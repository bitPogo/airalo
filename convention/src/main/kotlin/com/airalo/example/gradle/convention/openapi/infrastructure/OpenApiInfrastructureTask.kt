package com.airalo.example.gradle.convention.openapi.infrastructure

import com.airalo.example.gradle.convention.openapi.OpenApiGeneratorTask
import com.airalo.example.gradle.convention.openapi.generatorTargetDirectory
import java.io.File
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.gradle.internal.ensureParentDirsCreated
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

abstract class OpenApiInfrastructureTask : OpenApiGeneratorTask() {
    private val generatorOutputPath: Provider<RegularFile> = project.layout.buildDirectory.file(generatorTargetDirectory)

    init {
        @Suppress("LeakingThis")
        outputFile.convention(generatorOutputPath)
    }

    private fun generateDummyContract() {
        val contract = project.layout.buildDirectory.file("$generatorTargetDirectory/emptyContract.yml").get().asFile
        contract.ensureParentDirsCreated()
        contract.createNewFile()
        contract.writeText(emptyContractApi)
    }

    private fun GenerateTask.setupOpenapiGenerator() {
        val output = generatorOutputPath.get().asFile
        setDefaults()
        inputSpec.convention(File(output, "emptyContract.yml").absolutePath)
        outputDir.convention(output.absolutePath)
    }

    private fun generateOpenApiInfrastructure() {
        val openApiCodeGenerator = project.tasks.create(
            "generateOpenapiInfrastructure",
            GenerateTask::class.java,
        )

        openApiCodeGenerator.setupOpenapiGenerator()
        openApiCodeGenerator.doWork()
    }

    @TaskAction
    fun execute() {
        generateDummyContract()
        generateOpenApiInfrastructure()
        cleanUpCodeGeneration(deadSources)
    }

    private companion object {
        val deadSources = listOf(
            "PartConfig.kt",
            "OctetByteArray.kt",
            "Bytes.kt",
            "Base64ByteArray.kt",
            "ApiAbstractions.kt",
            "apis",
            ".openapi-generator-ignore",
            ".openapi-generator",
            "build.gradle.kts",
            "README.md",
            "settings.gradle.kts",
        )

        const val emptyContractApi = """openapi: 3.0.1
servers:
  - url:  https://www.airalo.com/api/v2
info:
  description: Airalo Example Project
  version: v2
  title: Airalo Example Project
paths:
  /nowhere/:
    get:
      tags:
        - Nothing
      description: "Nothing important"
      operationId: what every
      parameters: [ ]
      responses:
        '200':
          description: ""
          content:
            application/json:
              schema:
                type: array
                items:
                  type: integer
                  format: int64
    """
    }
}
