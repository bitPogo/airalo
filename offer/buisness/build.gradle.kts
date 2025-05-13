plugins {
    id(dependencyCatalog.plugins.convention.openapi.infrastructure.get().pluginId)
    id(dependencyCatalog.plugins.convention.openapi.client.get().pluginId)
    alias(dependencyCatalog.plugins.serialization)
    alias(testDependencyCatalog.plugins.resources)
}

val projectPackage = "com.airalo.sample.offer.buisness"

android {
    namespace = projectPackage
}

openApiContract {
    packageName.set(projectPackage)
    apiContracts.add("$rootDir/api-contract/spec/offer-api.yml")
}

kotlin {
    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.Experimental")
            }
        }

        commonTest {
            dependencies {
                implementation(testDependencyCatalog.anitbytes.assertions)
                implementation(projects.test.mockclientfactory)
                implementation(testDependencyCatalog.resources)
                implementation(testDependencyCatalog.coroutine)
            }
        }
    }
}
