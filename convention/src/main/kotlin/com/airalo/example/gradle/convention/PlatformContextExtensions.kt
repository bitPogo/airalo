package com.airalo.example.gradle.convention

import com.airalo.example.gradle.convention.GradleUtilApiContract.PlatformContext

// Taken from https://github.com/bitPogo/gradle-plugins/tree/main/antibytes-gradle-utils
internal fun PlatformContext.isAndroidLibrary(): Boolean {
    return this == PlatformContext.ANDROID_LIBRARY || this == PlatformContext.ANDROID_LIBRARY_KMP
}

internal fun PlatformContext.isAndroidApplication(): Boolean = this == PlatformContext.ANDROID_APPLICATION

internal fun PlatformContext.isJvmLibrary(): Boolean {
    return this == PlatformContext.JVM_LIBRARY || this == PlatformContext.JVM_LIBRARY_KMP
}

internal fun PlatformContext.isKmp(): Boolean {
    return when (this) {
        PlatformContext.ANDROID_LIBRARY_KMP -> true
        PlatformContext.JVM_LIBRARY_KMP -> true
        else -> false
    }
}

internal fun Set<PlatformContext>.hasAndroidLibrary(): Boolean = this.any { context -> context.isAndroidLibrary() }

internal fun Set<PlatformContext>.hasJvmLibrary(): Boolean = this.any { context -> context.isJvmLibrary() }

internal fun Set<PlatformContext>.hasAndroidApplication(): Boolean = this.any { context -> context.isAndroidApplication() }
