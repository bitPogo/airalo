plugins {
    id(dependencyCatalog.plugins.convention.kmp.android.compose.get().pluginId)
    id(dependencyCatalog.plugins.convention.openapi.infrastructure.get().pluginId)
    id(dependencyCatalog.plugins.convention.openapi.client.get().pluginId)
    alias(dependencyCatalog.plugins.serialization)
    alias(testDependencyCatalog.plugins.resources)
}

val projectPackage = "com.airalo.example.offer"

android {
    namespace = projectPackage
}

openApiContract {
    packageName.set(projectPackage)
    apiContracts.add("$rootDir/api-contract/spec/offer-api.yml")
}

dependencies {
    debugImplementation(dependencyCatalog.androidx.ui.tooling.preview)
    debugImplementation(dependencyCatalog.androidx.ui.tooling)
    debugImplementation(compose.uiTooling)
    debugImplementation(dependencyCatalog.androidx.ui.test.manifest)
}

kotlin {
    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.Experimental")
            }
        }

        commonMain {
            dependencies {
                implementation(projects.command)

                implementation(dependencyCatalog.jb.viewmodel)
                implementation(compose.foundation)
                implementation(compose.ui)

                implementation(dependencyCatalog.koin.composeVM)

                implementation(dependencyCatalog.ktor.contentNegotiation)
                implementation(dependencyCatalog.ktor.serialization.json)
                implementation(dependencyCatalog.ktor.serialization.core)
                implementation(dependencyCatalog.ktor.logging)
                implementation(dependencyCatalog.serialisation)
            }
        }

        commonTest {
            dependencies {
                implementation(testDependencyCatalog.anitbytes.assertions)
                implementation(projects.test.mockclientfactory)
                implementation(testDependencyCatalog.resources)
                implementation(testDependencyCatalog.coroutine)
                implementation(testDependencyCatalog.cashapp.turbine)
                implementation(testDependencyCatalog.ktor.mockclient)
                implementation(testDependencyCatalog.kfixture)
            }
        }

        androidMain {
            dependencies {
                implementation(dependencyCatalog.ktor.cio)
                implementation(compose.material3)
                implementation(dependencyCatalog.bundles.coil)
            }
        }

        androidUnitTest {
            dependencies {
                implementation(projects.test.roborazzi)
                implementation(testDependencyCatalog.coil)
            }
        }
    }
}
