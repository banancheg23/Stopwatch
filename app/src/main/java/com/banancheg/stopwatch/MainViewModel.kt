package com.banancheg.stopwatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class MainViewModel(
    private val coroutineScope: CoroutineScope,
    private val stopwatchListOrchestrator: StopwatchListOrchestrator
) : ViewModel() {

    private val liveDataMap: HashMap<Int, MutableLiveData<String>> = HashMap()

    fun subscribe(stopwatchId: Int): LiveData<String>? {
        return liveDataMap[stopwatchId]
    }

    fun start(stopwatchId: Int) {
        stopwatchListOrchestrator.start(stopwatchId)
    }

    fun pause(stopwatchId: Int) {
        stopwatchListOrchestrator.pause(stopwatchId)
    }

    fun stop(stopwatchId: Int) {
        stopwatchListOrchestrator.stop(stopwatchId)
    }

    fun startAll() {
        stopwatchListOrchestrator.startAll()
    }

    fun stopAll() {
        stopwatchListOrchestrator.stopAll()
    }

    fun pauseAll() {
        stopwatchListOrchestrator.pauseAll()
    }

    fun initStopwatches(count: Int): Set<Int> {
        return stopwatchListOrchestrator.initStopwatches(count).onEach { id ->
            liveDataMap[id] = MutableLiveData()

            coroutineScope.launch {
                stopwatchListOrchestrator.getStopwatchStateFlow(id)?.collect { stopwatchValue ->
                    liveDataMap[id]?.postValue(stopwatchValue)
                }
            }
        }
    }
}