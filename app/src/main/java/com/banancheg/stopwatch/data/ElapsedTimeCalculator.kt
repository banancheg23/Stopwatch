package com.banancheg.stopwatch.data

class ElapsedTimeCalculator(
    private val timestampProvider: TimestampProvider
) {
    fun calculate(state: StopwatchState.Started): Long {
        val currentTime = timestampProvider.getMilliseconds()
        val timePassedSinceStart = if (currentTime - state.startTime > 0) {
            currentTime - state.startTime
        } else {
            0
        }
        return state.elapsedTime + timePassedSinceStart
    }
}