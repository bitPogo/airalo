plugins {
    id(dependencyCatalog.plugins.convention.kmp.jvm.get().pluginId)
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
                implementation(dependencyCatalog.ktor.core)
                implementation(testDependencyCatalog.ktor.mockclient)
            }
        }

        commonTest {
            dependencies {
                implementation(testDependencyCatalog.coroutine)
                implementation(testDependencyCatalog.kfixture)
            }
        }
    }
}
