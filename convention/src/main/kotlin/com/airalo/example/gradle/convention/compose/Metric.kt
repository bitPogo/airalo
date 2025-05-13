package com.airalo.example.gradle.convention.compose

import java.io.File
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun Project.setupMetric() {
    val metricDestination = File(layout.buildDirectory.asFile.get(), "compose-metrics")
    tasks.withType(KotlinCompile::class.java) {
        compilerOptions {
            freeCompilerArgs.addAll(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${metricDestination.absolutePath}",
            )
        }
    }
}

fun Project.setupReport() {
    val metricDestination = File(layout.buildDirectory.asFile.get(), "reports/compose")
    tasks.withType(KotlinCompile::class.java) {
        compilerOptions {
            freeCompilerArgs.addAll(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${metricDestination.absolutePath}",
            )
        }
    }
}
