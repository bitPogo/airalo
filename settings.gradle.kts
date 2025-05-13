pluginManagement {
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
dependencyResolutionManagement {
    val antibytesPlugins = "^tech\\.antibytes\\.[\\.a-z\\-]+"
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
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
}
dependencyResolutionManagement {
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

rootProject.name = "Airalo Sample Project"
include(
    ":app",
    ":offer:business"
)
