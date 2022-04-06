package com.banancheg.stopwatch

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StopwatchListOrchestrator(
    private val stopwatchStateHolderList: List<StopwatchStateHolder>,
    private val scope: CoroutineScope,
) {

    private var job: Job? = null
    private val mutableTicker = MutableStateFlow("")
    val ticker: StateFlow<String> = mutableTicker

    fun start() {
        if (job == null) startJob()
        stopwatchStateHolderList[0].start()
    }

    private fun startJob() {
        scope.launch {
            while (isThereRunningStopwatch()) {
                mutableTicker.value = stopwatchStateHolderList[0].getStringTimeRepresentation()
                delay(20)
            }
        }
    }

    fun pause() {
        stopwatchStateHolderList[0].pause()
        stopJob()
    }

    fun stop() {
        stopwatchStateHolderList[0].stop()
        stopJob()
        clearValue()
    }

    private fun stopJob() {
        job?.cancel()
        job = null
    }

    private fun clearValue() {
        mutableTicker.value = "00:00:000"
    }

    private fun isThereRunningStopwatch(): Boolean {
        stopwatchStateHolderList.forEach {
            if (it.currentState is StopwatchState.Running) return true
        }
        return false
    }
}