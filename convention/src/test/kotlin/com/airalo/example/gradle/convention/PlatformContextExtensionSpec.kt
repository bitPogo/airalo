package com.airalo.example.gradle.convention

import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test

class PlatformContextExtensionSpec {
    @Test
    fun `When isAndroidLibrary is called, it returns false, if it is not an AndroidLibraryContext`() {
        // Given
        val context = GradleUtilApiContract.PlatformContext.JVM_LIBRARY

        // When
        val result = context.isAndroidLibrary()

        // Then
        assertFalse(result)
    }

    @Test
    fun `When isAndroidLibrary is called, it returns true, if it is an AndroidLibraryContext`() {
        // Given
        val context = GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY

        // When
        val result = context.isAndroidLibrary()

        // Then
        assertTrue(result)
    }

    @Test
    fun `When isAndroidLibrary is called, it returns true, if it is an AndroidLibraryContext for KMP`() {
        // Given
        val context = GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY_KMP

        // When
        val result = context.isAndroidLibrary()

        // Then
        assertTrue(result)
    }

    @Test
    fun `When isAndroidApplication is called, it returns false, if it is not an AndroidLibraryContext`() {
        // Given
        val context = GradleUtilApiContract.PlatformContext.JVM_LIBRARY

        // When
        val result = context.isAndroidApplication()

        // Then
        assertFalse(result)
    }

    @Test
    fun `When isAndroidApplication is called, it returns true, if it is an AndroidApplicationContext`() {
        // Given
        val context = GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION

        // When
        val result = context.isAndroidApplication()

        // Then
        assertTrue(result)
    }

    @Test
    fun `When isJvmLibrary is called, it returns false, if it is not an JvmLibraryContext`() {
        // Given
        val context = GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY

        // When
        val result = context.isJvmLibrary()

        // Then
        assertFalse(result)
    }

    @Test
    fun `When isJvmLibrary is called, it returns true, if it is an JvmLibraryContext`() {
        // Given
        val context = GradleUtilApiContract.PlatformContext.JVM_LIBRARY

        // When
        val result = context.isJvmLibrary()

        // Then
        assertTrue(result)
    }

    @Test
    fun `When isJvmLibrary is called, it returns true, if it is an JvmLibraryContext for KMP`() {
        // Given
        val context = GradleUtilApiContract.PlatformContext.JVM_LIBRARY_KMP

        // When
        val result = context.isJvmLibrary()

        // Then
        assertTrue(result)
    }

    @Test
    fun `When isKmp is called with ANDROID_APPLICATION it return false`() {
        // Given
        val context = GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION

        // When
        val result = context.isKmp()

        // Then
        assertFalse(result)
    }

    @Test
    fun `When isKmp is called with ANDROID_LIBRARY it return false`() {
        // Given
        val context = GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY

        // When
        val result = context.isKmp()

        // Then
        assertFalse(result)
    }

    @Test
    fun `When isKmp is called with JVM it return false`() {
        // Given
        val context = GradleUtilApiContract.PlatformContext.JVM_LIBRARY

        // When
        val result = context.isKmp()

        // Then
        assertFalse(result)
    }

    @Test
    fun `When isKmp is called with ANDROID_LIBRARY_KMP it return true`() {
        // Given
        val context = GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY_KMP

        // When
        val result = context.isKmp()

        // Then
        assertTrue(result)
    }

    @Test
    fun `When isKmp is called with JVM_KMP it return true`() {
        // Given
        val context = GradleUtilApiContract.PlatformContext.JVM_LIBRARY_KMP

        // When
        val result = context.isKmp()

        // Then
        assertTrue(result)
    }

    @Test
    fun `When hasAndroidLibrary is called, it returns false, if the Set is empty`() {
        // Given
        val set = setOf<GradleUtilApiContract.PlatformContext>()

        // When
        val result = set.hasAndroidLibrary()

        // Then
        assertFalse(result)
    }

    @Test
    fun `When hasAndroidLibrary is called, it returns false, if the Set contains not an Android Library Variant`() {
        // Given
        val set = setOf(
            GradleUtilApiContract.PlatformContext.JVM_LIBRARY_KMP,
        )

        // When
        val result = set.hasAndroidLibrary()

        // Then
        assertFalse(result)
    }

    @Test
    fun `When hasAndroidLibrary is called, it returns true, if the Set contains a ANDROID_LIBRARY`() {
        // Given
        val set = setOf(GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY)

        // When
        val result = set.hasAndroidLibrary()

        // Then
        assertTrue(result)
    }

    @Test
    fun `When hasAndroidLibrary is called, it returns true, if the Set contains a ANDROID_LIBRARY_KMP`() {
        // Given
        val set = setOf(GradleUtilApiContract.PlatformContext.ANDROID_LIBRARY_KMP)

        // When
        val result = set.hasAndroidLibrary()

        // Then
        assertTrue(result)
    }

    @Test
    fun `When hasAndroidApplication is called, it returns false, if the Set is empty`() {
        // Given
        val set = setOf<GradleUtilApiContract.PlatformContext>()

        // When
        val result = set.hasAndroidApplication()

        // Then
        assertFalse(result)
    }

    @Test
    fun `When hasAndroidApplication is called, it returns false, if the Set contains not an Android Library Variant`() {
        // Given
        val set = setOf(
            GradleUtilApiContract.PlatformContext.JVM_LIBRARY_KMP,
        )

        // When
        val result = set.hasAndroidApplication()

        // Then
        assertFalse(result)
    }

    @Test
    fun `When hasAndroidApplication is called, it returns true, if the Set contains a ANDROID_APPLICATION`() {
        // Given
        val set = setOf(GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION)

        // When
        val result = set.hasAndroidApplication()

        // Then
        assertTrue(result)
    }

    @Test
    fun `When hasJvmLibrary is called, it returns false, if the Set contains an JVM Library Variant`() {
        // Given
        val set = setOf(
            GradleUtilApiContract.PlatformContext.ANDROID_APPLICATION,
        )

        // When
        val result = set.hasJvmLibrary()

        // Then
        assertFalse(result)
    }

    @Test
    fun `When hasJvmLibrary is called, it returns true, if the Set contains a JVM_LIBRARY`() {
        // Given
        val set = setOf(GradleUtilApiContract.PlatformContext.JVM_LIBRARY)

        // When
        val result = set.hasJvmLibrary()

        // Then
        assertTrue(result)
    }

    @Test
    fun `When hasJvmLibrary is called, it returns true, if the Set contains a JVM_LIBRARY_KMP`() {
        // Given
        val set = setOf(GradleUtilApiContract.PlatformContext.JVM_LIBRARY_KMP)

        // When
        val result = set.hasJvmLibrary()

        // Then
        assertTrue(result)
    }
}
