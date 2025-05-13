@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("dependencyCatalog") {
            from(files("../gradle/runtime.versions.toml"))
        }

        create("buildDependencyCatalog") {
            from(files("../gradle/build.versions.toml"))
        }

        create("testDependencyCatalog") {
            from(files("../gradle/test.versions.toml"))
        }
    }
}

rootProject.name = "conventions"
