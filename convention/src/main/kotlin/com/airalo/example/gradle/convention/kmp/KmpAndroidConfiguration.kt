package com.airalo.example.gradle.convention.kmp

import com.airalo.example.gradle.config.BuildConfig
import com.airalo.example.gradle.convention.android.LibraryConfiguration
import com.airalo.example.gradle.convention.junit.JunitKmpConfiguration
import com.airalo.example.gradle.convention.koin.KoinKMP
import javax.inject.Inject
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KmpAndroidConfiguration(
    private val androidLibraryConvention: Plugin<Project>,
    private val junitConfiguration: JunitKmpConfiguration,
    private val koin: Plugin<Project>,
) : Plugin<Project> {
    @Inject
    constructor() : this(
        androidLibraryConvention = LibraryConfiguration(),
        junitConfiguration = JunitKmpConfiguration,
        koin = KoinKMP(),
    )

    private fun KotlinMultiplatformExtension.configure() {
        applyDefaultHierarchyTemplate()
        androidTarget()
    }

    override fun apply(target: Project) {
        target.plugins.apply(BuildConfig.kmpId)
        androidLibraryConvention.apply(target)
        target.extensions.getByType(KotlinMultiplatformExtension::class.java).configure()
        junitConfiguration.configure(target)
        koin.apply(target)
    }
}
