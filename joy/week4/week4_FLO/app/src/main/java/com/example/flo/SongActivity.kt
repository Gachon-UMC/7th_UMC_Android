package com.example.flo

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.WindowInsetsControllerCompat
import com.example.flo.databinding.ActivitySongBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SongActivity: AppCompatActivity() {

    lateinit var binding : ActivitySongBinding
    lateinit var song : Song
    lateinit var timer : Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()
        setPlayer(song)

//        if (intent.hasExtra("title") && intent.hasExtra("singer")) {
//            binding.songTitleTv.text = intent.getStringExtra("title")!!
//            binding.songSingerTv.text = intent.getStringExtra("singer")!!
//        }

        binding.nuguBtnDownIv.setOnClickListener {
            finish()
            Toast.makeText(this, intent.getStringExtra("title"), Toast.LENGTH_SHORT).show()
        }

        binding.nuguBtnPlayIv.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.nuguBtnPauseIv.setOnClickListener {
            setPlayerStatus(false)
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
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
    }

    private fun initSong() {
        if (intent.hasExtra("title") && intent.hasExtra("singer")) {
            song = Song(
                intent.getStringExtra("title") !!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second", 0)!!,
                //intent.getIntExtra("playTime", 0)!!,
                15, //15초로 임시 변경
                intent.getBooleanExtra("isPlaying", false)!!,
            )
        }
        startTimer()
    }

    private fun setPlayer(song : Song) {
        binding.songTitleTv.text = intent.getStringExtra("title")!!
        binding.songSingerTv.text = intent.getStringExtra("singer")!!
        binding.songStartTimeTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songSeekbar.progress = (song.second * 1000 / song.playTime)

        setPlayerStatus(song.isPlaying)
    }

    inner class Timer(private val playTime : Int, var isPlaying : Boolean = true):Thread() {
        private var second : Int = 0
        private var milis : Float = 0f

        override fun run() {
            super.run()
            try {
                while (true) {
                    if (second >= playTime) {
                        runOnUiThread {
                            setPlayerStatus(false) // 음악 멈추기
                        }
                        break
                    }
                    if (isPlaying) {
                        sleep(10) // 10 밀리초 대기
                        milis += 10 // 밀리초 증가

                        runOnUiThread {
                            binding.songSeekbar.progress = ((milis / playTime)*60).toInt() // SeekBar 진행률 업데이트
                        }

                        if (milis % 1000 == 0f) { // 1초마다
                            runOnUiThread {
                                binding.songStartTimeTv.text = String.format("%02d:%02d", second / 60, second % 60) // 시간 표시 업데이트
                            }
                            second ++ // 초 증가
                        }
                    }
                }
            } catch(e : InterruptedException) {
                Log.d("Song", "스레드가 죽었습니다. ${e.message}")
            }
        }
    }

    fun setPlayerStatus(isPlaying: Boolean) {
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if (isPlaying) {
            if (timer.state == Thread.State.TERMINATED) {
                // 타이머가 종료된 상태면 타이머를 다시 시작
                startTimer()
            }
            binding.nuguBtnPlayIv.visibility = View.INVISIBLE
            binding.nuguBtnPauseIv.visibility = View.VISIBLE
        } else {
            binding.nuguBtnPlayIv.visibility = View.VISIBLE
            binding.nuguBtnPauseIv.visibility = View.INVISIBLE
        }
    }

    private fun startTimer() {
        timer = Timer(song.playTime, song.isPlaying)
        timer.start()
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