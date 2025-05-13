package com.airalo.example.gradle.convention.openapi.client

import com.airalo.example.gradle.convention.kmp.KmpAndroidConfiguration
import com.airalo.example.gradle.convention.openapi.OpenApiGenerator
import java.io.File
import javax.inject.Inject
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class OpenApiClientGenerator internal constructor(
    private val kmpAndroidConvention: Plugin<Project>,
) : Plugin<Project>, OpenApiGenerator() {
    @Inject
    constructor() : this(kmpAndroidConvention = KmpAndroidConfiguration())

    private fun Project.findInfrastructureProject(): Project? {
        return rootProject.subprojects.firstOrNull { project ->
            project.name.endsWith("openapi-infrastructure")
        }
    }

    private fun Project.setupOpenApiTasks(extension: OpenApiClientExtension) {
        tasks.create("openApiGenerate", OpenApiClientTask::class.java).apply {
            group = "OpenAPI Tools"
            description = "Generate infrastructure code via Open API Tools Generator for Open API 2.0 or 3.x specification documents."

            packageName.set(extension.packageName)
            apiContracts.set(
                extension.apiContracts.get().map { path ->
                    File(path)
                },
            )

            wireCompilation(this)
        }
    }

    override fun apply(target: Project) {
        kmpAndroidConvention.apply(target)
        val extension = target.extensions.create("openApiContract", OpenApiClientExtension::class.java)

        target.setupDependencies(target.findInfrastructureProject()?.path)
        target.afterEvaluate {
            target.setupOpenApiTasks(extension)
        }
    }
}
