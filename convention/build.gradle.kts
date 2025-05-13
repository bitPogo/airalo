plugins {
    `kotlin-dsl`
    alias(buildDependencyCatalog.plugins.buildConfig)
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
}

group = "com.airalo.example.convention"
val sdkDir = rootProject.file("../local.properties").readText().split("=").last().trim()

buildConfig {
    packageName("com.airalo.example.gradle.config")
    buildConfigField("androidLibraryId", buildDependencyCatalog.plugins.android.library.get().pluginId)
    buildConfigField("androidApplicationId", buildDependencyCatalog.plugins.android.application.get().pluginId)
    buildConfigField("kotlinAndroidId", buildDependencyCatalog.plugins.kotlin.android.get().pluginId)
    buildConfigField("javaLibraryId", "java-library")
    buildConfigField("kotlinJvmId", buildDependencyCatalog.plugins.kotlin.jvm.get().pluginId)
    buildConfigField("kotlinMultiplatformId", buildDependencyCatalog.plugins.kotlin.multiplatform.get().pluginId)
    buildConfigField("spotlessId", buildDependencyCatalog.plugins.spotless.get().pluginId)
    buildConfigField("detektId", buildDependencyCatalog.plugins.detekt.get().pluginId)
    buildConfigField("detektVersion", buildDependencyCatalog.versions.detekt.get())
    buildConfigField("ktlintVersion", buildDependencyCatalog.versions.ktlint)
    buildConfigField("androidToolchainVersion", buildDependencyCatalog.versions.java.android.toolchain.get())
    buildConfigField("androidCompabilityVersion", buildDependencyCatalog.versions.java.android.compability.get())
    buildConfigField("androidMinSdk", buildDependencyCatalog.versions.sdk.min.get())
    buildConfigField("androidTargetSdk", buildDependencyCatalog.versions.sdk.target.get())
    buildConfigField("dependencyHealthId", buildDependencyCatalog.plugins.dependencyHealth.get().pluginId)
    buildConfigField("owaspId", buildDependencyCatalog.plugins.owasp.get().pluginId)
    buildConfigField("koverId", buildDependencyCatalog.plugins.kover.get().pluginId)
    buildConfigField("jacocoVersion", buildDependencyCatalog.versions.jacoco.get())
    buildConfigField("javaToolchainVersion", buildDependencyCatalog.versions.java.android.toolchain.get())
    buildConfigField("licenseCheckId", buildDependencyCatalog.plugins.licenseCheck.get().pluginId)
    buildConfigField("licenseComplianceId", buildDependencyCatalog.plugins.licenseCompliance.get().pluginId)
    buildConfigField("kmpId", buildDependencyCatalog.plugins.kotlin.multiplatform.get().pluginId)
    buildConfigField("jbComposeId", buildDependencyCatalog.plugins.jetbrains.compose.core.get().pluginId)
    buildConfigField("composeCompilerId", buildDependencyCatalog.plugins.jetbrains.compose.compiler.get().pluginId)
    buildConfigField("roborazziId", buildDependencyCatalog.plugins.roborazzi.get().pluginId)
    buildConfigField("kspId", buildDependencyCatalog.plugins.ksp.get().pluginId)
    buildConfigField("openapiId", buildDependencyCatalog.plugins.openapi.get().pluginId)
    buildConfigField("androidSdk", sdkDir)
}

dependencies {
    implementation(buildDependencyCatalog.spotless)
    implementation(buildDependencyCatalog.ktlint)
    implementation(buildDependencyCatalog.agp)
    implementation(buildDependencyCatalog.kotlin)
    implementation(buildDependencyCatalog.detekt)
    implementation(buildDependencyCatalog.dependencyHealth)
    implementation(buildDependencyCatalog.owasp)
    implementation(buildDependencyCatalog.kover)
    implementation(buildDependencyCatalog.jacoco)
    implementation(buildDependencyCatalog.licenseCheck)
    implementation(buildDependencyCatalog.licenseCompliance)
    implementation(buildDependencyCatalog.jetbrains.compose)
    implementation(buildDependencyCatalog.compose.compiler)
    implementation(buildDependencyCatalog.cashapp.sqldelight) // https://github.com/cashapp/sqldelight/issues/2438
    implementation(buildDependencyCatalog.roborazzi)
    implementation(buildDependencyCatalog.ksp)
    implementation(buildDependencyCatalog.openapi.generator)
    implementation(buildDependencyCatalog.kotlin.embedded)

    testImplementation(testDependencyCatalog.kotlin.junit5)
    testImplementation(platform(testDependencyCatalog.junit5.bom))
    testImplementation(testDependencyCatalog.junit5.runtime)
    testImplementation(testDependencyCatalog.mockk.core)
    testImplementation(testDependencyCatalog.jvmFixture)
    testImplementation(gradleTestKit())
}

gradlePlugin {
    plugins {
        register("androidApplicationConfiguration") {
            id = "com.airalo.example.plugin.android-application-configuration"
            implementationClass = "com.airalo.example.gradle.convention.android.ApplicationConfiguration"
        }
        register("androidLibraryConfiguration") {
            id = "com.airalo.example.plugin.android-library-configuration"
            implementationClass = "com.airalo.example.gradle.convention.android.LibraryConfiguration"
        }
        register("androidLibraryComposeConfiguration") {
            id = "com.airalo.example.plugin.android-library-compose-configuration"
            implementationClass = "com.airalo.example.gradle.convention.android.LibraryWithComposeConfiguration"
        }
        register("kmpAndroidConfiguration") {
            id = "com.airalo.example.plugin.kmp-android-configuration"
            implementationClass = "com.airalo.example.gradle.convention.kmp.KmpAndroidConfiguration"
        }
        register("kmpAndroidWithComposeConfiguration") {
            id = "com.airalo.example.plugin.kmp-android-compose-configuration"
            implementationClass = "com.airalo.example.gradle.convention.kmp.KmpAndroidWithComposeConfiguration"
        }
        register("kmpJvmConfiguration") {
            id = "com.airalo.example.plugin.kmp-jvm-configuration"
            implementationClass = "com.airalo.example.gradle.convention.kmp.KmpJvmConfiguration"
        }
        register("DependencyAnalysis") {
            id = "com.airalo.example.plugin.dependency-analysis-configuration"
            implementationClass = "com.airalo.example.gradle.convention.quality.dependency.DependencyAnalysis"
        }
        register("qualityMetricsConfiguration") {
            id = "com.airalo.example.plugin.quality-metric-configuration"
            implementationClass = "com.airalo.example.gradle.convention.quality.metric.QualityMetric"
        }
        register("staticAnalysisConfiguration") {
            id = "com.airalo.example.plugin.static-analysis-configuration"
            implementationClass = "com.airalo.example.gradle.convention.quality.staticanalysis.StaticAnalysisConventions"
        }
        register("openapiInfrastructureConfiguration") {
            id = "com.airalo.example.plugin.openapi-infrastructure"
            implementationClass = "com.airalo.example.gradle.convention.openapi.infrastructure.OpenApiInfrastructureGenerator"
        }
        register("openapiClientConfiguration") {
            id = "com.airalo.example.plugin.openapi-client"
            implementationClass = "com.airalo.example.gradle.convention.openapi.client.OpenApiClientGenerator"
        }
    }
}

val javaVersion = buildDependencyCatalog.versions.java.jvm.get().toInt()

extensions.getByType(JavaPluginExtension::class.java).apply {
    toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))

    tasks.withType(Test::class.java) {
        useJUnitPlatform()
    }
}
