package com.airalo.example.gradle.convention.koin

import com.airalo.example.gradle.config.BuildConfig
import com.airalo.example.gradle.convention.dependencyCatalog
import com.airalo.example.gradle.convention.isJvmLibrary
import com.airalo.example.gradle.convention.isKmp
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

class Koin : Plugin<Project> {
    private fun Project.setupKSP() {
        dependencies.add(
            "ksp",
            dependencyCatalog.findLibrary("convention-koin-annotations-compiler").get(),
        )
    }

    private fun Project.includeDependencies() {
        val bundle = dependencyCatalog.findBundle("convention-koin").get()

        dependencies.add("implementation", bundle)
        dependencies.add(
            "implementation",
            dependencies.platform(dependencyCatalog.findLibrary("koin-bom").get()),
        )
    }

    private fun Project.addSrc() {
        if (isJvmLibrary()) {
            extensions.getByType(KotlinJvmProjectExtension::class.java)
                .sourceSets
                .getByName("main")
                .kotlin
                .srcDir("build/generated/ksp/main/kotlin")
        }
    }

    override fun apply(target: Project) {
        if (!target.isKmp()) {
            target.plugins.apply(BuildConfig.kspId)
            target.setupKSP()
            target.includeDependencies()
            target.addSrc()
        }
    }
}
