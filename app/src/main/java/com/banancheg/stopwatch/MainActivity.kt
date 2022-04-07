package com.banancheg.stopwatch

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.banancheg.stopwatch.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val stopwatchListOrchestrator = StopwatchListOrchestrator()
    private val viewModel = MainViewModel(
        coroutineScope,
        stopwatchListOrchestrator
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val stopwatchesIds = viewModel.initStopwatches(STOPWATCHES_COUNT).toList()

        viewModel.subscribe(stopwatchesIds[0])?.observe(this) {
            binding.time.text = it
        }

        viewModel.subscribe(stopwatchesIds[1])?.observe(this) {
            binding.time2.text = it
        }

        // First
        binding.buttonStart.setOnClickListener {
            viewModel.start(stopwatchesIds[0])
        }
        binding.buttonPause.setOnClickListener {
            viewModel.pause(stopwatchesIds[0])
        }
        binding.buttonStop.setOnClickListener {
            viewModel.stop(stopwatchesIds[0])
        }

        // Second
        binding.buttonStart2.setOnClickListener {
            viewModel.start(stopwatchesIds[1])
        }
        binding.buttonPause2.setOnClickListener {
            viewModel.pause(stopwatchesIds[1])
        }
        binding.buttonStop2.setOnClickListener {
            viewModel.stop(stopwatchesIds[1])
        }

        // All
        binding.buttonStartAll.setOnClickListener {
            viewModel.startAll()
        }
        binding.buttonPauseAll.setOnClickListener {
            viewModel.pauseAll()
        }
        binding.buttonStopAll.setOnClickListener {
            viewModel.stopAll()
        }
    }

    companion object {
        private const val STOPWATCHES_COUNT = 2
    }
}