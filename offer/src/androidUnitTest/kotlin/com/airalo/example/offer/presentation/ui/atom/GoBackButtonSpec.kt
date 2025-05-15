package com.airalo.example.offer.presentation.ui.atom

import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.airalo.example.test.roborazzi.RoborazziTest
import com.airalo.example.test.roborazzi.TestApplication
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import tech.antibytes.util.test.mustBe

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], application = TestApplication::class)
class GoBackButtonSpec : RoborazziTest() {
    @Test
    fun `It renders a PurchaseButton`() {
        // Arrange
        var wasCalled = false

        // Act
        subjectUnderTest.setContent {
            GoBackButton {
                wasCalled = true
            }
        }

        subjectUnderTest.onNodeWithContentDescription("go back").performClick()

        // Assert
        wasCalled mustBe true
    }
}
