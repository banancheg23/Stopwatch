package com.banancheg.stopwatch.data

class StopwatchStateCalculator(
    private val timestampProvider: TimestampProvider,
    private val elapsedTimeCalculator: ElapsedTimeCalculator
) {
    fun calculateRunningState(oldState: StopwatchState) = when(oldState) {
        is StopwatchState.Started -> oldState
        is StopwatchState.Paused -> StopwatchState.Started(
            startTime = timestampProvider.getMilliseconds(),
            elapsedTime = oldState.elapsedTime
        )
    }

    fun calculatePausedState(oldState: StopwatchState) = when(oldState) {
        is StopwatchState.Paused -> oldState
        is StopwatchState.Started -> StopwatchState.Paused(
            elapsedTime = elapsedTimeCalculator.calculate(oldState)
        )
    }
}