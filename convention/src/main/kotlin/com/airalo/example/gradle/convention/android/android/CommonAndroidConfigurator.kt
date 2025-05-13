package com.airalo.example.gradle.convention.android.android

import com.airalo.example.gradle.convention.android.AndroidConfigurationApiContract.Companion.COMPATIBILITY_TARGETS
import com.airalo.example.gradle.convention.android.AndroidConfigurationApiContract.Companion.MIN_SDK
import com.airalo.example.gradle.convention.android.AndroidConfigurationApiContract.Companion.TARGET_SDK
import com.airalo.example.gradle.convention.android.AndroidConfigurationApiContract.Companion.TEST_RUNNER
import com.airalo.example.gradle.convention.android.AndroidConfigurationApiContract.Companion.TEST_RUNNER_ARGUMENTS
import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.ProguardFiles.getDefaultProguardFile
import org.gradle.api.Project

internal object CommonAndroidConfigurator {
    fun configure(
        project: Project,
        extension: CommonExtension<*, *, *, *, *, *>,
    ) {
        extension.compileSdk = TARGET_SDK

        extension.defaultConfig {
            minSdk = MIN_SDK

            testInstrumentationRunner = TEST_RUNNER
            testInstrumentationRunnerArguments.putAll(TEST_RUNNER_ARGUMENTS)
        }

        extension.compileOptions {
            targetCompatibility = COMPATIBILITY_TARGETS
            sourceCompatibility = COMPATIBILITY_TARGETS
        }

        @Suppress("UnstableApiUsage")
        extension.testOptions {
            unitTests {
                isReturnDefaultValues = true
                isIncludeAndroidResources = true
            }
        }

        extension.buildTypes {
            named("release").configure {
                isMinifyEnabled = false
                isShrinkResources = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt", project.layout.buildDirectory),
                    project.file("proguard-rules.pro"),
                )
            }

            named("debug").configure {
                isMinifyEnabled = false
                isShrinkResources = false
                matchingFallbacks.add("release")
            }
        }
    }
}
