import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompileCommon
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile

plugins {
    alias(buildDependencyCatalog.plugins.openapi)
    alias(buildDependencyCatalog.plugins.android.library)
    alias(buildDependencyCatalog.plugins.kotlin.kmp)
}

val openApiOutputDir = "$projectDir/build/generated/openapi"

openApiGenerate {
    generatorName = "kotlin"
    inputSpec = "$rootDir/api-contract/spec/offer-api.yml"
    outputDir = openApiOutputDir
    apiPackage = "com.airalo.sample.api.client"
    modelPackage = "com.airalo.sample.api.model"
    modelNameSuffix = "DTO"
    generateApiDocumentation = false
    generateModelDocumentation = false
    configOptions = mapOf(
        "omitGradlePluginVersions" to "true",
        "omitGradleWrapper" to "true",
        "dateLibrary" to "kotlinx-datetime",
    )
    additionalProperties = mapOf(
        "nonPublicApi" to "true",
    )
    library = "multiplatform"
}

val openApiGenerate: Task by tasks.getting {
    outputs.upToDateWhen { false }

    val folders = listOf(
        "test",
        "commonTest",
        "jsTest",
        "jvmTest",
        "iosTest",
    )
    val files = listOf(
        "PartConfig.kt",
        "OctetByteArray.kt",
        "Bytes.kt",
        "Base64ByteArray.kt",
        "ApiAbstractions.kt",
    )

    doLast {
        val src = project.file("$openApiOutputDir/src")
        src.listFiles()?.forEach { file ->
            if (file.isDirectory && folders.contains(file.name)) {
                file.deleteRecursively()
            } else {
                file.walkTopDown().forEach { deepFile ->
                    if (deepFile.isFile && files.contains(deepFile.name.substringAfterLast("/"))) {
                        deepFile.delete()
                    }
                }
            }
        }
    }
}

val moduleName = "com.airalo.sample.business"

android {
    namespace = moduleName
    compileSdk = 35

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

kotlin {
    androidTarget()
}

listOf(KotlinCompile::class.java, KotlinNativeCompile::class.java, KotlinCompileCommon::class.java).forEach { flavour ->
    tasks.withType(flavour) {
        compilerOptions {
            freeCompilerArgs.add("-Xexpect-actual-classes")
        }

        dependsOn(openApiGenerate)
        mustRunAfter(openApiGenerate)
    }
}

tasks.withType(Test::class.java) {
    useJUnitPlatform()
}