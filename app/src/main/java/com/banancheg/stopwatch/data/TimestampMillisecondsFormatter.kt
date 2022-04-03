package com.banancheg.stopwatch.data

class TimestampMillisecondsFormatter() {
    fun format(timeMs: Long):String {
        val millisecondsFormatted = (timeMs % 1000).pad(3)
        val seconds = timeMs / 1000
        val secondsFormatted = (seconds % 60).pad(2)
        val minutes = seconds / 60
        val minutesFormatted = (minutes % 60).pad(2)
        val hours = minutes / 60
        return if (hours > 0) {
            val hoursFormatted = hours.pad(2)
            "$hoursFormatted:$minutesFormatted:$secondsFormatted:$millisecondsFormatted"
        } else {
            "$minutesFormatted:$secondsFormatted:$millisecondsFormatted"
        }
    }

    private fun Long.pad(desiredLength: Int) = this.toString().padStart(desiredLength, PAD_START_VALUE)

    companion object {
        const val DEFAULT_TIME = "00:00:000"
        const val PAD_START_VALUE = '0'
    }
}