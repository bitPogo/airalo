package com.airalo.example.test.roborazzi

import androidx.compose.material3.Text
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], application = TestApplication::class)
class RoborazziSpec : RoborazziTest() {
    @Test
    fun `It renders an Compose Component`() {
        subjectUnderTest.setContent {
            Text("Test")
        }
    }
}
