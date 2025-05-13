package com.airalo.example.gradle.convention.quality.staticanalysis

import com.airalo.example.gradle.config.BuildConfig
import com.airalo.example.gradle.invokeGradleAction
import com.appmattus.kotlinfixture.kotlinFixture
import com.diffplug.gradle.spotless.BaseKotlinExtension
import com.diffplug.gradle.spotless.FormatExtension
import com.diffplug.gradle.spotless.KotlinExtension
import com.diffplug.gradle.spotless.KotlinGradleExtension
import com.diffplug.gradle.spotless.SpotlessExtension
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.PluginContainer
import org.junit.jupiter.api.Test

class SpotlessSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `When apply is called it applies the spotless gradle plugin`() {
        // Given
        val pluginContainer: PluginContainer = mockk(relaxed = true)
        val spotlessExtension: SpotlessExtension = mockk(relaxed = true)
        val extensionContainer: ExtensionContainer = mockk {
            every { getByType(SpotlessExtension::class.java) } returns spotlessExtension
        }
        val project: Project = mockk(relaxed = true) {
            every { plugins } returns pluginContainer
            every { extensions } returns extensionContainer
        }

        // When
        Spotless.apply(project)

        // Then
        verify(exactly = 1) {
            pluginContainer.apply(BuildConfig.spotlessId)
        }
    }

    @Test
    fun `When apply is called it ensures all plugins feed from the gradlePluginPortal`() {
        // Given
        val repositoryHandler: RepositoryHandler = mockk(relaxed = true)
        val extensionContainer: ExtensionContainer = mockk {
            every { getByType(SpotlessExtension::class.java) } returns mockk(relaxed = true)
        }
        val project: Project = mockk(relaxed = true) {
            every { extensions } returns extensionContainer
            every { repositories } returns repositoryHandler
        }

        invokeGradleAction(
            project,
        ) { probe ->
            project.allprojects(probe)
        }

        // When
        Spotless.apply(project)

        // Then
        verify(exactly = 1) {
            repositoryHandler.gradlePluginPortal()
        }
    }

    @Test
    fun `When apply is called it configures KTLint for Kotlin Sources`() {
        // Given
        val rootProjectDir: File = fixture()
        val spotlessExtension: SpotlessExtension = mockk(relaxed = true)
        val ktlintBase: BaseKotlinExtension.KtlintConfig = mockk(relaxed = true)
        val kotlinExtension = KotlinExtensionFake(spotlessExtension, ktlintBase)
        val extensionContainer: ExtensionContainer = mockk {
            every { getByType(SpotlessExtension::class.java) } returns spotlessExtension
        }
        val project: Project = mockk(relaxed = true) {
            every { extensions } returns extensionContainer
            every { rootDir } returns rootProjectDir
        }

        invokeGradleAction(
            kotlinExtension,
            kotlinExtension,
        ) { probe ->
            @Suppress("UNCHECKED_CAST")
            spotlessExtension.kotlin(probe as Action<KotlinExtension>)
        }

        // When
        Spotless.apply(project)

        // Then
        assertEquals(
            actual = kotlinExtension.version,
            expected = BuildConfig.ktlintVersion,
        )
        assertEquals(
            actual = kotlinExtension.targetArguments.toList(),
            expected = listOf("**/*.kt"),
        )
        assertEquals(
            actual = kotlinExtension.excluded.toList(),
            expected = listOf(
                "buildSrc/build/",
                "**/buildSrc/build/",
                "**/src/test/resources/**/*.kt",
                "**/build/**/*.kt",
            ),
        )
        assertTrue { kotlinExtension.useEndWithNewline }
        assertTrue { kotlinExtension.useIndentWithSpaces }
        assertTrue { kotlinExtension.useTrimTrailingWhitespace }
        verify(exactly = 1) {
            ktlintBase.setEditorConfigPath(File(rootProjectDir, ".editorconfig"))
        }
    }

    @Test
    fun `When apply is called it configures KTLint for Kotlin Scripts`() {
        // Given
        val rootProjectDir: File = fixture()
        val spotlessExtension: SpotlessExtension = mockk(relaxed = true)
        val ktlintBase: BaseKotlinExtension.KtlintConfig = mockk(relaxed = true)
        val kotlinExtension = KotlinGradleExtensionFake(spotlessExtension, ktlintBase)
        val extensionContainer: ExtensionContainer = mockk {
            every { getByType(SpotlessExtension::class.java) } returns spotlessExtension
        }
        val project: Project = mockk(relaxed = true) {
            every { extensions } returns extensionContainer
            every { rootDir } returns rootProjectDir
        }

        invokeGradleAction(
            kotlinExtension,
            kotlinExtension,
        ) { probe ->
            @Suppress("UNCHECKED_CAST")
            spotlessExtension.kotlinGradle(probe as Action<KotlinGradleExtension>)
        }

        // When
        Spotless.apply(project)

        // Then
        assertEquals(
            actual = kotlinExtension.version,
            expected = BuildConfig.ktlintVersion,
        )
        assertEquals(
            actual = kotlinExtension.targetArguments.toList(),
            expected = listOf("**/*.gradle.kts"),
        )
        assertEquals(
            actual = kotlinExtension.excluded.toList(),
            expected = listOf("**/build/**/*"),
        )
        assertTrue { kotlinExtension.useEndWithNewline }
        assertTrue { kotlinExtension.useIndentWithSpaces }
        assertTrue { kotlinExtension.useTrimTrailingWhitespace }
        verify(exactly = 1) {
            ktlintBase.setEditorConfigPath(File(rootProjectDir, ".editorconfig"))
        }
    }

    @Test
    fun `When apply is called it configures formatting for miscallious files`() {
        // Given
        val rootProjectDir: File = fixture()
        val spotlessExtension: SpotlessExtension = mockk(relaxed = true)
        val formatExtension = FormatExtensionFake(spotlessExtension)
        val extensionContainer: ExtensionContainer = mockk {
            every { getByType(SpotlessExtension::class.java) } returns spotlessExtension
        }
        val project: Project = mockk(relaxed = true) {
            every { extensions } returns extensionContainer
            every { rootDir } returns rootProjectDir
        }

        invokeGradleAction(
            formatExtension,
            formatExtension,
        ) { probe ->
            @Suppress("UNCHECKED_CAST")
            spotlessExtension.format("misc", probe as Action<FormatExtension>)
        }

        // When
        Spotless.apply(project)

        // Then
        assertEquals(
            actual = formatExtension.targetArguments.toList(),
            expected = listOf("**/*.md", "**/.gitignore"),
        )
        assertTrue { formatExtension.useEndWithNewline }
        assertTrue { formatExtension.useIndentWithSpaces }
        assertTrue { formatExtension.useTrimTrailingWhitespace }
    }

    @Test
    fun `It fulfils the StaticAnalysisConventionContract`() {
        assertTrue {
            @Suppress("USELESS_IS_CHECK")
            Spotless is StaticAnalysisConventionContract
        }
    }
}

// see: https://github.com/bitPogo/gradle-plugins/blob/main/antibytes-quality/src/test/kotlin/tech/antibytes/gradle/quality/linter/SpotlessSpec.kt
private class KotlinExtensionFake(
    spotlessExtension: SpotlessExtension,
    private val formatExtension: BaseKotlinExtension.KtlintConfig,
) : KotlinExtension(spotlessExtension) {
    var targetArguments: Array<out Any?> = emptyArray()
    var excluded: Array<out Any?> = emptyArray()
    var version: String? = null
    var useTrimTrailingWhitespace = false
    var useIndentWithSpaces = false
    var useEndWithNewline = false

    override fun target(vararg targets: Any?) {
        targetArguments = targets
    }

    override fun targetExclude(vararg targets: Any?) {
        excluded = targets
    }

    override fun ktlint(version: String?): KtlintConfig {
        this.version = version

        return formatExtension
    }

    override fun trimTrailingWhitespace() {
        useTrimTrailingWhitespace = true
    }

    override fun indentWithSpaces() {
        useIndentWithSpaces = true
    }

    override fun endWithNewline() {
        useEndWithNewline = true
    }
}

private class KotlinGradleExtensionFake(
    spotlessExtension: SpotlessExtension,
    private val formatExtension: BaseKotlinExtension.KtlintConfig,
) : KotlinGradleExtension(spotlessExtension) {
    var targetArguments: Array<out Any?> = emptyArray()
    var excluded: Array<out Any?> = emptyArray()
    var version: String? = null
    var useTrimTrailingWhitespace = false
    var useIndentWithSpaces = false
    var useEndWithNewline = false

    override fun target(vararg targets: Any?) {
        targetArguments = targets
    }

    override fun targetExclude(vararg targets: Any?) {
        excluded = targets
    }

    override fun ktlint(version: String?): KtlintConfig {
        this.version = version

        return formatExtension
    }

    override fun trimTrailingWhitespace() {
        useTrimTrailingWhitespace = true
    }

    override fun indentWithSpaces() {
        useIndentWithSpaces = true
    }

    override fun endWithNewline() {
        useEndWithNewline = true
    }
}

private class FormatExtensionFake(spotlessExtension: SpotlessExtension) : FormatExtension(spotlessExtension) {
    var targetArguments: Array<out Any?> = emptyArray()
    var excluded: Array<out Any?> = emptyArray()
    var useTrimTrailingWhitespace = false
    var useIndentWithSpaces = false
    var useEndWithNewline = false

    override fun target(vararg targets: Any?) {
        targetArguments = targets
    }

    override fun targetExclude(vararg targets: Any?) {
        excluded = targets
    }

    override fun trimTrailingWhitespace() {
        useTrimTrailingWhitespace = true
    }

    override fun indentWithSpaces() {
        useIndentWithSpaces = true
    }

    override fun endWithNewline() {
        useEndWithNewline = true
    }
}
