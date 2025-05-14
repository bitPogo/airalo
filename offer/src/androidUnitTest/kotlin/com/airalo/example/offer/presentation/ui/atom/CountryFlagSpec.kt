package com.airalo.example.offer.presentation.ui.atom

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.onRoot
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil3.ColorImage
import coil3.test.FakeImageLoaderEngine
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import coil3.annotation.ExperimentalCoilApi
import com.airalo.example.offer.domain.entity.CountryFlagUri
import com.airalo.example.test.roborazzi.RoborazziTest
import com.airalo.example.test.roborazzi.TestApplication
import kotlin.text.isNotBlank
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], application = TestApplication::class)
class CountryFlagSpec : RoborazziTest() {
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
    fun `It renders a remote Flag`() {
        subjectUnderTest.setContent {
            CountryFlag(CountryFlagUri("https://example.com/image.jpg"))
        }

        subjectUnderTest.onNode(
            matcher = SemanticsMatcher("Empty ContentDescription") { node ->
                val contentDescription = node.config.getOrElse(SemanticsProperties.ContentDescription) { emptyList() }
                contentDescription.isNotEmpty()
            },
        ).assertDoesNotExist()
    }

    @Test
    fun `It renders a the flag of Flags if the remote resource is not accessible`() {
        subjectUnderTest.setContent {
            CountryFlag(CountryFlagUri("https://example.com/noimage.jpg"))
        }

        subjectUnderTest.onNode(
            matcher = SemanticsMatcher("Empty ContentDescription") { node ->
                val contentDescription = node.config.getOrElse(SemanticsProperties.ContentDescription) { emptyList() }
                contentDescription.isNotEmpty()
            },
        ).assertDoesNotExist()
    }
}
