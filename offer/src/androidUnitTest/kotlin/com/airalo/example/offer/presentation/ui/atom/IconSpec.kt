package com.airalo.example.offer.presentation.ui.atom

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.airalo.example.test.roborazzi.RoborazziTest
import com.airalo.example.test.roborazzi.TestApplication
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], application = TestApplication::class)
class IconSpec : RoborazziTest() {
    @Test
    fun `It renders a Trailing Arrow`() {
        subjectUnderTest.setContent {
            TrailingArrow()
        }

        subjectUnderTest.onNode(
            matcher = SemanticsMatcher("Empty ContentDescription") { node ->
                val contentDescription = node.config.getOrElse(SemanticsProperties.ContentDescription) { emptyList() }
                contentDescription.isNotEmpty()
            },
        ).assertDoesNotExist()
    }
}
