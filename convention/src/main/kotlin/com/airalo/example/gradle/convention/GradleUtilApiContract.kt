/*
 * Copyright (c) 2024 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package com.airalo.example.gradle.convention

import org.gradle.api.Project

// Taken from https://github.com/bitPogo/gradle-plugins/tree/main/antibytes-gradle-utils
internal interface GradleUtilApiContract {
    enum class PlatformContext(val prefix: String) {
        ANDROID_APPLICATION("android"),
        ANDROID_LIBRARY("android"),
        ANDROID_LIBRARY_KMP("android"),
        JVM_LIBRARY("jvm"),
        JVM_LIBRARY_KMP("jvm"),
        UNKNOWN("unknown"),
    }

    fun interface PlatformContextResolver {
        fun getType(project: Project): Set<PlatformContext>
    }
}
