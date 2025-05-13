package com.airalo.example.gradle.convention.kmp

import com.airalo.example.gradle.config.BuildConfig
import com.airalo.example.gradle.convention.java.JavaLibraryConfiguration
import com.airalo.example.gradle.convention.junit.JunitKmpConfiguration
import com.airalo.example.gradle.convention.koin.KoinKMP
import javax.inject.Inject
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KmpJvmConfiguration(
    private val junitConfiguration: JunitKmpConfiguration,
    private val jvmLibraryConvention: Plugin<Project>,
    private val koin: Plugin<Project>,
) : Plugin<Project> {
    @Inject
    constructor() : this(
        jvmLibraryConvention = JavaLibraryConfiguration(),
        junitConfiguration = JunitKmpConfiguration,
        koin = KoinKMP(),
    )

    private fun KotlinMultiplatformExtension.configure() {
        jvm()
    }

    override fun apply(target: Project) {
        target.plugins.apply(BuildConfig.kmpId)
        target.extensions.getByType(KotlinMultiplatformExtension::class.java).configure()
        jvmLibraryConvention.apply(target)
        junitConfiguration.configure(target)
        koin.apply(target)
    }
}
