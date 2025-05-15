package com.airalo.example.offer.presentation.ui.molecule

import androidx.compose.runtime.Composable
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.airalo.example.offer.domain.entity.Price
import com.airalo.example.test.roborazzi.RoborazziTest
import com.airalo.example.test.roborazzi.TestApplication
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], application = TestApplication::class)
class PurchaseButtonSpec : RoborazziTest() {
    @Test
    fun `It renders a PurchaseButton`() {
        subjectUnderTest.setContent {
            PurchaseButton(Price(9.99))
        }
    }
}
