package com.airalo.example.gradle.convention.android.android

import com.airalo.example.gradle.convention.android.AndroidConfigurationApiContract.Companion.ANDROID_PREFIX
import com.airalo.example.gradle.convention.android.AndroidConfigurationApiContract.Companion.ANDROID_PREFIX_SEPARATOR
import com.airalo.example.gradle.convention.android.ConfigurationContract
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project

// Taken from https://github.com/bitPogo/gradle-plugins/tree/main/antibytes-android-configuration
internal class AndroidLibraryConfigurator(
    private val commonConfiguration: CommonAndroidConfigurator = CommonAndroidConfigurator,
) : ConfigurationContract.ParameterlessConfigurator {
    private fun Project.determinePrefix(): String {
        val projectInfix = name.replace("-", ANDROID_PREFIX_SEPARATOR)

        return "${ANDROID_PREFIX}${ANDROID_PREFIX_SEPARATOR}${projectInfix}$ANDROID_PREFIX_SEPARATOR"
    }

    private fun LibraryExtension.setupDefaultProguard(project: Project) {
        defaultConfig {
            consumerProguardFiles(project.file("consumer-rules.pro"))
        }
    }

    override fun configure(project: Project) {
        project.extensions.configure(LibraryExtension::class.java) {
            commonConfiguration.configure(project, this)
            resourcePrefix = project.determinePrefix()
            setupDefaultProguard(project)
        }
    }
}
