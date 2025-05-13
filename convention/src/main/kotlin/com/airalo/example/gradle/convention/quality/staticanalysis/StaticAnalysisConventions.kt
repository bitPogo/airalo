package com.airalo.example.gradle.convention.quality.staticanalysis

import javax.inject.Inject
import org.gradle.api.Plugin
import org.gradle.api.Project

class StaticAnalysisConventions(
    private val spotless: StaticAnalysisConventionContract,
    private val detekt: StaticAnalysisConventionContract,
    private val androidLint: StaticAnalysisConventionContract,
) : Plugin<Project> {
    @Inject
    constructor() : this(
        spotless = Spotless,
        detekt = Detekt,
        androidLint = AndroidLint,
    )

    override fun apply(target: Project) {
        spotless.apply(target)
        detekt.apply(target)

        target.allprojects {
            androidLint.apply(this)
        }
    }
}
