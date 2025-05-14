package com.airalo.example.test.roborazzi

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.dropbox.differ.SimpleImageComparator
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.InternalRoborazziApi
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.provideRoborazziContext

// see: https://github.com/bitPogo/keather/blob/45cc973d016f5b8c08906121f321a19353b74a75/android/src/test/kotlin/tech/antibytes/keather/android/app/RoborazziRule.kt

@OptIn(ExperimentalRoborazziApi::class, InternalRoborazziApi::class)
fun roborazziOf(
    szenario: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>,
    captureType: RoborazziRule.CaptureType = RoborazziRule.CaptureType.None,
): RoborazziRule {
    return RoborazziRule(
        composeRule = szenario,
        captureRoot = szenario.onRoot(),
        options = RoborazziRule.Options(
            outputDirectoryPath = "${provideRoborazziContext().outputDirectory}/../../../snapshots/android/images",
            captureType = captureType,
            roborazziOptions = RoborazziOptions(
                compareOptions = RoborazziOptions.CompareOptions(
                    imageComparator = SimpleImageComparator(maxDistance = 0.007F, hShift = 1),
                ),
            ),
        ),
    )
}
