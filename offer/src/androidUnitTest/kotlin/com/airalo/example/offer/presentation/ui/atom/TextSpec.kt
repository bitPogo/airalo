package com.airalo.example.offer.presentation.ui.atom

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
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
class TextSpec : RoborazziTest() {
    @Test
    fun `It renders a CountryName`() {
        subjectUnderTest.setContent {
            CountryName("Test!")
        }

        subjectUnderTest.onNode(
            matcher = SemanticsMatcher("Empty ContentDescription") { node ->
                val contentDescription = node.config.getOrElse(SemanticsProperties.ContentDescription) { emptyList() }
                contentDescription.isNotEmpty()
            },
        ).assertDoesNotExist()
    }

    @Test
    fun `It renders an OfferDetailLabel`() {
        // Arrange
        val contentDescription = "No"

        // Act
        subjectUnderTest.setContent {
            OfferDetailLabel("Yes!", contentDescription)
        }

        // Assert
        subjectUnderTest.onNodeWithContentDescription(contentDescription).assertIsDisplayed()
    }

    @Test
    fun `It renders an OfferDetailValue`() {
        // Arrange
        val contentDescription = "No"

        // Act
        subjectUnderTest.setContent {
            OfferDetailValue("Yes!", contentDescription)
        }

        // Assert
        subjectUnderTest.onNodeWithContentDescription(contentDescription).assertIsDisplayed()
    }

    @Test
    fun `It renders an OfferDetailButtonLabel`() {
        // Arrange
        val contentDescription = "No"

        // Act
        subjectUnderTest.setContent {
            OfferDetailButtonLabel("Yes!", contentDescription)
        }

        // Assert
        subjectUnderTest.onNodeWithContentDescription(contentDescription).assertIsDisplayed()
    }

    @Test
    fun `It renders a SectionTitle`() {
        // Arrange
        val contentDescription = "No"

        // Act
        subjectUnderTest.setContent {
            SectionTitle("Yes!", contentDescription)
        }

        // Assert
        subjectUnderTest.onNodeWithContentDescription(contentDescription).assertIsDisplayed()
    }

    @Test
    fun `It renders a ScreenTitle`() {
        // Act
        subjectUnderTest.setContent {
            ScreenTitle("Yes!")
        }
    }
}
