package com.airalo.example.gradle.convention

import org.gradle.api.Project

// Taken from https://github.com/bitPogo/gradle-plugins/tree/main/antibytes-gradle-utils
internal fun Project.applyIfNotExists(vararg pluginNames: String) {
    pluginNames.forEach { pluginName ->
        if (!plugins.hasPlugin(pluginName)) {
            plugins.apply(pluginName)
        }
    }
}

internal fun Project.isKmp(): Boolean = plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")

internal fun Project.isAndroidLibrary(): Boolean = plugins.hasPlugin("com.android.library")

internal fun Project.isJvmLibrary(): Boolean = plugins.hasPlugin("java-library")

internal fun Project.isAndroidApplication(): Boolean = plugins.hasPlugin("com.android.application")
