package com.airalo.example.gradle.convention.android

import javax.inject.Inject
import org.gradle.api.Plugin
import org.gradle.api.Project

class LibraryWithComposeConfiguration(
    private val androidLibraryConvention: Plugin<Project>,
    private val composeConvention: Plugin<Project>,
) : Plugin<Project> {
    @Inject
    constructor() : this(
        androidLibraryConvention = LibraryConfiguration(),
        composeConvention = ComposeConfiguration(),
    )

    override fun apply(target: Project) {
        androidLibraryConvention.apply(target)
        composeConvention.apply(target)
    }
}
