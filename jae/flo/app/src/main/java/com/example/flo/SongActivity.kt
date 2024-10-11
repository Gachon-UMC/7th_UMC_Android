package com.example.flo

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    lateinit var binding: ActivitySongBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("title") && intent.hasExtra("singer")) {
            binding.songMusicTitleTv.text = intent.getStringExtra("title")
            binding.songSingerNameTv.text = intent.getStringExtra("singer")
        }

        binding.songDownIb.setOnClickListener {
            finish()
        }

        binding.songMiniplayerPlayIv.setOnClickListener {
            if (binding.songMiniplayerPlayIv.drawable.constantState == resources.getDrawable(R.drawable.btn_miniplayer_play).constantState) {
                binding.songMiniplayerPlayIv.setImageResource(R.drawable.btn_miniplay_pause)
            } else {
                binding.songMiniplayerPlayIv.setImageResource(R.drawable.btn_miniplayer_play)
            }
        }
    }
}
