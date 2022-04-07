package com.banancheg.stopwatch

class TimestampProvider : ITimestampProvider {
    override fun getMilliseconds(): Long {
        return System.currentTimeMillis()
    }
}