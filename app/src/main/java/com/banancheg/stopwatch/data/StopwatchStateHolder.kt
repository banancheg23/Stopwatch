package com.banancheg.stopwatch.data

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StopwatchStateHolder(
    private val stopwatchStateCalculator: StopwatchStateCalculator,
    private val timestampMillisecondsFormatter: TimestampMillisecondsFormatter,
    private val elapsedTimeCalculator: ElapsedTimeCalculator,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
) {

    private var job: Job? = null
    private val mutableTicker = MutableStateFlow(TimestampMillisecondsFormatter.DEFAULT_TIME)
    val ticker: StateFlow<String> = mutableTicker

    private var currentState: StopwatchState = StopwatchState.Paused(0)

    fun start() {
        if (job == null) startJob()
        currentState = stopwatchStateCalculator.calculateRunningState(currentState)
    }

    fun pause() {
        startJob()
        currentState = stopwatchStateCalculator.calculatePausedState(currentState)
    }

    fun stop() {
        stopJob()
        clearValue()
        currentState = StopwatchState.Paused(0)
    }

    private fun startJob() {
        job = scope.launch {
            while (isActive) {
                mutableTicker.value = getStringTimeRepresentation()
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

    private fun getStringTimeRepresentation(): String {
        val elapsedTime = when (val currentState = currentState) {
            is StopwatchState.Paused -> currentState.elapsedTime
            is StopwatchState.Started -> elapsedTimeCalculator.calculate(currentState)
        }

        return timestampMillisecondsFormatter.format(elapsedTime)
    }


    companion object {
        private const val TICK_DELAY: Long = 20
    }
}