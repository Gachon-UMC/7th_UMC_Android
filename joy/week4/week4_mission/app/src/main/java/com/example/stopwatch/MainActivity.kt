package com.example.stopwatch

import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import com.example.stopwatch.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private lateinit var NowTime: TextView
    private lateinit var StartPauseBtn: TextView
    private lateinit var ClearBtn: TextView

    private var stopwatchJob: Job? = null
    private var elapsedTime = 0L // 경과 시간 (밀리초)
    private var isRunning = false
    private var lastStartTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NowTime = binding.swNowTime
        StartPauseBtn = binding.swStartPauseBtn
        ClearBtn = binding.swClearBtn

        StartPauseBtn.setOnClickListener {
            if (!isRunning) {
                startStopWatch()
                StartPauseBtn.text = "Pause"
            } else {
                pauseStopWatch()
                StartPauseBtn.text = "Start"
            }
        }

        ClearBtn.setOnClickListener {
            clearStopWatch()
        }
    }

    private fun startStopWatch() {
        isRunning = true
        lastStartTime = SystemClock.elapsedRealtime() - elapsedTime

        stopwatchJob = lifecycleScope.launch {
            while (isRunning) {
                elapsedTime = SystemClock.elapsedRealtime() - lastStartTime
                NowTime.text = formatTime(elapsedTime)
                delay(50)
            }
        }
    }

    private fun pauseStopWatch() {
        isRunning = false
        stopwatchJob?.cancel()
    }

    private fun clearStopWatch() {
        if (isRunning) {
            pauseStopWatch()
            StartPauseBtn.text = "Start"
        }
        elapsedTime = 0L
        NowTime.text = formatTime(elapsedTime)
    }

    private fun formatTime(timeInMillis: Long): String {
        val totalSeconds = timeInMillis / 1000
        val minutes = (totalSeconds / 60).toInt()
        val seconds = (totalSeconds % 60).toInt()
        val milliseconds = ((timeInMillis % 1000) / 10).toInt()
        return String.format("%02d:%02d:%02d", minutes, seconds, milliseconds)
    }
}