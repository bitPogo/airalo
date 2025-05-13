package com.airalo.example.gradle.convention.koin

import com.airalo.example.gradle.config.BuildConfig
import com.airalo.example.gradle.convention.GradleUtilApiContract
import com.airalo.example.gradle.convention.GradleUtilApiContract.PlatformContext
import com.airalo.example.gradle.convention.PlatformContextResolver
import com.airalo.example.gradle.convention.dependencyCatalog
import com.airalo.example.gradle.convention.hasAndroidLibrary
import com.airalo.example.gradle.convention.hasJvmLibrary
import javax.inject.Inject
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KoinKMP internal constructor(
    private val platformResolver: GradleUtilApiContract.PlatformContextResolver,
) : Plugin<Project> {
    @Inject
    constructor() : this(
        platformResolver = PlatformContextResolver,
    )

    private fun Set<PlatformContext>.determineMainKmpTarget(): List<String> {
        return if (hasJvmLibrary()) {
            listOf("kspJvm")
        } else {
            listOf("kspAndroidRelease", "kspAndroidDebug")
        }
    }

    private fun Project.setupKSP(context: Set<PlatformContext>) {
        listOf(
            listOf("kspCommonMainMetadata"),
            context.determineMainKmpTarget(),
        ).flatten().forEach { target ->
            dependencies.add(
                target,
                dependencyCatalog.findLibrary("convention-koin-annotations-compiler").get(),
            )
        }
    }

    private fun Project.includeDependencies(context: Set<PlatformContext>) {
        val bundle = dependencyCatalog.findBundle("convention-koin").get()
        val bom = project.dependencies.platform(dependencyCatalog.findLibrary("koin-bom").get())

        val sourceSets = extensions.getByType(KotlinMultiplatformExtension::class.java).sourceSets
        sourceSets.getByName("commonMain").dependencies {
            implementation(bundle)
            implementation(bom)
        }

        if (context.hasJvmLibrary()) {
            sourceSets.getByName("jvmMain").kotlin.srcDir("build/generated/ksp/jvm/kotlin")
        } else {
            sourceSets.getByName("androidMain").kotlin.srcDir("build/generated/ksp/android/androidDebug/kotlin")
        }
    }

    private fun Project.wireTasksDependencies(context: Set<PlatformContext>) {
        if (context.hasAndroidLibrary()) {
            afterEvaluate {
                this@wireTasksDependencies.tasks.named("kspReleaseKotlinAndroid").configure {
                    dependsOn("kspDebugKotlinAndroid")
                }
            }
        }
    }

    override fun apply(target: Project) {
        target.plugins.apply(BuildConfig.kspId)
        val context = platformResolver.getType(target)
        target.setupKSP(context)
        target.includeDependencies(context)
        target.wireTasksDependencies(context)
    }
}
