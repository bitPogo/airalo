package com.airalo.example.offer.presentation.ui.atom

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
class SpaceSpec : RoborazziTest() {
    @Test
    fun `It renders SHorizontalSpace`() {
        subjectUnderTest.setContent {
            Box(
                modifier = Modifier.border(2.dp, Color.Black).height(20.dp),
            ) {
                SHorizontalSpace()
            }
        }
    }

    @Test
    fun `It renders XSVerticalSpace`() {
        subjectUnderTest.setContent {
            Box(
                modifier = Modifier.border(2.dp, Color.Black).width(20.dp),
            ) {
                XSVerticalSpace()
            }
        }
    }

    @Test
    fun `It renders MVerticalSpace`() {
        subjectUnderTest.setContent {
            Box(
                modifier = Modifier.border(2.dp, Color.Black).width(20.dp),
            ) {
                MVerticalSpace()
            }
        }
    }

    @Test
    fun `It renders XLVerticalSpace`() {
        subjectUnderTest.setContent {
            Box(
                modifier = Modifier.border(2.dp, Color.Black).width(20.dp),
            ) {
                XLVerticalSpace()
            }
        }
    }

    @Test
    fun `It renders XXLVerticalSpace`() {
        subjectUnderTest.setContent {
            Box(
                modifier = Modifier.border(2.dp, Color.Black).width(20.dp),
            ) {
                XXLVerticalSpace()
            }
        }
    }
}
