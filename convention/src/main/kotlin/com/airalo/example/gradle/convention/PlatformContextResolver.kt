package com.airalo.example.gradle.convention

import com.airalo.example.gradle.convention.GradleUtilApiContract.PlatformContext
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

// Taken from https://github.com/bitPogo/gradle-plugins/tree/main/antibytes-gradle-utils
internal object PlatformContextResolver : GradleUtilApiContract.PlatformContextResolver {
    private fun Project.determineContext(): Set<PlatformContext> {
        val context = when {
            isAndroidApplication() -> PlatformContext.ANDROID_APPLICATION
            isAndroidLibrary() -> PlatformContext.ANDROID_LIBRARY
            isJvmLibrary() -> PlatformContext.JVM_LIBRARY
            else -> PlatformContext.UNKNOWN
        }

        return setOf(context)
    }

    private fun Project.isJvmKmp(): Boolean {
        val kotlin = extensions.getByName("kotlin") as KotlinMultiplatformExtension
        return kotlin.targets.asMap.containsKey(PlatformContext.JVM_LIBRARY_KMP.prefix)
    }

    private fun Project.determineKmpContext(): Set<PlatformContext> {
        val contexts: MutableSet<PlatformContext> = mutableSetOf()

        if (isJvmKmp()) {
            contexts.add(PlatformContext.JVM_LIBRARY_KMP)
        }

        if (isAndroidLibrary()) {
            contexts.add(PlatformContext.ANDROID_LIBRARY_KMP)
        }

        return contexts
    }

    override fun getType(project: Project): Set<PlatformContext> {
        return if (project.isKmp()) {
            project.determineKmpContext()
        } else {
            project.determineContext()
        }
    }
}
