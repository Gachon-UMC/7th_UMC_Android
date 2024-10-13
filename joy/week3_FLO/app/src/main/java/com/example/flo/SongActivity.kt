package com.example.flo

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.WindowInsetsControllerCompat
import com.example.flo.databinding.ActivitySongBinding

class SongActivity: AppCompatActivity() {

    lateinit var binding: ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nuguBtnDownIv.setOnClickListener {
            finish()
            Toast.makeText(this, intent.getStringExtra("title"), Toast.LENGTH_SHORT).show()
        }

        binding.nuguBtnPlayIv.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.nuguBtnPauseIv.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.nuguBtnRepeatActiveIv.setOnClickListener {
            setRepeatStatus(false)
        }

        binding.nuguBtnRepeatInactiveIv.setOnClickListener {
            setRepeatStatus(true)
        }

        binding.nuguBtnRandomActiveIv.setOnClickListener {
            setRandomStatus(false)
        }

        binding.nuguBtnRandomInactiveIv.setOnClickListener {
            setRandomStatus(true)
        }

        if (intent.hasExtra("title") && intent.hasExtra("singer")) {
            binding.songTitleTv.text = intent.getStringExtra("title")
            binding.songSingerTv.text = intent.getStringExtra("singer")
        }
    }

    fun setPlayerStatus(isPlaying: Boolean) {
        if (isPlaying) {
            binding.nuguBtnPlayIv.visibility = View.VISIBLE
            binding.nuguBtnPauseIv.visibility = View.INVISIBLE
        } else {
            binding.nuguBtnPlayIv.visibility = View.INVISIBLE
            binding.nuguBtnPauseIv.visibility = View.VISIBLE
        }
    }

    fun setRepeatStatus(isReapeat: Boolean) {
        if (isReapeat) {
            binding.nuguBtnRepeatActiveIv.visibility = View.VISIBLE
            binding.nuguBtnRepeatInactiveIv.visibility = View.INVISIBLE
        } else {
            binding.nuguBtnRepeatActiveIv.visibility = View.INVISIBLE
            binding.nuguBtnRepeatInactiveIv.visibility = View.VISIBLE
        }
    }

    fun setRandomStatus(isRandom: Boolean) {
        if (isRandom) {
            binding.nuguBtnRandomActiveIv.visibility = View.VISIBLE
            binding.nuguBtnRandomInactiveIv.visibility = View.INVISIBLE
        } else {
            binding.nuguBtnRandomActiveIv.visibility = View.INVISIBLE
            binding.nuguBtnRandomInactiveIv.visibility = View.VISIBLE
        }
    }
}