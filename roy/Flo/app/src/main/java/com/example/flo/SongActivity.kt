package com.example.flo

import android.annotation.SuppressLint
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {
    lateinit var binding: ActivitySongBinding
    lateinit var song: Song
    lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null
    private var gson: Gson = Gson()
    private var isRepeatActive = false
    private var currentSecond: Int = 0 // 현재 재생 시간을 저장할 변수


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // SharedPreferences에서 데이터 불러오기
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val savedCurrentSecond = sharedPreferences.getInt("currentSecond", 0)
        val savedSongData = sharedPreferences.getString("songData", null)

        song = if (savedSongData != null) {
            gson.fromJson(savedSongData, Song::class.java).apply {
                second = savedCurrentSecond // 저장된 재생 시간 설정
            }
        } else {
            // 인텐트로부터 기본값 설정
            Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second", 0),
                intent.getIntExtra("playTime", 0),
                intent.getBooleanExtra("isPlaying", false),
                intent.getStringExtra("music")!!
            )
        }






        initSong()
        setPlayer(song)


        binding.songDownIb.setOnClickListener {
            finish()
        }

        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.songRepeatIv.setOnClickListener {
            if (isRepeatActive) {
                binding.songRepeatIv.clearColorFilter()
            } else {
                binding.songRepeatIv.setColorFilter(Color.parseColor("#3f3fff"))
            }
            isRepeatActive = !isRepeatActive
        }

        var isMixActive = false
        binding.songRandomIv.setOnClickListener {
            if (isMixActive) {
                binding.songRandomIv.clearColorFilter()
            } else {
                binding.songRandomIv.setColorFilter(Color.parseColor("#3f3fff"))
            }
            isMixActive = !isMixActive
        }
    }

    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)

        // 현재 재생 시간 저장
        currentSecond = mediaPlayer?.currentPosition?.div(1000) ?: 0 // 밀리초를 초로 변환
        song.second = currentSecond // song 객체에 저장


        song.second = (song.playTime * binding.songProgressSb.progress) / 100000

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val songToJson = gson.toJson(song)
        editor.putInt("currentSecond", currentSecond)
        editor.putString("songData", songToJson)
        editor.apply()

    }

    override fun onResume() {
        super.onResume()
        // SharedPreferences에서 데이터 불러오기
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val savedCurrentSecond = sharedPreferences.getInt("currentSecond", 0)

        // 저장된 재생 시간이 있을 경우 해당 시간부터 음악을 재생
        if (savedCurrentSecond > 0) {
            mediaPlayer?.seekTo(savedCurrentSecond * 1000) // 밀리초 단위로 이동
            setPlayerStatus(true) // 음악 재생

            if (!timer.isAlive) {
                startTimer() // 타이머가 종료된 경우 새로 시작
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }


    private fun initSong() {
        if (intent.hasExtra("title") && intent.hasExtra("singer")) {
            song = Song(
                intent.getStringExtra(("title"))!!,
                intent.getStringExtra(("singer"))!!,
                intent.getIntExtra("second", 0),
                intent.getIntExtra("playTime", 0),
                intent.getBooleanExtra("isPlaying", false),
                intent.getStringExtra("music")!!
            )
        }
        startTimer()
    }

    private fun setPlayer(song: Song) {

        binding.songMusicTitleTv.text = intent.getStringExtra("title")
        binding.songSingerNameTv.text = intent.getStringExtra("singer")
        binding.songStartTimeTv.text =
            String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTimeTv.text =
            String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songProgressSb.progress = (song.second * 1000 / song.playTime)


        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)


        mediaPlayer?.setOnCompletionListener {
            if (isRepeatActive) {
                mediaPlayer?.seekTo(0)
                mediaPlayer?.start()
                resetTimerAndSeekBar()
            } else {
                setPlayerStatus(false)
                resetTimerAndSeekBar()
            }
        }

        setPlayerStatus(song.isPlaying)

        // 재생 버튼 클릭 이벤트 처리
        binding.songMiniplayerIv.setOnClickListener {
            if (!song.isPlaying) {
                mediaPlayer?.seekTo(0)
                setPlayerStatus(true)
                resetTimerAndSeekBar()
            }
        }
    }

    private fun resetTimerAndSeekBar() {
        binding.songProgressSb.progress = 0 // SeekBar 초기화
        timer.interrupt() // 기존 타이머 중지
        startTimer() // 새 타이머 시작
    }

    fun setPlayerStatus(isPlaying: Boolean) {
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if (isPlaying) {
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()

        } else {
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE

            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        }
    }

    private fun startTimer() {
        timer = Timer(song.playTime, song.isPlaying)
        timer.start()
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true) : Thread() {

        private var second: Int = 0
        private var mills: Float = 0f

        override fun run() {
            super.run()
            try {
                while (true) {
                    if (second >= playTime) {
                        break
                    }
                    if (isPlaying) {
                        sleep(50)
                        mills += 50
                        runOnUiThread {
                            binding.songProgressSb.progress = ((mills / playTime) * 100).toInt()
                        }
                        if (mills % 1000 == 0f) {
                            runOnUiThread {
                                binding.songStartTimeTv.text =
                                    String.format("%02d:%02d", second / 60, second % 60)
                            }
                            second++
                        }
                    }
                }
            } catch (e: InterruptedException) {
                Log.d("Song", "쓰레드가 죽었습니다. ${e.message}")
            }
        }
    }
}
