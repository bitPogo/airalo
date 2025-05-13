package com.airalo.example.gradle.convention.android

import com.airalo.example.gradle.config.BuildConfig
import com.airalo.example.gradle.convention.DependencyRepository
import com.airalo.example.gradle.convention.DependencyRepositoryProvider
import com.airalo.example.gradle.convention.android.ConfigurationContract.ParameterlessConfigurator
import com.airalo.example.gradle.convention.android.android.AndroidApplicationConfigurator
import com.airalo.example.gradle.convention.android.android.CompileTaskConfigurator
import com.airalo.example.gradle.convention.applyIfNotExists
import com.airalo.example.gradle.convention.isKmp
import com.airalo.example.gradle.convention.junit.JunitConfiguration
import com.airalo.example.gradle.convention.koin.Koin
import com.airalo.example.gradle.convention.quality.metric.QualityMetric
import com.airalo.example.gradle.convention.toolchain.ToolChainAndroidConfigurator
import javax.inject.Inject
import org.gradle.api.Plugin
import org.gradle.api.Project

// Taken from https://github.com/bitPogo/gradle-plugins/tree/main/antibytes-android-configuration
class ApplicationConfiguration(
    private val applicationConfigurator: ParameterlessConfigurator,
    private val compileTaskConfiguration: ParameterlessConfigurator,
    private val toolChainConfiguration: ParameterlessConfigurator,
    private val qualityMetric: Plugin<Project>,
    private val repositoryConfiguration: DependencyRepositoryProvider,
    private val composeConfiguration: Plugin<Project>,
    private val junitConfiguration: JunitConfiguration,
    private val koin: Plugin<Project>,
) : Plugin<Project> {
    @Inject
    constructor() : this(
        applicationConfigurator = AndroidApplicationConfigurator(),
        compileTaskConfiguration = CompileTaskConfigurator,
        toolChainConfiguration = ToolChainAndroidConfigurator,
        qualityMetric = QualityMetric(),
        repositoryConfiguration = DependencyRepository,
        composeConfiguration = ComposeConfiguration(),
        junitConfiguration = JunitConfiguration,
        koin = Koin(),
    )

    private fun Project.determineApplicablePlugins(): Array<String> =
        if (isKmp()) {
            arrayOf(BuildConfig.androidApplicationId)
        } else {
            arrayOf(
                BuildConfig.androidApplicationId,
                BuildConfig.kotlinAndroidId,
            )
        }

    override fun apply(target: Project) {
        target.applyIfNotExists(
            *target.determineApplicablePlugins(),
        )
        qualityMetric.apply(target)
        applicationConfigurator.configure(target)
        compileTaskConfiguration.configure(target)
        toolChainConfiguration.configure(target)
        repositoryConfiguration.configure(target)
        composeConfiguration.apply(target)
        junitConfiguration.configure(target)
        koin.apply(target)
    }
}
