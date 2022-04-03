package com.banancheg.stopwatch.data

class StopwatchStateHolder(
    private val stopwatchStateCalculator: StopwatchStateCalculator,
    private val timestampMillisecondsFormatter: TimestampMillisecondsFormatter,
    private val elapsedTimeCalculator: ElapsedTimeCalculator
) {

    private var currentState: StopwatchState = StopwatchState.Paused(0)

    fun start() {
        currentState = stopwatchStateCalculator.calculateRunningState(currentState)
    }

    fun pause() {
        currentState = stopwatchStateCalculator.calculatePausedState(currentState)
    }

    fun stop() {
        currentState = StopwatchState.Paused(0)
    }

    fun getStringTimeRepresentation(): String {
        val elapsedTime = when (val currentState = currentState) {
            is StopwatchState.Paused -> currentState.elapsedTime
            is StopwatchState.Started -> elapsedTimeCalculator.calculate(currentState)
        }

        return timestampMillisecondsFormatter.format(elapsedTime)
    }
}