package com.example.flo

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {

    lateinit var binding: ActivitySongBinding
    lateinit var song : Song
    lateinit var timer : Timer
    private var mediaPlayer : MediaPlayer? = null
    private var gson : Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.songRepeatOnIv.visibility = View.GONE
        initSong()
        setPlayer(song)
        initClickListener()
        }

    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)

        song.second = (song.playTime * binding.songProgressSb.progress) / 100000
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val songToJson = gson.toJson(song)
        editor.putString("songData", songToJson)
        Log.d("songData", songToJson.toString())
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun initClickListener() {
        binding.songDownIb.setOnClickListener {
            Toast.makeText(this, intent.getStringExtra("title"), Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.songMiniplayerPlayIv.setOnClickListener {
            song.isPlaying = !song.isPlaying
            setPlayerStatus(song.isPlaying)
        }

        binding.songMiniplayerPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.songRepeatIv.setOnClickListener {
            val isRepeatOn = binding.songRepeatOnIv.visibility == View.VISIBLE
            setRepeatStatus(!isRepeatOn)
        }

        binding.songRepeatOnIv.setOnClickListener {
            setRepeatStatus(false)
        }

        binding.songRandomIv.setOnClickListener {
            val isRandomOn = binding.songRandomOnIv.visibility == View.VISIBLE
            setRandomStatus(!isRandomOn)
        }

        binding.songRandomOnIv.setOnClickListener {
            setRandomStatus(false)
        }
    }

    private fun initSong(){
        if (intent.hasExtra("title") && intent.hasExtra("singer")) {
            song = Song (
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second", 0),
                intent.getIntExtra("playTime", 0),
                intent.getBooleanExtra("isPlaying", false),
                intent.getStringExtra("music")!!
            )
        }
        startTimer()
    }

    private fun setPlayer(song: Song){
        binding.songMusicTitleTv.text = song.title
        binding.songSingerNameTv.text = song.singer
        binding.songStartTimeTv.text = String.format("%02d:%02d",song.second / 60, song.second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d",song.playTime / 60, song.playTime % 60)
        binding.songProgressSb.progress = (song.second * 1000 / song.playTime)
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)
        setPlayerStatus(song.isPlaying)
    }

    private fun setRepeatStatus(isRepeatOn: Boolean) {
        if (isRepeatOn) {
            binding.songRepeatIv.visibility = View.GONE
            binding.songRepeatOnIv.visibility = View.VISIBLE
        } else {
            binding.songRepeatIv.visibility = View.VISIBLE
            binding.songRepeatOnIv.visibility = View.GONE
        }
    }

    private fun setRandomStatus(isRandomOn: Boolean) {
        if (isRandomOn) {
            binding.songRandomIv.visibility = View.GONE
            binding.songRandomOnIv.visibility = View.VISIBLE
        } else {
            binding.songRandomIv.visibility = View.VISIBLE
            binding.songRandomOnIv.visibility = View.GONE
        }
    }

    private fun setPlayerStatus(isPlaying: Boolean) {
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if (isPlaying) {
            binding.songMiniplayerPlayIv.visibility = View.GONE
            binding.songMiniplayerPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
            startTimer()
        }
        else {
            binding.songMiniplayerPlayIv.visibility = View.VISIBLE
            binding.songMiniplayerPauseIv.visibility = View.GONE
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
                song.second = mediaPlayer?.currentPosition?.div(1000) ?: song.second
            }
        }
    }

    private fun startTimer() {
        if (::timer.isInitialized && timer.isAlive) {
            timer.isPlaying = song.isPlaying
        } else {
            timer = Timer(song.playTime, song.isPlaying)
            timer.start()
        }
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true) : Thread() {
        private var mills: Float = (song.second * 1000).toFloat()  // 현재 재생 위치에서 시작
        private var second: Int = song.second

        override fun run() {
            super.run()
            while (true) {
                if (!isPlaying) {
                    sleep(50)
                    continue
                }

                if (second >= playTime) {  // 곡이 끝난 경우
                    runOnUiThread {
                        mediaPlayer?.pause()
                        mediaPlayer?.seekTo(0)
                        binding.songProgressSb.progress = 0
                        binding.songStartTimeTv.text = String.format("%02d:%02d", 0, 0)

                        // 반복 재생 확인
                        if (binding.songRepeatOnIv.visibility == View.VISIBLE) {
                            setPlayerStatus(true)  // 반복 재생
                        } else {
                            setPlayerStatus(false)  // 재생 중지
                        }
                    }
                    break
                }

                sleep(50)
                mills += 50

                runOnUiThread {
                    binding.songProgressSb.progress = ((mills / (playTime * 1000)) * 10000).toInt()
                    binding.songStartTimeTv.text = String.format("%02d:%02d", second / 60, second % 60)
                }


                if (mills % 1000 == 0f) {
                    second++
                }
            }
        }
    }

}
