package com.airalo.example.offer.presentation.ui.molecule

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.airalo.example.offer.presentation.ui.atom.SectionTitle
import com.airalo.example.test.roborazzi.RoborazziTest
import com.airalo.example.test.roborazzi.TestApplication
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], application = TestApplication::class)
class CountryListHeaderSpec: RoborazziTest() {
    @Test
    fun `It renders a CountryListHeader`() {
        // Act
        subjectUnderTest.setContent {
            CountryListHeader()
        }

        // Assert
        subjectUnderTest.onNodeWithContentDescription("Purchase a eSim for any of the following popular countries:").assertIsDisplayed()
    }
}
