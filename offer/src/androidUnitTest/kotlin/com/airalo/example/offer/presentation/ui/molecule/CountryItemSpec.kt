package com.airalo.example.offer.presentation.ui.molecule

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil3.ColorImage
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import coil3.annotation.ExperimentalCoilApi
import coil3.test.FakeImageLoaderEngine
import com.airalo.example.offer.domain.entity.Country
import com.airalo.example.offer.domain.entity.CountryFlagUri
import com.airalo.example.offer.domain.entity.Id
import com.airalo.example.test.roborazzi.RoborazziTest
import com.airalo.example.test.roborazzi.TestApplication
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import tech.antibytes.util.test.mustBe

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], application = TestApplication::class)
class CountryItemSpec : RoborazziTest() {
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    @OptIn(ExperimentalCoilApi::class, DelicateCoilApi::class)
    fun setup() {
        val engine = FakeImageLoaderEngine.Builder()
            .intercept("https://example.com/image.jpg", ColorImage(Color.Blue.toArgb()))
            .build()
        val imageLoader = ImageLoader.Builder(context)
            .components { add(engine) }
            .build()

        SingletonImageLoader.setUnsafe(imageLoader)
    }

    @Test
    fun `It renders a CountryItem with the Country name as ContentDescription, which is clickable`() {
        // Arrange
        val countryName = "Germany"
        var wasClicked = false

        // Act
        subjectUnderTest.setContent {
            CountryItem(
                country = Country(
                    id = Id(1),
                    name = "Germany",
                    flag = CountryFlagUri("https://example.com/image.jpg"),
                ),
            ) {
                wasClicked = true
            }
        }

        subjectUnderTest.onNodeWithContentDescription(countryName).performClick()

        // Then
        wasClicked mustBe true
    }
}
