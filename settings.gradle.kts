enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("convention")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}

dependencyResolutionManagement {
    val antibytesPlugins = "^tech\\.antibytes\\.[\\.a-z\\-]+"
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven {
            setUrl("https://raw.github.com/bitPogo/maven-snapshots/main/snapshots")
            content {
                includeGroupByRegex(antibytesPlugins)
            }
        }
        maven {
            setUrl("https://raw.github.com/bitPogo/maven-rolling-releases/main/rolling")
            content {
                includeGroupByRegex(antibytesPlugins)
            }
        }
    }

    versionCatalogs {
        create("dependencyCatalog") {
            from(files("./gradle/runtime.versions.toml"))
        }

        create("buildDependencyCatalog") {
            from(files("./gradle/build.versions.toml"))
        }

        create("testDependencyCatalog") {
            from(files("./gradle/test.versions.toml"))
        }
    }
}

rootProject.name = "Airalo_Sample_Project"
include(
    ":app",
    ":command",
    ":offer",
)

include(
    ":test:mockclientfactory",
    ":test:roborazzi"
)
