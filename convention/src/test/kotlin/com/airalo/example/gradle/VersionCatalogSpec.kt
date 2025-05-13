package com.airalo.example.gradle

import com.airalo.example.gradle.convention.composeKmpCatalog
import com.airalo.example.gradle.convention.dependencyCatalog
import com.airalo.example.gradle.convention.testDependencyCatalog
import io.mockk.every
import io.mockk.mockk
import kotlin.test.assertSame
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.junit.jupiter.api.Test

class VersionCatalogSpec {
    @Test
    fun `It propagates the App VersionCatalog to the BuildDependencies`() {
        // Given
        val versionCatalog: VersionCatalog = mockk()
        val project: Project = mockk {
            every { extensions } returns mockk {
                every { getByType(VersionCatalogsExtension::class.java) } returns mockk {
                    every { named("dependencyCatalog") } returns versionCatalog
                }
            }
        }

        // When
        val actual = project.dependencyCatalog

        // Then
        assertSame(
            actual = actual,
            expected = versionCatalog,
        )
    }

    @Test
    fun `It propagates the Test VersionCatalog to the BuildDependencies`() {
        // Given
        val versionCatalog: VersionCatalog = mockk()
        val project: Project = mockk {
            every { extensions } returns mockk {
                every { getByType(VersionCatalogsExtension::class.java) } returns mockk {
                    every { named("testDependencyCatalog") } returns versionCatalog
                }
            }
        }

        // When
        val actual = project.testDependencyCatalog

        // Then
        assertSame(
            actual = actual,
            expected = versionCatalog,
        )
    }

    @Test
    fun `It propagates the Compose VersionCatalog to the BuildDependencies`() {
        // Given
        val versionCatalog: VersionCatalog = mockk()
        val project: Project = mockk {
            every { extensions } returns mockk {
                every { getByType(VersionCatalogsExtension::class.java) } returns mockk {
                    every { named("compose") } returns versionCatalog
                }
            }
        }

        // When
        val actual = project.composeKmpCatalog

        // Then
        assertSame(
            actual = actual,
            expected = versionCatalog,
        )
    }
}
