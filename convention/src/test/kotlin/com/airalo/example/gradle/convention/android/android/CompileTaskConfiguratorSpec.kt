package com.airalo.example.gradle.convention.android.android

import com.airalo.example.gradle.convention.android.ConfigurationContract
import com.airalo.example.gradle.invokeGradleAction
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.gradle.api.Project
import org.gradle.api.tasks.TaskContainer
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.junit.jupiter.api.Test

class CompileTaskConfiguratorSpec {
    @Test
    fun `It fulfils Configurator`() {
        val configurator: Any = CompileTaskConfigurator

        assertTrue(configurator is ConfigurationContract.ParameterlessConfigurator)
    }

    @Test
    fun `When configure is called it ignores compile task for non Android`() {
        // Given
        val project: Project = mockk()
        val tasks: TaskContainer = mockk()
        val task: KotlinCompile = mockk()
        val lambdaExec = slot<KotlinJvmCompilerOptions.() -> Unit>()

        every { project.tasks } returns tasks
        every { task.name } returns "releaseKotlinJvm"
        every { task.compilerOptions(capture(lambdaExec)) } just Runs

        invokeGradleAction(task, mockk()) { action ->
            tasks.withType(KotlinCompile::class.java, action)
        }

        // When
        CompileTaskConfigurator.configure(project)

        // Then
        assertFalse(lambdaExec.isCaptured)
    }

    @Test
    fun `When configure is called it adjust the compile task for Android in KMP`() {
        // Given
        val project: Project = mockk()
        val tasks: TaskContainer = mockk()
        val task: KotlinCompile = mockk()
        val lambdaExec = slot<KotlinJvmCompilerOptions.() -> Unit>()
        val options: KotlinJvmCompilerOptions = mockk(relaxed = true)

        every { project.tasks } returns tasks
        every { task.name } returns "releaseKotlinAndroid"
        every { task.compilerOptions(capture(lambdaExec)) } just Runs

        invokeGradleAction(task, mockk()) { action ->
            tasks.withType(KotlinCompile::class.java, action)
        }

        // When
        CompileTaskConfigurator.configure(project)

        // Then
        lambdaExec.captured.invoke(options)

        verify(exactly = 1) { options.jvmTarget.set(JvmTarget.JVM_1_8) }
    }

    @Test
    fun `When configure is called it adjust the compile task for Android`() {
        // Given
        val project: Project = mockk()
        val tasks: TaskContainer = mockk()
        val task: KotlinCompile = mockk()
        val lambdaExec = slot<KotlinJvmCompilerOptions.() -> Unit>()
        val options: KotlinJvmCompilerOptions = mockk(relaxed = true)

        every { project.tasks } returns tasks
        every { task.name } returns "releaseAndroidKotlin"
        every { task.compilerOptions(capture(lambdaExec)) } just Runs

        invokeGradleAction(task, mockk()) { action ->
            tasks.withType(KotlinCompile::class.java, action)
        }

        // When
        CompileTaskConfigurator.configure(project)

        // Then
        lambdaExec.captured.invoke(options)

        verify(exactly = 1) { options.jvmTarget.set(JvmTarget.JVM_1_8) }
    }

    @Test
    fun `When configure is called it ignores non Android compile tasks`() {
        // Given
        val project: Project = mockk()
        val tasks: TaskContainer = mockk()
        val task: KotlinCompile = mockk()
        val lambdaExec = slot<KotlinJvmCompilerOptions.() -> Unit>()

        every { project.tasks } returns tasks
        every { task.name } returns "jvm"
        every { task.compilerOptions(capture(lambdaExec)) } just Runs

        invokeGradleAction(task, mockk()) { action ->
            tasks.withType(KotlinCompile::class.java, action)
        }

        // When
        CompileTaskConfigurator.configure(project)

        // Then
        assertFalse(lambdaExec.isCaptured)
    }
}
