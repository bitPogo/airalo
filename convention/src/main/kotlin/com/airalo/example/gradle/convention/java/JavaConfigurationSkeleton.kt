package com.airalo.example.gradle.convention.java

import com.airalo.example.gradle.config.BuildConfig
import org.gradle.api.JavaVersion
import org.gradle.api.plugins.JavaPluginExtension

abstract class JavaConfigurationSkeleton {
    private val version = JavaVersion.toVersion(BuildConfig.javaToolchainVersion)

    protected fun JavaPluginExtension.setupCompatibility() {
        sourceCompatibility = version
        targetCompatibility = version
    }
}
