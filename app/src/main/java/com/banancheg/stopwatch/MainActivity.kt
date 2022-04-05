package com.banancheg.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.banancheg.stopwatch.data.*
import com.banancheg.stopwatch.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val timestampProvider = object : TimestampProvider {
        override fun getMilliseconds(): Long {
            return System.currentTimeMillis()
        }
    }

    private val stopwatchStateHolderList: MutableList<StopwatchStateHolder> = mutableListOf()
    private val stopwatchListOrchestrator = StopwatchListOrchestrator(stopwatchStateHolderList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repeat(STOPWATCH_COUNT) {
            stopwatchStateHolderList.add(
                StopwatchStateHolder(
                    StopwatchStateCalculator(
                        timestampProvider,
                        ElapsedTimeCalculator(timestampProvider)
                    ),
                    TimestampMillisecondsFormatter(),
                    ElapsedTimeCalculator(timestampProvider),
                )
            )
        }

        CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
            stopwatchStateHolderList[0].ticker.collect { timeString ->
                binding.time.text = timeString
            }
        }

        CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
            stopwatchStateHolderList[1].ticker.collect { timeString ->
                binding.time2.text = timeString
            }
        }

        binding.buttonStart.setOnClickListener {
            stopwatchListOrchestrator.start(stopwatchStateHolderList[0])
        }

        binding.buttonPause.setOnClickListener {
            stopwatchListOrchestrator.pause(stopwatchStateHolderList[0])
        }

        binding.buttonStop.setOnClickListener {
            stopwatchListOrchestrator.stop(stopwatchStateHolderList[0])
        }

        binding.buttonStart2.setOnClickListener {
            stopwatchListOrchestrator.start(stopwatchStateHolderList[1])
        }

        binding.buttonPause2.setOnClickListener {
            stopwatchListOrchestrator.pause(stopwatchStateHolderList[1])
        }

        binding.buttonStop2.setOnClickListener {
            stopwatchListOrchestrator.stop(stopwatchStateHolderList[1])
        }

        binding.buttonStartAll.setOnClickListener {
            stopwatchListOrchestrator.startAll()
        }

        binding.buttonPauseAll.setOnClickListener {
            stopwatchListOrchestrator.pauseAll()
        }

        binding.buttonStopAll.setOnClickListener {
            stopwatchListOrchestrator.stopAll()
        }
    }

    companion object {
        private const val STOPWATCH_COUNT = 2
    }
}