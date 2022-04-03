package com.banancheg.stopwatch

import com.banancheg.stopwatch.data.StopwatchStateHolder
import com.banancheg.stopwatch.data.TimestampMillisecondsFormatter
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StopwatchListOrchestrator(
    private val stopwatchStateHolder: StopwatchStateHolder,
    private val scope: CoroutineScope
) {

    private var job: Job? = null
    private val mutableTicker = MutableStateFlow(TimestampMillisecondsFormatter.DEFAULT_TIME)
    val ticker: StateFlow<String> = mutableTicker

    fun start() {
        if (job == null) startJob()
        stopwatchStateHolder.start()
    }

    fun pause() {
        stopwatchStateHolder.pause()
        stopJob()
    }

    fun stop() {
        stopwatchStateHolder.stop()
        stopJob()
        clearValue()
    }

    private fun startJob() {
        job = scope.launch {
            while (isActive) {
                mutableTicker.value = stopwatchStateHolder.getStringTimeRepresentation()
                delay(TICK_DELAY)
            }
        }
    }

    private fun stopJob() {
        scope.coroutineContext.cancelChildren()
        job = null
    }

    fun clearValue() {
        mutableTicker.value = TimestampMillisecondsFormatter.DEFAULT_TIME
    }

    companion object {
        private const val TICK_DELAY: Long = 20
    }
}