package com.banancheg.stopwatch

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.banancheg.stopwatch.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val timestampProvider = object : ITimestampProvider {
        override fun getMilliseconds(): Long {
            return System.currentTimeMillis()
        }
    }
    private val stopwatchListOrchestrator = StopwatchListOrchestrator(
        listOf(
            StopwatchStateHolder(
                StopwatchStateCalculator(
                    timestampProvider,
                    ElapsedTimeCalculator(timestampProvider)
                ),
                ElapsedTimeCalculator(timestampProvider),
                TimestampMillisecondsFormatter()
            ),
            StopwatchStateHolder(
                StopwatchStateCalculator(
                    timestampProvider,
                    ElapsedTimeCalculator(timestampProvider)
                ),
                ElapsedTimeCalculator(timestampProvider),
                TimestampMillisecondsFormatter()
            )
        ),
        coroutineScope
    )
    private val viewModel = MainViewModel(
        coroutineScope,
        stopwatchListOrchestrator
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.subscribe().observe(this) {
            binding.time.text = it
        }

        binding.buttonStart.setOnClickListener {
            viewModel.start()
        }
        binding.buttonPause.setOnClickListener {
            viewModel.pause()
        }
        binding.buttonStop.setOnClickListener {
            viewModel.stop()
        }
    }
}