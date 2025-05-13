package com.airalo.example.gradle.convention.java

import org.gradle.api.Project

fun interface ParameterlessConfiguratorContract {
    fun configure(project: Project)
}
