package com.airalo.example.offer.presentation.ui.atom

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
class OfferDetailsActionSpec : RoborazziTest() {
    @Test
    fun `It renders an OfferDetailsValueSlot`() {
        subjectUnderTest.setContent {
            Box(modifier = Modifier.border(1.dp, color = Color.Black)) {
                OfferDetailActionSlot { Text("Jo") }
            }
        }
    }
}
