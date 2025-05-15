package com.airalo.example.offer.presentation.ui.organism

import android.content.Context
import androidx.compose.ui.graphics.toArgb
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil3.ColorImage
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import coil3.annotation.ExperimentalCoilApi
import coil3.test.FakeImageLoaderEngine
import com.airalo.example.offer.domain.entity.Offer
import com.airalo.example.offer.domain.entity.Operator
import com.airalo.example.offer.domain.entity.OperatorLogoUrl
import com.airalo.example.offer.domain.entity.Price
import com.airalo.example.offer.domain.entity.Validity
import com.airalo.example.offer.domain.entity.Volume
import com.airalo.example.test.roborazzi.RoborazziTest
import com.airalo.example.test.roborazzi.TestApplication
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], application = TestApplication::class)
class OfferListSpec : RoborazziTest() {
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    @OptIn(ExperimentalCoilApi::class, DelicateCoilApi::class)
    fun setup() {
        val engine = FakeImageLoaderEngine.Builder()
            .intercept("https://example.com/image.jpg", ColorImage(androidx.compose.ui.graphics.Color.Blue.toArgb()))
            .build()
        val imageLoader = ImageLoader.Builder(context)
            .components { add(engine) }
            .build()

        SingletonImageLoader.setUnsafe(imageLoader)
    }

    @Test
    fun `It renders a OfferList`() {
        // Arrange
        val offers = listOf(
            Offer(
                location = "United States",
                operator = Operator("Operator A", OperatorLogoUrl("https://cdn.airalo.com/images/d71dd812-9787-408e-a362-85346756762c.jpg")),
                price = Price(10.00),
                validity = Validity("7 Days"),
                volume = Volume("1 GB"),
            ),
            Offer(
                location = "Canada",
                operator = Operator("Operator B", OperatorLogoUrl("https://cdn.airalo.com/images/d71dd812-9787-408e-a362-85346756762c.jpg")),
                price = Price(15.00),
                validity = Validity("15 Days"),
                volume = Volume("3 GB"),
            ),
            Offer(
                location = "Mexico",
                operator = Operator("Operator C", OperatorLogoUrl("https://cdn.airalo.com/images/d71dd812-9787-408e-a362-85346756762c.jpg")),
                price = Price(20.00),
                validity = Validity("30 Days"),
                volume = Volume("5 GB"),
            ),
            Offer(
                location = "United States",
                operator = Operator("Operator A", OperatorLogoUrl("https://cdn.airalo.com/images/d71dd812-9787-408e-a362-85346756762c.jpg")),
                price = Price(5.00),
                validity = Validity("3 Days"),
                volume = Volume("500 MB"),
            ),
            Offer(
                location = "Canada",
                operator = Operator("Operator B", OperatorLogoUrl("https://cdn.airalo.com/images/d71dd812-9787-408e-a362-85346756762c.jpg")),
                price = Price(25.00),
                validity = Validity("30 Days"),
                volume = Volume("10 GB"),
            ),
            Offer(
                location = "Mexico",
                operator = Operator("Operator C", OperatorLogoUrl("https://cdn.airalo.com/images/d71dd812-9787-408e-a362-85346756762c.jpg")),
                price = Price(12.00),
                validity = Validity("7 Days"),
                volume = Volume("2 GB"),
            ),
            Offer(
                location = "United States",
                operator = Operator("Operator A", OperatorLogoUrl("https://cdn.airalo.com/images/d71dd812-9787-408e-a362-85346756762c.jpg")),
                price = Price(30.00),
                validity = Validity("60 Days"),
                volume = Volume("15 GB"),
            ),
            Offer(
                location = "Canada",
                operator = Operator("Operator B", OperatorLogoUrl("https://cdn.airalo.com/images/d71dd812-9787-408e-a362-85346756762c.jpg")),
                price = Price(8.00),
                validity = Validity("7 Days"),
                volume = Volume("1 GB"),
            ),
            Offer(
                location = "Mexico",
                operator = Operator("Operator C", OperatorLogoUrl("https://cdn.airalo.com/images/d71dd812-9787-408e-a362-85346756762c.jpg")),
                price = Price(18.00),
                validity = Validity("15 Days"),
                volume = Volume("4 GB"),
            ),
            Offer(
                location = "United States",
                operator = Operator("Operator A", OperatorLogoUrl("https://cdn.airalo.com/images/d71dd812-9787-408e-a362-85346756762c.jpg")),
                price = Price(40.00),
                validity = Validity("90 Days"),
                volume = Volume("20 GB"),
            ),
        )

        // Act
        subjectUnderTest.setContent {
            OfferList(offers)
        }
    }
}
