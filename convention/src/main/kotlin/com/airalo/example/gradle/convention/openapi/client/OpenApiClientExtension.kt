package com.airalo.example.gradle.convention.openapi.client

import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input

interface OpenApiClientExtension {
    @get:Input
    val apiContracts: ListProperty<String>

    @get:Input
    val packageName: Property<String>
}
