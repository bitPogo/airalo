package com.airalo.example.gradle.convention.quality.staticanalysis

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlin.test.assertTrue
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke
import org.junit.jupiter.api.Test

class StaticAnalysisConventionsSpec {
    @Test
    fun `When apply is called with a Project it delegates it to several Conventions`() {
        // Given
        val spotless: StaticAnalysisConventionContract = mockk(relaxUnitFun = true)
        val detekt: StaticAnalysisConventionContract = mockk(relaxUnitFun = true)
        val androidLint: StaticAnalysisConventionContract = mockk(relaxUnitFun = true)

        val rootAll = slot<Action<Project>>()
        val subproject: Project = mockk()
        val project: Project = mockk {
            every { allprojects(capture(rootAll)) } returns mockk()
        }

        // When
        StaticAnalysisConventions(
            spotless = spotless,
            detekt = detekt,
            androidLint = androidLint,
        ).apply(project)

        rootAll.captured.invoke(subproject)

        // Then
        verify(exactly = 1) {
            spotless.apply(project)
            detekt.apply(project)
            androidLint.apply(subproject)
        }
    }

    @Test
    fun `It fulfils Plugin`() {
        assertTrue {
            @Suppress("USELESS_IS_CHECK")
            StaticAnalysisConventions() is Plugin<*>
        }
    }
}
