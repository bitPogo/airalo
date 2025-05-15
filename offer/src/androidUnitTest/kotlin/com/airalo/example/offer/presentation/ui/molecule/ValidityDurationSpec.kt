package com.airalo.example.offer.presentation.ui.molecule

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.airalo.example.offer.domain.entity.Validity
import com.airalo.example.test.roborazzi.RoborazziTest
import com.airalo.example.test.roborazzi.TestApplication
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], application = TestApplication::class)
class ValidityDurationSpec : RoborazziTest() {
    @Test
    fun `It renders a ValidityDuration`() {
        // Arrange
        val validity = Validity("5 days")

        // Act
        subjectUnderTest.setContent {
            ValidityDuration(validity)
        }

        // Assert
        subjectUnderTest.onNodeWithContentDescription("This package can be used for ${validity.validity}.").assertIsDisplayed()
    }
}
