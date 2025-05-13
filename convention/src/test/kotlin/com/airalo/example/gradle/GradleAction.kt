@file:Suppress("ktlint:standard:filename")

// Taken from: https://github.com/bitPogo/gradle-plugins/tree/main/antibytes-gradle-test-utils

package com.airalo.example.gradle

import io.mockk.MockKMatcherScope
import io.mockk.every
import io.mockk.slot
import org.gradle.api.Action

/**
 * Captures a given Gradle Action Call
 * @param (Action<T>) -> T: Closure, which contains the Gradle Action Call which is meant to be execute
 * @param T: Object which is wrapped by the Action
 * @param T: return value of the Gradle Action
 */
inline fun <T : Any, reified R> invokeGradleAction(
    probe: T,
    returnValue: R,
    crossinline caller: MockKMatcherScope.(Action<T>) -> R,
) {
    val action = slot<Action<T>>()
    every {
        caller(capture(action))
    } answers {
        action.captured.execute(probe)
        returnValue
    }
}

/**
 * Captures a given Gradle Action Call
 * @param (Action<T>) -> T: Closure, which contains the Gradle Action Call which is meant to be execute
 * @param T: Object which is wrapped by the Action
 */
inline fun <T : Any> invokeGradleAction(
    probe: T,
    crossinline caller: MockKMatcherScope.(Action<T>) -> Unit,
) {
    val action = slot<Action<T>>()
    every {
        caller(capture(action))
    } answers {
        action.captured.execute(probe)
    }
}
