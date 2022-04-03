package com.banancheg.stopwatch.data

sealed class StopwatchState {
    data class Paused(
        val elapsedTime: Long
    ) : StopwatchState()

    data class Started(
        val startTime: Long,
        val elapsedTime: Long
    ) : StopwatchState()
}
