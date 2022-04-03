package com.banancheg.stopwatch.data

interface TimestampProvider {
    fun getMilliseconds(): Long
}