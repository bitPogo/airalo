package com.airalo.example.gradle.convention.openapi.infrastructure

import com.airalo.example.gradle.convention.kmp.KmpAndroidConfiguration
import com.airalo.example.gradle.convention.openapi.OpenApiGenerator
import javax.inject.Inject
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class OpenApiInfrastructureGenerator internal constructor(
    private val kmpAndroidConvention: Plugin<Project>,
) : Plugin<Project>, OpenApiGenerator() {
    @Inject
    constructor() : this(kmpAndroidConvention = KmpAndroidConfiguration())

    private fun Project.setupOpenApiTasks() {
        tasks.create("openApiInfrastructureGenerate", OpenApiInfrastructureTask::class.java).apply {
            group = "OpenAPI Tools"
            description = "Generate infrastructure code via Open API Tools Generator for Open API 2.0 or 3.x specification documents."
            wireCompilation(this)
        }
    }

    override fun apply(target: Project) {
        kmpAndroidConvention.apply(target)
        target.setupOpenApiTasks()
        target.setupDependencies()
    }
}
