package com.banancheg.stopwatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class MainViewModel(
    private val coroutineScope: CoroutineScope,
    private val stopwatchListOrchestrator: StopwatchListOrchestrator
) : ViewModel() {

    init {
        coroutineScope.launch {
            stopwatchListOrchestrator.ticker.collect {
                liveData.postValue(it)
            }
        }
    }

    private val liveData: MutableLiveData<String> = MutableLiveData()

    fun subscribe(): LiveData<String> {
        return liveData
    }

    fun start() {
        stopwatchListOrchestrator.start()
    }

    fun pause() {
        stopwatchListOrchestrator.pause()
    }

    fun stop() {
        stopwatchListOrchestrator.stop()
    }
}