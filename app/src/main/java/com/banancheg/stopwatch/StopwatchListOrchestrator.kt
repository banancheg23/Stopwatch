package com.banancheg.stopwatch

import com.banancheg.stopwatch.data.StopwatchStateHolder
import com.banancheg.stopwatch.data.TimestampMillisecondsFormatter
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StopwatchListOrchestrator(
    private val stopwatchStateHolderList: List<StopwatchStateHolder>
) {
    fun start(stateHolder: StopwatchStateHolder) {
        stateHolder.start()
    }

    fun pause(stateHolder: StopwatchStateHolder) {
        stateHolder.pause()
    }

    fun stop(stateHolder: StopwatchStateHolder) {
        stateHolder.stop()
    }

    fun startAll() {
        stopwatchStateHolderList.forEach { it.start() }
    }

    fun pauseAll() {
        stopwatchStateHolderList.forEach { it.pause() }
    }

    fun stopAll() {
        stopwatchStateHolderList.forEach { it.stop() }
    }
}