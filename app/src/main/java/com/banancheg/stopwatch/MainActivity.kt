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

    private val stopwatchListOrchestrator = StopwatchListOrchestrator(
        StopwatchStateHolder(
            stopwatchStateCalculator = StopwatchStateCalculator(
                timestampProvider,
                ElapsedTimeCalculator(timestampProvider)
            ),
            timestampMillisecondsFormatter = TimestampMillisecondsFormatter(),
            elapsedTimeCalculator = ElapsedTimeCalculator(timestampProvider),
        ),
        CoroutineScope(Dispatchers.Default + SupervisorJob())
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
            stopwatchListOrchestrator.ticker.collect { timeString ->
                binding.time.text = timeString
            }
        }

        binding.buttonStart.setOnClickListener {
            stopwatchListOrchestrator.start()
        }

        binding.buttonPause.setOnClickListener {
            stopwatchListOrchestrator.pause()
        }

        binding.buttonStop.setOnClickListener {
            stopwatchListOrchestrator.stop()
        }
    }
}