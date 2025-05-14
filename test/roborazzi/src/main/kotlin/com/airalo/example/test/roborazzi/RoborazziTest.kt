package com.airalo.example.test.roborazzi

import android.content.ContentProvider
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.After
import org.junit.Rule
import org.robolectric.Robolectric

// see: https://github.com/bitPogo/keather/blob/45cc973d016f5b8c08906121f321a19353b74a75/android/src/test/kotlin/tech/antibytes/keather/android/app/RoborazziTest.kt

abstract class RoborazziTest(
    captureType: RoborazziRule.CaptureType = RoborazziRule.CaptureType.None,
) {
    @get:Rule
    val subjectUnderTest = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val roborazziRule = roborazziOf(subjectUnderTest, captureType)

    private fun setupAndroidContextProvider() {
        try {
            @Suppress("UNCHECKED_CAST")
            val clazz = Class.forName("org.jetbrains.compose.resources.AndroidContextProvider") as Class<ContentProvider>
            Robolectric.setupContentProvider(clazz)
        } catch (e: Throwable) {
            // do nothing
        }
    }

    init {
        setupAndroidContextProvider()
    }

    @After
    fun capture() {
        subjectUnderTest.onRoot().captureRoboImage()
        subjectUnderTest.activityRule.scenario.recreate()
    }
}
