package com.airalo.example.gradle.convention.quality.metric

import com.airalo.example.gradle.config.BuildConfig
import com.airalo.example.gradle.convention.GradleUtilApiContract.PlatformContext
import com.airalo.example.gradle.convention.GradleUtilApiContract.PlatformContextResolver
import com.airalo.example.gradle.invokeGradleAction
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.kover.gradle.plugin.dsl.KoverCurrentProjectVariantsConfig
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import kotlinx.kover.gradle.plugin.dsl.KoverVariantCreateConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.internal.project.DefaultProject
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.PluginContainer
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class QualityMetricSpec {
    @TempDir
    private lateinit var rootProjectDir: File

    @TempDir
    private lateinit var subProjectDir: File
    private lateinit var fakeRootProject: Project
    private lateinit var fakeProject: Project

    @BeforeEach
    fun setup() {
        fakeRootProject = ProjectBuilder.builder()
            .withProjectDir(rootProjectDir)
            .build()
        fakeProject = ProjectBuilder.builder()
            .withProjectDir(subProjectDir)
            .withParent(fakeRootProject).build()
    }

    @Test
    fun `When apply is called it does nothing for non Android or Jvm Projects`() {
        // Given
        val contextResolver: PlatformContextResolver = mockk {
            every { getType(any()) } returns setOf(PlatformContext.UNKNOWN)
        }
        val pluginContainer: PluginContainer = mockk(relaxed = true)
        val project: Project = mockk(relaxed = true) {
            every { rootProject } returns mockk()
            every { plugins } returns pluginContainer
        }

        // When
        QualityMetric(contextResolver).apply(project)

        // Then
        confirmVerified(pluginContainer)
    }

    @Test
    fun `When apply is called it applies the KoverPlugin for AndroidApplications`() {
        // Given
        val contextResolver: PlatformContextResolver = mockk {
            every { getType(any()) } returns setOf(PlatformContext.ANDROID_APPLICATION)
        }
        fakeProject.plugins.apply(BuildConfig.androidApplicationId)
        fakeProject.plugins.apply(BuildConfig.koverId)
        val pluginContainer: PluginContainer = mockk(relaxed = true)
        val project: Project = spyk(fakeProject) {
            every { plugins } returns pluginContainer
        }

        // When
        QualityMetric(contextResolver).apply(project)

        // Then
        verify(exactly = 1) { pluginContainer.apply(BuildConfig.koverId) }
    }

    @Test
    fun `When apply is called it applies it configures Kover for AndroidApplications`() {
        // Given
        fakeProject.plugins.apply(BuildConfig.androidApplicationId)
        fakeProject.plugins.apply(BuildConfig.koverId)

        val contextResolver: PlatformContextResolver = mockk {
            every { getType(any()) } returns setOf(PlatformContext.ANDROID_APPLICATION)
        }
        val variantsConfiguration: KoverCurrentProjectVariantsConfig = mockk(relaxed = true)
        val extension: KoverProjectExtension = spyk(fakeProject.extensions.getByType(KoverProjectExtension::class.java))
        val pluginContainer: PluginContainer = mockk(relaxed = true)
        val project: Project = spyk(fakeProject) {
            every { plugins } returns pluginContainer
            every { extensions } returns spyk(fakeProject.extensions) {
                every { getByType(KoverProjectExtension::class.java) } returns extension
            }
        }
        invokeGradleAction(
            variantsConfiguration,
        ) { probe ->
            extension.currentProject(probe)
        }

        fakeProject.extensions.getByType(ApplicationExtension::class.java).apply {
            namespace = "test"
            compileSdk = 34
        }

        // When
        QualityMetric(contextResolver).apply(project)
        (project as DefaultProject).evaluate()

        // Then
        assertEquals(
            actual = extension.jacocoVersion.get(),
            expected = BuildConfig.jacocoVersion,
        )
        verify(exactly = 0) { variantsConfiguration.createVariant("custom", any()) }
    }

    @Test
    fun `When apply is called it applies it configures Kover for AndroidApplications with Flavours`() {
        // Given
        fakeProject.plugins.apply(BuildConfig.androidApplicationId)
        fakeProject.plugins.apply(BuildConfig.koverId)

        val contextResolver: PlatformContextResolver = mockk {
            every { getType(any()) } returns setOf(PlatformContext.ANDROID_APPLICATION)
        }
        val variantConfiguration: KoverVariantCreateConfig = mockk(relaxed = true)
        val variantsConfiguration: KoverCurrentProjectVariantsConfig = mockk(relaxed = true)
        val extension: KoverProjectExtension = spyk(fakeProject.extensions.getByType(KoverProjectExtension::class.java))
        val pluginContainer: PluginContainer = mockk(relaxed = true)
        val project: Project = spyk(fakeProject) {
            every { plugins } returns pluginContainer
            every { extensions } returns spyk(fakeProject.extensions) {
                every { getByType(KoverProjectExtension::class.java) } returns extension
            }
        }

        invokeGradleAction(
            variantsConfiguration,
        ) { probe ->
            extension.currentProject(probe)
        }
        invokeGradleAction(
            variantConfiguration,
        ) { probe ->
            variantsConfiguration.createVariant(any(), probe)
        }

        fakeProject.extensions.getByType(ApplicationExtension::class.java).apply {
            namespace = "test"
            compileSdk = 34
            flavorDimensions.add("abc")
            productFlavors {
                create("staging") {
                    versionNameSuffix = "-staging"
                    applicationIdSuffix = ".staging1"
                    dimension = "abc"
                }
                create("uat") {
                    versionNameSuffix = "-uat"
                    applicationIdSuffix = ".uat1"
                    dimension = "abc"
                }
            }
        }

        // When
        QualityMetric(contextResolver).apply(project)
        (project as DefaultProject).evaluate()

        // Then
        assertEquals(
            actual = extension.jacocoVersion.get(),
            expected = BuildConfig.jacocoVersion,
        )
        verify(exactly = 1) { variantsConfiguration.createVariant("custom", any()) }
        verify(exactly = 1) { variantConfiguration.addWithDependencies("stagingDebug") }
        confirmVerified(
            variantsConfiguration,
            variantConfiguration,
        )
    }

    @Test
    fun `When apply is called it applies it configures AndroidApplication for Kover`() {
        fakeProject.plugins.apply(BuildConfig.androidApplicationId)
        fakeProject.plugins.apply(BuildConfig.koverId)
        val extension: ApplicationExtension = fakeProject.extensions.getByType(ApplicationExtension::class.java)

        // When
        QualityMetric().apply(fakeProject)

        // Then
        assertEquals(
            actual = extension.testCoverage.jacocoVersion,
            expected = BuildConfig.jacocoVersion,
        )
        extension.buildTypes.forEach { type ->
            if (type.name != "release") {
                assertTrue(type.enableAndroidTestCoverage, "InstrumentedCoverage was not enabled for ${type.name}")
            }
        }

        extension.buildTypes.getByName("release").apply {
            assertFalse(enableAndroidTestCoverage, "InstrumentedCoverage was enabled for $name")
        }
    }

    @Test
    fun `When apply is called it applies the KoverPlugin for AndroidLibraries`() {
        // Given
        val contextResolver: PlatformContextResolver = mockk {
            every { getType(any()) } returns setOf(PlatformContext.ANDROID_LIBRARY)
        }
        fakeProject.plugins.apply(BuildConfig.androidLibraryId)
        fakeProject.plugins.apply(BuildConfig.koverId)
        val pluginContainer: PluginContainer = mockk(relaxed = true)
        val project: Project = spyk(fakeProject) {
            every { plugins } returns pluginContainer
        }

        // When
        QualityMetric(contextResolver).apply(project)

        // Then
        verify(exactly = 1) { pluginContainer.apply(BuildConfig.koverId) }
    }

    @Test
    fun `When apply is called it applies it configures Kover for AndroidLibraries`() {
        // Given
        fakeProject.plugins.apply(BuildConfig.androidLibraryId)
        fakeProject.plugins.apply(BuildConfig.koverId)

        val contextResolver: PlatformContextResolver = mockk {
            every { getType(any()) } returns setOf(PlatformContext.ANDROID_LIBRARY)
        }
        val variantsConfiguration: KoverCurrentProjectVariantsConfig = mockk(relaxed = true)
        val extension: KoverProjectExtension = spyk(fakeProject.extensions.getByType(KoverProjectExtension::class.java))
        val pluginContainer: PluginContainer = mockk(relaxed = true)
        val project: Project = spyk(fakeProject) {
            every { plugins } returns pluginContainer
            every { extensions } returns spyk(fakeProject.extensions) {
                every { getByType(KoverProjectExtension::class.java) } returns extension
            }
        }
        invokeGradleAction(
            variantsConfiguration,
        ) { probe ->
            extension.currentProject(probe)
        }

        fakeProject.extensions.getByType(LibraryExtension::class.java).apply {
            namespace = "test"
            compileSdk = 34
        }

        // When
        QualityMetric(contextResolver).apply(project)
        (project as DefaultProject).evaluate()

        // Then
        assertEquals(
            actual = extension.jacocoVersion.get(),
            expected = BuildConfig.jacocoVersion,
        )
        verify(exactly = 0) { variantsConfiguration.createVariant("custom", any()) }
    }

    @Test
    fun `When apply is called it applies it configures Kover for AndroidLibraries with Flavours`() {
        // Given
        fakeProject.plugins.apply(BuildConfig.androidLibraryId)
        fakeProject.plugins.apply(BuildConfig.koverId)

        val contextResolver: PlatformContextResolver = mockk {
            every { getType(any()) } returns setOf(PlatformContext.ANDROID_LIBRARY)
        }
        val variantConfiguration: KoverVariantCreateConfig = mockk(relaxed = true)
        val variantsConfiguration: KoverCurrentProjectVariantsConfig = mockk(relaxed = true)
        val extension: KoverProjectExtension = spyk(fakeProject.extensions.getByType(KoverProjectExtension::class.java))
        val pluginContainer: PluginContainer = mockk(relaxed = true)
        val project: Project = spyk(fakeProject) {
            every { plugins } returns pluginContainer
            every { extensions } returns spyk(fakeProject.extensions) {
                every { getByType(KoverProjectExtension::class.java) } returns extension
            }
        }

        invokeGradleAction(
            variantsConfiguration,
        ) { probe ->
            extension.currentProject(probe)
        }
        invokeGradleAction(
            variantConfiguration,
        ) { probe ->
            variantsConfiguration.createVariant(any(), probe)
        }

        fakeProject.extensions.getByType(LibraryExtension::class.java).apply {
            namespace = "test"
            compileSdk = 34
            flavorDimensions.add("abc")
            productFlavors {
                create("staging") {
                    dimension = "abc"
                }
                create("uat") {
                    dimension = "abc"
                }
            }
        }

        // When
        QualityMetric(contextResolver).apply(project)
        (project as DefaultProject).evaluate()

        // Then
        assertEquals(
            actual = extension.jacocoVersion.get(),
            expected = BuildConfig.jacocoVersion,
        )
        verify(exactly = 1) { variantsConfiguration.createVariant("custom", any()) }
        verify(exactly = 1) { variantConfiguration.addWithDependencies("stagingDebug") }
        confirmVerified(
            variantsConfiguration,
            variantConfiguration,
        )
    }

    @Test
    fun `When apply is called it applies it configures AndroidLibrary for Kover`() {
        fakeProject.plugins.apply(BuildConfig.androidLibraryId)
        val extension: LibraryExtension = fakeProject.extensions.getByType(LibraryExtension::class.java)

        // When
        QualityMetric().apply(fakeProject)

        // Then
        assertEquals(
            actual = extension.testCoverage.jacocoVersion,
            expected = BuildConfig.jacocoVersion,
        )
        extension.buildTypes.forEach { type ->
            if (type.name != "release") {
                assertTrue(type.enableAndroidTestCoverage, "InstrumentedCoverage was not enabled for ${type.name}")
            }
        }

        extension.buildTypes.getByName("release").apply {
            assertFalse(enableAndroidTestCoverage, "InstrumentedCoverage was enabled for $name")
        }
    }

    @Test
    fun `When apply is called it applies the KoverPlugin for JavaLibraries`() {
        // Given
        fakeProject.plugins.apply(BuildConfig.javaLibraryId)
        fakeProject.plugins.apply(BuildConfig.koverId)

        val contextResolver: PlatformContextResolver = mockk {
            every { getType(any()) } returns setOf(PlatformContext.JVM_LIBRARY)
        }
        val pluginContainer: PluginContainer = mockk(relaxed = true)
        val project: Project = spyk(fakeProject) {
            every { plugins } returns pluginContainer
        }

        // When
        QualityMetric(contextResolver).apply(project)

        // Then
        verify(exactly = 1) { pluginContainer.apply(BuildConfig.koverId) }
    }

    @Test
    fun `When apply is called it applies it configures Kover for JavaLibraries`() {
        // Given
        fakeProject.plugins.apply(BuildConfig.javaLibraryId)
        fakeProject.plugins.apply(BuildConfig.koverId)

        val contextResolver: PlatformContextResolver = mockk {
            every { getType(any()) } returns setOf(PlatformContext.JVM_LIBRARY)
        }
        val variantsConfiguration: KoverCurrentProjectVariantsConfig = mockk(relaxed = true)
        val extension: KoverProjectExtension = spyk(fakeProject.extensions.getByType(KoverProjectExtension::class.java))
        val pluginContainer: PluginContainer = mockk(relaxed = true)
        val project: Project = spyk(fakeProject) {
            every { plugins } returns pluginContainer
            every { extensions } returns spyk(fakeProject.extensions) {
                every { getByType(KoverProjectExtension::class.java) } returns extension
            }
        }

        invokeGradleAction(
            variantsConfiguration,
        ) { probe ->
            extension.currentProject(probe)
        }

        // When
        QualityMetric(contextResolver).apply(project)
        (project as DefaultProject).evaluate()

        // Then
        assertEquals(
            actual = extension.jacocoVersion.get(),
            expected = BuildConfig.jacocoVersion,
        )

        verify(exactly = 0) { variantsConfiguration.createVariant("custom", any()) }
    }

    @Test
    fun `When apply is called it applies the KoverPlugin for the ProjectRoot`() {
        // Given
        val contextResolver: PlatformContextResolver = mockk {
            every { getType(any()) } returns setOf(PlatformContext.ANDROID_LIBRARY)
        }
        fakeRootProject.plugins.apply(BuildConfig.koverId)
        val pluginContainer: PluginContainer = mockk(relaxed = true)
        val project: Project = spyk(fakeRootProject) {
            every { plugins } returns pluginContainer
            every { rootProject } returns this
        }

        // When
        QualityMetric(contextResolver).apply(project)

        // Then
        verify(exactly = 1) { pluginContainer.apply(BuildConfig.koverId) }
    }

    @Test
    fun `When apply is called it applies it configures Kover for the ProjectRoot`() {
        // Given
        val dependencyHandler: DependencyHandler = mockk(relaxed = true)
        val subProject: Project = spyk(fakeProject)
        val project: Project = spyk(fakeRootProject) {
            every { subprojects } returns setOf(subProject)
            every { dependencies } returns dependencyHandler
            every { rootProject } returns this
            every { projectDir } returns rootProjectDir
        }

        // When
        QualityMetric().apply(project)
        (project as DefaultProject).evaluate()

        // Then
        val prefix = if (File("/private/$rootProjectDir").exists()) {
            "/private/"
        } else {
            ""
        }
        val extension: KoverProjectExtension = project.extensions.getByType(KoverProjectExtension::class.java)
        assertEquals(
            actual = extension.jacocoVersion.get(),
            expected = BuildConfig.jacocoVersion,
        )
        extension.reports {
            total {
                xml {
                    assertFalse(onCheck.get())
                    assertEquals(
                        actual = xmlFile.get().asFile,
                        expected = File("$prefix$rootProjectDir", "build/reports/kover/result.xml"),
                    )
                }

                html {
                    assertFalse(onCheck.get())
                    assertEquals(
                        actual = htmlDir.get().asFile,
                        expected = File("$prefix$rootProjectDir", "build/reports/kover/report"),
                    )
                }
            }
        }
    }

    @Test
    fun `When apply is called it applies it configures Kover for the ProjectRoots subprojects but skips subprojects without a build configuration`() {
        // Given
        val dependencyHandler: DependencyHandler = mockk(relaxed = true)
        val koverExtension: KoverProjectExtension = mockk(relaxed = true)
        val extensionContainer: ExtensionContainer = mockk {
            every { getByType(KoverProjectExtension::class.java) } returns koverExtension
        }
        val subProject: Project = spyk(fakeProject)
        val project: Project = mockk(relaxed = true) {
            every { extensions } returns extensionContainer
            every { subprojects } returns setOf(subProject)
            every { dependencies } returns dependencyHandler
            every { rootProject } returns this
            every { projectDir } returns rootProjectDir
        }
        invokeGradleAction(
            project,
            project,
        ) {
            project.afterEvaluate(it)
        }

        // When
        QualityMetric().apply(project)

        // Then
        verify(exactly = 0) { dependencyHandler.add("kover", subProject) }
    }

    @Test
    fun `When apply is called it applies it configures Kover for the ProjectRoots subprojects`() {
        // Given
        val dependencyHandler: DependencyHandler = mockk(relaxed = true)
        val koverExtension: KoverProjectExtension = mockk(relaxed = true)
        val extensionContainer: ExtensionContainer = mockk {
            every { getByType(KoverProjectExtension::class.java) } returns koverExtension
        }

        File(subProjectDir, "build.gradle.kts").apply {
            createNewFile()
            writeText("HELO")
        }

        val subProject: Project = spyk(fakeProject)
        val project: Project = mockk(relaxed = true) {
            every { extensions } returns extensionContainer
            every { subprojects } returns setOf(subProject)
            every { dependencies } returns dependencyHandler
            every { rootProject } returns this
            every { projectDir } returns rootProjectDir
        }

        invokeGradleAction(
            project,
            project,
        ) {
            project.afterEvaluate(it)
        }

        // When
        QualityMetric().apply(project)

        // Then
        verify(exactly = 1) { dependencyHandler.add("kover", subProject) }
    }

    @Test
    fun `When apply is called it applies it configures Kover for the ProjectRoots while ignorign subprojects with koverignore`() {
        // Given
        val dependencyHandler: DependencyHandler = mockk(relaxed = true)
        val koverExtension: KoverProjectExtension = mockk(relaxed = true)
        val extensionContainer: ExtensionContainer = mockk {
            every { getByType(KoverProjectExtension::class.java) } returns koverExtension
        }

        File(subProjectDir, "build.gradle.kts").apply {
            createNewFile()
            writeText("HELO")
        }
        File(subProjectDir, ".koverignore").apply {
            createNewFile()
            writeText("HELO")
        }

        val subProject: Project = spyk(fakeProject)
        val project: Project = mockk(relaxed = true) {
            every { extensions } returns extensionContainer
            every { subprojects } returns setOf(subProject)
            every { dependencies } returns dependencyHandler
            every { rootProject } returns this
            every { projectDir } returns rootProjectDir
        }

        invokeGradleAction(
            project,
            project,
        ) {
            project.afterEvaluate(it)
        }

        // When
        QualityMetric().apply(project)

        // Then
        verify(exactly = 0) { dependencyHandler.add("kover", subProject) }
    }

    @Test
    fun `It fulfils Plugin`() {
        assertTrue {
            @Suppress("USELESS_IS_CHECK")
            QualityMetric() is Plugin<*>
        }
    }
}
