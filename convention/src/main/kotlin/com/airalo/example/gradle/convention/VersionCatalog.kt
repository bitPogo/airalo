package com.airalo.example.gradle.convention

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension

private val Project.versionCatalog: VersionCatalogsExtension
    get() = extensions.getByType(VersionCatalogsExtension::class.java)

internal val Project.dependencyCatalog: VersionCatalog
    get() = versionCatalog.named("dependencyCatalog")

internal val Project.testDependencyCatalog: VersionCatalog
    get() = versionCatalog.named("testDependencyCatalog")

internal val Project.composeKmpCatalog: VersionCatalog
    get() = versionCatalog.named("compose")
