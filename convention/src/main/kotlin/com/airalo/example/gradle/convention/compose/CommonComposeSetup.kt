package com.airalo.example.gradle.convention.compose

import com.android.build.api.dsl.CommonExtension

internal fun CommonExtension<*, *, *, *, *, *>.configureCompose() {
    buildFeatures.apply {
        compose = true
    }
}
