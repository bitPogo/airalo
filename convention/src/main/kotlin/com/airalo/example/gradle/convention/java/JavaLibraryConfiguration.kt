package com.airalo.example.gradle.convention.java

import com.airalo.example.gradle.config.BuildConfig
import com.airalo.example.gradle.convention.DependencyRepository
import com.airalo.example.gradle.convention.DependencyRepositoryProvider
import com.airalo.example.gradle.convention.isKmp
import com.airalo.example.gradle.convention.junit.JunitConfiguration
import com.airalo.example.gradle.convention.koin.Koin
import com.airalo.example.gradle.convention.quality.metric.QualityMetric
import com.airalo.example.gradle.convention.toolchain.ToolChainJavaConfigurator
import javax.inject.Inject
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension

class JavaLibraryConfiguration(
    private val qualityMetric: Plugin<Project>,
    private val toolChainConfigurator: ParameterlessConfiguratorContract,
    private val repositoryConfiguration: DependencyRepositoryProvider,
    private val junitConfiguration: JunitConfiguration,
    private val koin: Plugin<Project>,
) : Plugin<Project>, JavaConfigurationSkeleton() {
    @Inject
    constructor() : this(
        toolChainConfigurator = ToolChainJavaConfigurator,
        qualityMetric = QualityMetric(),
        repositoryConfiguration = DependencyRepository,
        junitConfiguration = JunitConfiguration,
        koin = Koin(),
    )

    private fun Project.applyPlugins() {
        if (!isKmp()) {
            plugins.apply(BuildConfig.javaLibraryId)
            plugins.apply(BuildConfig.kotlinJvmId)
        }
    }

    private fun JavaPluginExtension.setupJavaExtensionForLibrary(project: Project) {
        if (!project.isKmp()) {
            withJavadocJar()
            withSourcesJar()
        }
    }

    override fun apply(target: Project) {
        target.applyPlugins()
        val extension = target.extensions.getByType(JavaPluginExtension::class.java)
        extension.setupCompatibility()
        extension.setupJavaExtensionForLibrary(target)

        qualityMetric.apply(target)
        toolChainConfigurator.configure(target)
        repositoryConfiguration.configure(target)
        junitConfiguration.configure(target)
        koin.apply(target)
    }
}
