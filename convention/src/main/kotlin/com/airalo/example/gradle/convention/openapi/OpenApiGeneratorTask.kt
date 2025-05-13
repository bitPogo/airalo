package com.airalo.example.gradle.convention.openapi

import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.OutputDirectory
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

abstract class OpenApiGeneratorTask : DefaultTask() {
    @get:OutputDirectory
    abstract val outputFile: RegularFileProperty

    protected fun GenerateTask.setDefaults() {
        releaseNote.convention("")
        modelNamePrefix.convention("")
        modelNameSuffix.convention("DTO")
        apiNameSuffix.convention("")
        generateModelTests.convention(false)
        generateApiTests.convention(false)
        configOptions.convention(mapOf())
        validateSpec.convention(false)
        logToStderr.convention(false)
        enablePostProcessFile.convention(false)
        skipValidateSpec.convention(true)
        generateAliasAsModel.convention(false)
        cleanupOutput.convention(false)
        dryRun.convention(false)
        generatorName.convention("kotlin")
        generateApiDocumentation.convention(false)
        generateModelDocumentation.convention(false)
        library.convention("multiplatform")
        configOptions.convention(
            mapOf(
                "omitGradlePluginVersions" to "true",
                "omitGradleWrapper" to "true",
                "dateLibrary" to "kotlinx-datetime",
            ),
        )
    }

    private fun File.removeTestFolders() {
        if (isDirectory && name.endsWith("Test")) {
            deleteRecursively()
        }
    }

    private fun File.removeDeadCode(deadSources: Collection<String>) {
        if (name in deadSources) {
            deleteRecursively()
        }
    }

    protected fun cleanUpCodeGeneration(deadSources: Collection<String>) {
        project.layout.buildDirectory.file(generatorTargetDirectory).get().asFile
            .listFiles()?.forEach { file ->
                file.walkTopDown().forEach { deepFile ->
                    deepFile.removeTestFolders()
                    deepFile.removeDeadCode(deadSources)
                }
            }
    }
}
