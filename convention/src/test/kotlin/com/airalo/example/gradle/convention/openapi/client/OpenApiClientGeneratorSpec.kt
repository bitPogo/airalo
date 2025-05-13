package com.airalo.example.gradle.convention.openapi.client

import com.airalo.example.gradle.config.BuildConfig
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import java.io.File
import java.util.Optional
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.internal.project.DefaultProject
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompileCommon
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OpenApiClientGeneratorSpec {
    private val fakeProject: Project = ProjectBuilder
        .builder()
        .build()
    private val project = spyk(fakeProject)
    private val dependencyProvider: Optional<Provider<ExternalModuleDependencyBundle>> = mockk(relaxed = true)
    private val extensionContainer: ExtensionContainer = spyk(project.extensions)
    private val kmpAndroidConfiguration: Plugin<Project> = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        clearMocks(
            project,
            kmpAndroidConfiguration,
        )

        every {
            project.extensions
        } returns extensionContainer

        every {
            extensionContainer.getByType(VersionCatalogsExtension::class.java).named("dependencyCatalog")
        } returns mockk<VersionCatalog> {
            every { findBundle("convention-openapi") } returns dependencyProvider
        }
    }

    @Test
    fun `When apply is called it applies the KMP Android Configuration`() {
        // Given
        every {
            extensionContainer.getByType(KotlinMultiplatformExtension::class.java)
        } returns mockk(relaxed = true) {
            every { sourceSets.getByName(any()) } returns mockk(relaxed = true)
        }

        // When
        OpenApiClientGenerator(kmpAndroidConfiguration).apply(project)

        // Then
        verify(exactly = 1) {
            kmpAndroidConfiguration.apply(project)
        }
    }

    @Test
    fun `When apply is called it registers and wires tasks to generate the Openapi Client part with the payload of the extension`() {
        // Given
        val spec = "spec"
        val name = "random"

        every {
            extensionContainer.getByType(KotlinMultiplatformExtension::class.java)
        } returns mockk(relaxed = true) {
            every { sourceSets.getByName(any()) } returns mockk(relaxed = true)
        }

        // When
        OpenApiClientGenerator(kmpAndroidConfiguration).apply(project)
        val extension = extensionContainer.getByType(OpenApiClientExtension::class.java)

        extension.apiContracts.add(spec)
        extension.packageName.set(name)

        project.afterEvaluate {
            // Then
            val generatorTask = project.tasks.findByName("openApiGenerate")
            assertTrue(generatorTask is OpenApiClientTask, "It does not contain the Api Client Task")
            assertEquals(
                actual = generatorTask.group,
                expected = "OpenAPI Tools",
            )
            assertEquals(
                actual = generatorTask.description,
                expected = "Generate infrastructure code via Open API Tools Generator for Open API 2.0 or 3.x specification documents.",
            )
            assertEquals(
                actual = generatorTask.packageName.get(),
                expected = name,
            )
            assertEquals(
                actual = generatorTask.apiContracts.get(),
                expected = listOf(File(spec)),
            )
        }
        (project as DefaultProject).evaluate()
    }

    @Test
    fun `When apply is called it adds the Openapi convention dependency bundle`() {
        // Given
        val dependencyHandler: KotlinDependencyHandler = mockk(relaxed = true)
        val executor = slot<KotlinDependencyHandler.() -> Unit>()
        val kmpExtension: KotlinMultiplatformExtension = mockk {
            every { sourceSets.getByName(any()) } returns mockk(relaxed = true)

            every { sourceSets.getByName("commonMain") } returns mockk(relaxed = true) {
                every { dependencies(capture(executor)) } answers {
                    executor.captured.invoke(dependencyHandler)
                }
            }
        }
        val dependencyScope: DependencyHandlerScope = mockk {
            every { add(any(), any()) } returns mockk()
        }
        every {
            extensionContainer.getByType(KotlinMultiplatformExtension::class.java)
        } returns kmpExtension

        every { project.plugins } returns mockk(relaxed = true)
        every { project.dependencies } returns dependencyScope

        // When
        OpenApiClientGenerator(kmpAndroidConfiguration).apply(project)

        // Then
        verify(exactly = 1) {
            dependencyHandler.implementation(dependencyProvider.get())
        }
    }

    @Test
    fun `When apply is called it adds the Openapi Infrastructure project if had been already invoked`() {
        // Given
        val name = "openapi-infrastructure"
        val path = "path"
        val infrastructureProject: Project = mockk {
            every { this@mockk.name } returns name
            every { this@mockk.path } returns path
        }
        val root = spyk(fakeProject) {
            every { subprojects } returns setOf(this@OpenApiClientGeneratorSpec.project, infrastructureProject)
        }

        every { project.rootProject } returns root

        val infrastructureDependency: ProjectDependency = mockk()
        val dependencyHandler: KotlinDependencyHandler = mockk(relaxed = true) {
            every { project(any<String>()) } returns infrastructureDependency
        }
        val executor = slot<KotlinDependencyHandler.() -> Unit>()
        val kmpExtension: KotlinMultiplatformExtension = mockk {
            every { sourceSets.getByName(any()) } returns mockk(relaxed = true)

            every { sourceSets.getByName("commonMain") } returns mockk(relaxed = true) {
                every { dependencies(capture(executor)) } answers {
                    executor.captured.invoke(dependencyHandler)
                }
            }
        }
        val dependencyScope: DependencyHandlerScope = mockk {
            every { add(any(), any()) } returns mockk()
        }
        every {
            extensionContainer.getByType(KotlinMultiplatformExtension::class.java)
        } returns kmpExtension

        every { project.plugins } returns mockk(relaxed = true)
        every { project.dependencies } returns dependencyScope

        // When
        OpenApiClientGenerator(kmpAndroidConfiguration).apply(project)

        // Then
        verify(exactly = 1) {
            dependencyHandler.project(path)
        }
        verify(exactly = 1) {
            dependencyHandler.implementation(dependencyProvider.get())
            dependencyHandler.implementation(infrastructureDependency)
        }
    }

    @Test
    fun `When apply is called it adds the generated openapi folder to the sources`() {
        // Given
        val sources: SourceDirectorySet = mockk(relaxed = true)
        val kmpExtension: KotlinMultiplatformExtension = mockk {
            every { sourceSets.getByName(any()) } returns mockk(relaxed = true)

            every { sourceSets.getByName("commonMain") } returns mockk(relaxed = true) {
                every { kotlin } returns sources
            }
        }
        every {
            extensionContainer.getByType(KotlinMultiplatformExtension::class.java)
        } returns kmpExtension

        every { project.plugins } returns mockk(relaxed = true)

        // When
        OpenApiClientGenerator(kmpAndroidConfiguration).apply(project)

        // Then
        verify(exactly = 1) {
            sources.srcDir("${fakeProject.projectDir.absolutePath}/build/generated/openapi/src/commonMain/kotlin")
        }
    }

    @Test
    fun `When apply is called it sets a dependency to the openApiInfrastructureGenerate tasks for all compilations`() {
        // Given
        val spec = "spec"
        val name = "random"

        project.plugins.apply(BuildConfig.kmpId)

        extensionContainer.getByType(KotlinMultiplatformExtension::class.java).apply {
            jvm()
            iosArm64()
        }

        every {
            extensionContainer.getByType(KotlinMultiplatformExtension::class.java)
        } returns mockk(relaxed = true) {
            every { sourceSets.getByName(any()) } returns mockk(relaxed = true)
        }

        // When
        OpenApiClientGenerator(kmpAndroidConfiguration).apply(project)
        val extension = extensionContainer.getByType(OpenApiClientExtension::class.java)

        extension.apiContracts.add(spec)
        extension.packageName.set(name)

        // Then
        project.afterEvaluate {
            val clientTask = project.tasks.findByName("openApiGenerate")
            val commonCompilation = project.tasks.withType(KotlinCompileCommon::class.java).first()
            val jvmCompilation = project.tasks.withType(KotlinCompile::class.java).first()
            val nativeCompilation = project.tasks.withType(KotlinNativeCompile::class.java).first()
            assertTrue(
                actual = commonCompilation.dependsOn.toList().contains(clientTask),
                message = "Expected commonCompilation to depend on clientTask",
            )
            assertTrue(
                actual = commonCompilation.mustRunAfter.getDependencies(clientTask).toList().contains(clientTask),
                message = "Expected commonCompilation to run after clientTask",
            )
            assertTrue(
                actual = jvmCompilation.dependsOn.toList().contains(clientTask),
                message = "Expected jvmCompilation to depend on clientTask",
            )
            assertTrue(
                actual = jvmCompilation.mustRunAfter.getDependencies(clientTask).toList().contains(clientTask),
                message = "Expected jvmCompilation to run after clientTask",
            )
            assertTrue(
                actual = nativeCompilation.dependsOn.toList().contains(clientTask),
                message = "Expected nativeCompilation to depend on clientTask",
            )
            assertTrue(
                actual = nativeCompilation.mustRunAfter.getDependencies(clientTask).toList().contains(clientTask),
                message = "Expected nativeCompilation to run after clientTask",
            )
        }

        (project as DefaultProject).evaluate()
    }

    @Test
    fun `It fulfils Plugin`() {
        assertTrue {
            @Suppress("USELESS_IS_CHECK")
            OpenApiClientGenerator(kmpAndroidConfiguration) is Plugin<*>
        }
    }
}
