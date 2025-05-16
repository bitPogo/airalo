package com.airalo.example.offer.data.repository

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler

class DispatcherSpy(
    scheduler: TestCoroutineScheduler,
) : CoroutineDispatcher() {
    private val dispatcher = StandardTestDispatcher(scheduler)
    private var _hadBeenCalled = false
    val hadBeenCalled: Boolean
        get() = _hadBeenCalled

    override fun dispatch(
        context: CoroutineContext,
        block: Runnable,
    ) = dispatcher.dispatch(context, block).also {
        _hadBeenCalled = true
    }
}
