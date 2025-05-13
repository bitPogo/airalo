package com.airalo.example.gradle.convention.android.android

import com.airalo.example.gradle.config.BuildConfig
import com.airalo.example.gradle.convention.android.ConfigurationContract
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Taken from https://github.com/bitPogo/gradle-plugins/tree/main/antibytes-android-configuration
internal object CompileTaskConfigurator : ConfigurationContract.ParameterlessConfigurator {
    override fun configure(project: Project) {
        project.tasks.withType<KotlinCompile> {
            if (name.endsWith("Kotlin") || name.endsWith("KotlinAndroid")) {
                compilerOptions {
                    jvmTarget.set(JvmTarget.fromTarget(JavaVersion.toVersion(BuildConfig.androidCompabilityVersion).toString()))
                }
            }
        }
    }
}
