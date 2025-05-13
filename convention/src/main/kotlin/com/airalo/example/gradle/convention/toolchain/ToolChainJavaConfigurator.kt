package com.airalo.example.gradle.convention.toolchain

import com.airalo.example.gradle.config.BuildConfig
import com.airalo.example.gradle.convention.java.ParameterlessConfiguratorContract
import org.gradle.api.Project
import org.gradle.jvm.toolchain.JavaLanguageVersion

// Taken from https://github.com/bitPogo/gradle-plugins/tree/main/antibytes-android-configuration
internal object ToolChainJavaConfigurator : ParameterlessConfiguratorContract, ToolChainSekelton() {
    override fun configure(project: Project) {
        configure(project, "Jvm", JavaLanguageVersion.of(BuildConfig.androidToolchainVersion))
    }
}
