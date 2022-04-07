package com.banancheg.stopwatch

import org.koin.java.KoinJavaComponent.inject
import kotlin.random.Random

class StopwatchListOrchestrator() {

    private val stopwatchStateHolderMap: HashMap<Int, StopwatchStateHolder> = HashMap()

    fun initStopwatches(count: Int): Set<Int> {
        repeat(count) {
            val stopwatchStateHolder: StopwatchStateHolder by inject(StopwatchStateHolder::class.java)
            stopwatchStateHolderMap[Random.nextInt(0, 999999)] = stopwatchStateHolder
        }

        return stopwatchStateHolderMap.keys
    }

    fun start(stopwatchId: Int) {
        stopwatchStateHolderMap[stopwatchId]?.start()
    }

    fun pause(stopwatchId: Int) {
        stopwatchStateHolderMap[stopwatchId]?.pause()
    }

    fun stop(stopwatchId: Int) {
        stopwatchStateHolderMap[stopwatchId]?.stop()
    }

    fun startAll() {
        stopwatchStateHolderMap.values.onEach { it.start() }
    }

    fun stopAll() {
        stopwatchStateHolderMap.values.onEach { it.stop() }
    }

    fun pauseAll() {
        stopwatchStateHolderMap.values.onEach { it.pause() }
    }

    fun getStopwatchStateFlow(stopwatchId: Int) = stopwatchStateHolderMap[stopwatchId]?.ticker
}