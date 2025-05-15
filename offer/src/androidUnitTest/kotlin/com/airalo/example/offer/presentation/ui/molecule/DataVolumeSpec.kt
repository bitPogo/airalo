package com.airalo.example.offer.presentation.ui.molecule

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.airalo.example.offer.domain.entity.Volume
import com.airalo.example.test.roborazzi.RoborazziTest
import com.airalo.example.test.roborazzi.TestApplication
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], application = TestApplication::class)
class DataVolumeSpec : RoborazziTest() {
    @Test
    fun `It renders a DataVolume`() {
        // Arrange
        val volume = Volume("2 GB")

        // Act
        subjectUnderTest.setContent {
            DataVolume(volume)
        }

        // Assert
        subjectUnderTest.onNodeWithContentDescription("This package includes ${volume.volume} of data.").assertIsDisplayed()
    }
}
