package com.airalo.example.gradle.convention.android

import org.gradle.api.Project

// Taken from https://github.com/bitPogo/gradle-plugins/tree/main/antibytes-android-configuration
interface ConfigurationContract {
    fun interface ParameterlessConfigurator {
        fun configure(project: Project)
    }
}
