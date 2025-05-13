@file:Suppress("ktlint:standard:max-line-length")

package com.airalo.example.gradle.convention

import com.airalo.example.gradle.convention.GradleUtilApiContract.PlatformContext
import io.mockk.every
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.junit.jupiter.api.Test

class PlatformContextResolverSpec {
    @Test
    fun `It fulfils PlatformTypeResolver`() {
        val types: Any = PlatformContextResolver

        assertTrue(types is GradleUtilApiContract.PlatformContextResolver)
    }

    @Test
    fun `When getType is called with a Project, it returns a Set which contains only Unknown context if the project type is not set`() {
        // Given
        val project: Project = mockk()

        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns false
        every { project.plugins.hasPlugin("com.android.application") } returns false
        every { project.plugins.hasPlugin("com.android.library") } returns false
        every { project.plugins.hasPlugin("java-library") } returns false

        // When
        val result = PlatformContextResolver.getType(project)

        // Then
        assertEquals(
            expected = setOf(PlatformContext.UNKNOWN),
            actual = result,
        )
    }

    @Test
    fun `When getType is called with a Project, it returns a Set which contains JVM if the project type is a java library`() {
        // Given
        val project: Project = mockk()

        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns false
        every { project.plugins.hasPlugin("com.android.application") } returns false
        every { project.plugins.hasPlugin("com.android.library") } returns false
        every { project.plugins.hasPlugin("java-library") } returns true

        // When
        val result = PlatformContextResolver.getType(project)

        // Then
        assertEquals(
            expected = setOf(PlatformContext.JVM_LIBRARY),
            actual = result,
        )
    }

    @Test
    fun `When getType is called with a Project, it returns a Set which contains JVM KMP context if the project has a JVM target and KMP is in use`() {
        // Given
        val project: Project = mockk()

        val kotlinExtension: KotlinMultiplatformExtension = mockk()

        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns true
        every { project.plugins.hasPlugin("com.android.library") } returns false
        every { project.plugins.hasPlugin("com.android.application") } returns false
        every { project.plugins.hasPlugin("java-library") } returns false

        every { project.extensions.getByName("kotlin") } returns kotlinExtension
        every { kotlinExtension.targets.asMap } returns sortedMapOf("jvm" to mockk())

        // When
        val result = PlatformContextResolver.getType(project)

        // Then
        assertEquals(
            expected = setOf(PlatformContext.JVM_LIBRARY_KMP),
            actual = result,
        )
    }

    @Test
    fun `When getType is called with a Project, it returns a Set which contains only a AndroidLibrary context if the project is a AndroidLibrary`() {
        // Given
        val project: Project = mockk()

        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns false
        every { project.plugins.hasPlugin("com.android.application") } returns false
        every { project.plugins.hasPlugin("com.android.library") } returns true
        every { project.plugins.hasPlugin("java-library") } returns false

        // When
        val result = PlatformContextResolver.getType(project)

        // Then
        assertEquals(
            expected = setOf(PlatformContext.ANDROID_LIBRARY),
            actual = result,
        )
    }

    @Test
    fun `When getType is called with a Project, it returns a Set which contains a AndroidLibrary context for KMP if the project is a AndroidLibrary and KMP is in use`() {
        // Given
        val project: Project = mockk()

        val kotlinExtension: KotlinMultiplatformExtension = mockk()

        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns true
        every { project.plugins.hasPlugin("com.android.application") } returns false
        every { project.plugins.hasPlugin("com.android.library") } returns true
        every { project.plugins.hasPlugin("java-library") } returns false

        every { project.extensions.getByName("kotlin") } returns kotlinExtension
        every { kotlinExtension.targets.asMap } returns sortedMapOf("android" to mockk())

        // When
        val result = PlatformContextResolver.getType(project)

        // Then
        assertEquals(
            expected = setOf(PlatformContext.ANDROID_LIBRARY_KMP),
            actual = result,
        )
    }

    @Test
    fun `When getType is called with a Project, it returns a Set which contains only a AndroidApplication context if the project is a AndroidApplication`() {
        // Given
        val project: Project = mockk()

        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns false
        every { project.plugins.hasPlugin("com.android.application") } returns true
        every { project.plugins.hasPlugin("com.android.library") } returns false
        every { project.plugins.hasPlugin("java-library") } returns false

        // When
        val result = PlatformContextResolver.getType(project)

        // Then
        assertEquals(
            expected = setOf(PlatformContext.ANDROID_APPLICATION),
            actual = result,
        )
    }

    @Test
    fun `When getType is called with a Project, it returns a Set which contains a multiple contexts for KMP if the project uses KMP and has multiple targets`() {
        // Given
        val project: Project = mockk()

        val kotlinExtension: KotlinMultiplatformExtension = mockk()

        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns true
        every { project.plugins.hasPlugin("com.android.application") } returns false
        every { project.plugins.hasPlugin("com.android.library") } returns true
        every { project.plugins.hasPlugin("java-library") } returns false

        every { project.extensions.getByName("kotlin") } returns kotlinExtension
        every { kotlinExtension.targets.asMap } returns sortedMapOf(
            "android" to mockk(),
            "common" to mockk(),
            "jvm" to mockk(),
        )

        // When
        val result = PlatformContextResolver.getType(project)

        // Then
        assertEquals(
            expected = setOf(
                PlatformContext.ANDROID_LIBRARY_KMP,
                PlatformContext.JVM_LIBRARY_KMP,
            ),
            actual = result,
        )
    }

    @Test
    fun `When getType is called with a Project, it returns a empty Set which contains a multiple contexts for KMP if the project uses KMP and no target is known`() {
        // Given
        val project: Project = mockk()

        val kotlinExtension: KotlinMultiplatformExtension = mockk()

        every { project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") } returns true
        every { project.plugins.hasPlugin("com.android.application") } returns false
        every { project.plugins.hasPlugin("com.android.library") } returns false
        every { project.plugins.hasPlugin("java-library") } returns false

        every { project.extensions.getByName("kotlin") } returns kotlinExtension
        every { kotlinExtension.targets.asMap } returns sortedMapOf(
            "common" to mockk(),
            "iOS" to mockk(),
        )

        // When
        val result = PlatformContextResolver.getType(project)

        // Then
        assertEquals(
            expected = emptySet(),
            actual = result,
        )
    }
}
