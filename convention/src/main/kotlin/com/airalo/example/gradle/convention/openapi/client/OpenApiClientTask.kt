package com.airalo.example.gradle.convention.openapi.client

import com.airalo.example.gradle.convention.openapi.OpenApiGeneratorTask
import com.airalo.example.gradle.convention.openapi.generatorTargetDirectory
import java.io.File
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

@CacheableTask
abstract class OpenApiClientTask : OpenApiGeneratorTask() {
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.NONE)
    abstract val apiContracts: ListProperty<File>

    @get:Input
    abstract val packageName: Property<String>

    @get:OutputDirectory
    abstract override val outputFile: RegularFileProperty

    private val generatorOutputPath: Provider<RegularFile> = project.layout.buildDirectory.file(generatorTargetDirectory)

    init {
        @Suppress("LeakingThis")
        outputFile.convention(generatorOutputPath)
    }

    private fun GenerateTask.setupOpenapiGenerator(apiContract: String) {
        setDefaults()
        val packageName = this@OpenApiClientTask.packageName.get()
        val output = generatorOutputPath.get().asFile

        inputSpec.set(apiContract)
        outputDir.convention(output.absolutePath)
        apiPackage.set("$packageName.api.client")
        modelPackage.set("$packageName.api.model")
        additionalProperties.set(
            mapOf("nonPublicApi" to "true"),
        )
    }

    private fun generateOpenApiClient() {
        apiContracts.get().forEachIndexed { idx, contract ->
            val openApiCodeGenerator = project.tasks.create(
                "generateOpenApiClient$idx",
                GenerateTask::class.java,
            )

            openApiCodeGenerator.setupOpenapiGenerator(contract.absolutePath)
            openApiCodeGenerator.doWork()
        }
    }

    @TaskAction
    fun execute() {
        generateOpenApiClient()
        cleanUpCodeGeneration(deadSources)
    }

    private companion object {
        val deadSources = listOf(
            "org",
            ".openapi-generator-ignore",
            ".openapi-generator",
            "build.gradle.kts",
            "README.md",
            "settings.gradle.kts",
        )
    }
}
