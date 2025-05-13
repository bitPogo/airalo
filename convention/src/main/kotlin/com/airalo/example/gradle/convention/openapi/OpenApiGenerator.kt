package com.airalo.example.gradle.convention.openapi

import com.airalo.example.gradle.convention.dependencyCatalog
import java.io.File
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompileCommon
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile

internal abstract class OpenApiGenerator : Plugin<Project> {
    protected fun Project.wireCompilation(dependencyTask: Task) {
        listOf(
            KotlinCompileCommon::class.java,
            KotlinCompile::class.java,
            KotlinNativeCompile::class.java,
        ).forEach { taskType ->
            tasks.withType(taskType) {
                dependsOn(dependencyTask)
                mustRunAfter(dependencyTask)
            }
        }
    }

    protected fun Project.setupDependencies(internalDependency: String? = null) {
        extensions.getByType(KotlinMultiplatformExtension::class.java).apply {
            sourceSets.getByName("commonMain").apply {
                kotlin.srcDir(File(projectDir, "build/generated/openapi/src/commonMain/kotlin").absolutePath)

                dependencies {
                    implementation(dependencyCatalog.findBundle("convention-openapi").get())
                    internalDependency?.apply {
                        implementation(project(internalDependency))
                    }
                }
            }
        }
    }
}
