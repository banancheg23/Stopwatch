package com.banancheg.stopwatch

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StopwatchStateHolder(
    private val stopwatchStateCalculator: StopwatchStateCalculator,
    private val elapsedTimeCalculator: ElapsedTimeCalculator,
    private val timestampMillisecondsFormatter: TimestampMillisecondsFormatter
) {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var job: Job? = null
    private val mutableTicker = MutableStateFlow(TimestampMillisecondsFormatter.DEFAULT_TIME)
    val ticker: StateFlow<String> = mutableTicker

    var currentState: StopwatchState = StopwatchState.Paused(0)
        private set

    fun start() {
        if (job == null) startJob()
        currentState = stopwatchStateCalculator.calculateRunningState(currentState)
    }

    fun pause() {
        stopJob()
        currentState = stopwatchStateCalculator.calculatePausedState(currentState)
    }

    fun stop() {
        stopJob()
        clearValue()
        currentState = StopwatchState.Paused(0)
    }

    private fun startJob() {
        scope.launch {
            while (isActive) {
                mutableTicker.value = getStringTimeRepresentation()
                delay(20)
            }
        }
    }

    private fun stopJob() {
        job?.cancel()
        job = null
    }

    private fun clearValue() {
        mutableTicker.value = TimestampMillisecondsFormatter.DEFAULT_TIME
    }

    private fun getStringTimeRepresentation(): String {
        val elapsedTime = when (val currentState = currentState) {
            is StopwatchState.Paused -> currentState.elapsedTime
            is StopwatchState.Running -> elapsedTimeCalculator.calculate(currentState)
        }
        return timestampMillisecondsFormatter.format(elapsedTime)
    }
}