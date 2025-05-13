@file:Suppress("ktlint:standard:max-line-length")

package com.airalo.example.gradle.convention.android.android

import com.airalo.example.gradle.config.BuildConfig
import com.airalo.example.gradle.convention.android.AndroidConfigurationApiContract.Companion.ANDROID_PREFIX
import com.airalo.example.gradle.convention.android.AndroidConfigurationApiContract.Companion.ANDROID_PREFIX_SEPARATOR
import com.airalo.example.gradle.convention.android.ConfigurationContract
import com.airalo.example.gradle.invokeGradleAction
import com.android.build.api.dsl.LibraryExtension
import com.appmattus.kotlinfixture.kotlinFixture
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AndroidLibraryConfiguratorSpec {
    private val fixture = kotlinFixture()

    private val common: CommonAndroidConfigurator = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        clearMocks(common)
    }

    @Test
    fun `It fulfils ParameterlessConfigurator`() {
        val configurator: Any = AndroidLibraryConfigurator(common)

        @Suppress("KotlinConstantConditions")
        assertTrue(configurator is ConfigurationContract.ParameterlessConfigurator)
    }

    @Test
    fun `When configure is called with a Project, it delegates the call to CommonAndroidConfigurator`() {
        // Given
        val common: CommonAndroidConfigurator = mockk(relaxed = true)
        val project: Project = mockk {
            every { name } returns fixture()
        }

        // When
        val extensions: ExtensionContainer = mockk()
        val libraryExtension: LibraryExtension = mockk(relaxed = true)

        every { project.extensions } returns extensions
        every { extensions.configure(any<Class<Any>>(), any()) } returns mockk()

        invokeGradleAction(libraryExtension) { probe ->
            extensions.configure(LibraryExtension::class.java, probe)
        }

        // When
        AndroidLibraryConfigurator(common).configure(project)

        // Then
        verify(exactly = 1) {
            common.configure(project, libraryExtension)
        }
    }

    @Test
    fun `When configure is called with a Project, it sets the Prefix`() {
        // Given
        val projectName = "lala-laaala"
        val fakeProject: Project = ProjectBuilder.builder().build().also {
            it.plugins.apply(BuildConfig.androidLibraryId)
        }

        val extension = fakeProject.extensions.getByType(LibraryExtension::class.java).also {
            it.namespace = "test"
        }
        val project: Project = spyk(fakeProject) {
            every { name } returns projectName
        }

        // When
        AndroidLibraryConfigurator(common).configure(project)

        // Then
        assertEquals(
            actual = extension.resourcePrefix,
            expected = "${ANDROID_PREFIX}${ANDROID_PREFIX_SEPARATOR}lala${ANDROID_PREFIX_SEPARATOR}laaala${ANDROID_PREFIX_SEPARATOR}",
        )
    }

    @Test
    fun `When configure is called it sets up the default proguard file`() {
        // Given
        val fakeProject: Project = ProjectBuilder.builder().build().also {
            it.plugins.apply(BuildConfig.androidLibraryId)
        }
        val extension = fakeProject.extensions.getByType(LibraryExtension::class.java).also {
            it.namespace = "test"
        }

        val common: CommonAndroidConfigurator = mockk(relaxed = true)

        // When
        AndroidLibraryConfigurator(common).configure(fakeProject)

        // Then
        assertEquals(
            actual = extension.defaultConfig.consumerProguardFiles,
            expected = listOf(fakeProject.file("consumer-rules.pro")),
        )
    }
}
