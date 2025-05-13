package com.airalo.example.gradle.convention.android

import com.airalo.example.gradle.config.BuildConfig
import org.gradle.api.JavaVersion

// Taken from https://github.com/bitPogo/gradle-plugins/tree/main/antibytes-android-configuration
interface AndroidConfigurationApiContract {
    companion object {
        const val ANDROID_PREFIX = "thermondo"
        const val ANDROID_PREFIX_SEPARATOR = "_"
        val TARGET_SDK = BuildConfig.androidTargetSdk.toInt()
        val MIN_SDK = BuildConfig.androidMinSdk.toInt()
        val COMPATIBILITY_TARGETS: JavaVersion = JavaVersion.toVersion(BuildConfig.androidCompabilityVersion)
        const val TEST_RUNNER = "androidx.test.runner.AndroidJUnitRunner"
        val TEST_RUNNER_ARGUMENTS = mapOf("clearPackageData" to "true")
    }
}
