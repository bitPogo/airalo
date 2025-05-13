plugins {
    alias(dependencyCatalog.plugins.convention.check.dependency)
    alias(dependencyCatalog.plugins.convention.check.staticAnalysis)
    alias(dependencyCatalog.plugins.convention.check.qualtyMetric)
}

kover {
    reports {
        total {
            filters {
                excludes {
                    packages(
                        "org.openapitools.client",
                        "org.koin.ksp.generated",
                    )
                }
            }
        }
    }
}

spotless {
    format("misc") {
        targetExclude("**/api-contract/**")
    }
}
