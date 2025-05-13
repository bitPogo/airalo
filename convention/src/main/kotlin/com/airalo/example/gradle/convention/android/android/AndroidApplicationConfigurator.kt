package com.airalo.example.gradle.convention.android.android

import com.airalo.example.gradle.convention.android.AndroidConfigurationApiContract.Companion.TARGET_SDK
import com.airalo.example.gradle.convention.android.ConfigurationContract
import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project

// Taken from https://github.com/bitPogo/gradle-plugins/tree/main/antibytes-android-configuration
internal class AndroidApplicationConfigurator(
    private val commonConfiguration: CommonAndroidConfigurator = CommonAndroidConfigurator,
) : ConfigurationContract.ParameterlessConfigurator {
    override fun configure(project: Project) {
        project.extensions.configure(ApplicationExtension::class.java) {
            commonConfiguration.configure(project = project, extension = this)
            defaultConfig {
                targetSdk = TARGET_SDK
                vectorDrawables {
                    useSupportLibrary = true
                }
            }

            buildFeatures {
                viewBinding = true
            }

            buildTypes {
                named("release").configure {
                    isMinifyEnabled = true
                    isShrinkResources = true
                }

                named("debug").configure {
                    isMinifyEnabled = false
                    isShrinkResources = false
                }
            }
        }
    }
}
