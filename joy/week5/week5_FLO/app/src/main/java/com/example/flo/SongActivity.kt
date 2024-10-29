package com.example.flo

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity: AppCompatActivity() {

    lateinit var binding : ActivitySongBinding
    lateinit var song : Song
    lateinit var timer : Timer
    private var mediaPlayer: MediaPlayer? = null
    private var gson: Gson = Gson()
    private var isRepeat = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()
        setPlayer(song)

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



    private fun initSong() {
        if (intent.hasExtra("title") && intent.hasExtra("singer")) {
            song = Song(
                intent.getStringExtra("title") !!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second", 0)!!,
                intent.getIntExtra("playTime", 0)!!,
                intent.getBooleanExtra("isPlaying", false)!!,
                intent.getStringExtra("music")!!,
            )
        }
        startTimer()
    }

    private fun setPlayer(song : Song) {
        binding.songTitleTv.text = intent.getStringExtra("title")!!
        binding.songSingerTv.text = intent.getStringExtra("singer")!!
        binding.songStartTimeTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)

        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)
        song.playTime = mediaPlayer?.duration?.div(1000) ?: 0

        binding.songSeekbar.max = mediaPlayer?.duration ?: 0

        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)

        //binding.songSeekbar.progress = (song.second * 1000 / song.playTime)
        binding.songSeekbar.progress = song.second

        //mediaPlayer?.seekTo(song.second * 1000)



        setPlayerStatus(song.isPlaying)
    }

    fun setPlayerStatus(isPlaying: Boolean) {
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if (isPlaying) {
            if (timer.state == Thread.State.TERMINATED) {
                startTimer()
            }
            binding.nuguBtnPlayIv.visibility = View.INVISIBLE
            binding.nuguBtnPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        } else {
            binding.nuguBtnPlayIv.visibility = View.VISIBLE
            binding.nuguBtnPauseIv.visibility = View.INVISIBLE

            if(mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
        //song.second = ((binding.songSeekbar.progress * song.playTime)/100)/1000
        song.second = mediaPlayer?.currentPosition?.div(1000) ?: 0

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit() //에디터
        val songJson = gson.toJson(song)
        editor.putString("songData", songJson)
        editor.apply()

        Log.d("SongActivity", "Saved song data: $songJson")
    }

//    override fun onResume() {
//        super.onResume()
//        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
//        val songJson = sharedPreferences.getString("songData", null)
//
//        if (songJson != null) {
//            song = gson.fromJson(songJson, Song::class.java)
//            setPlayer(song)  // 이전 상태로 설정
//
//            // MediaPlayer를 이전 재생 위치로 이동
//            mediaPlayer?.seekTo(song.second * 100000)
//
//            // 이전 재생 상태가 true라면 재생 시작
//            if (song.isPlaying) {
//                setPlayerStatus(true)
//            }
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() //미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null //미디어 플레이어 해제
    }

    inner class Timer(private val playTime : Int, var isPlaying : Boolean = true):Thread() {
        private var second : Int = 0
        //private var milis : Float = 0f
        private var milis: Float = (song.second * 1000).toFloat()  // 시작 위치 설정

        override fun run() {
            super.run()
            try {
                while (true) {
                    if (second >= playTime) {
                        runOnUiThread {
                            setPlayerStatus(false) // 음악 멈추기

                            if (isRepeat) {
                                resetPlayer()      // 반복 재생 시 재시작
                                setPlayerStatus(true)
                            } else {
                                resetPlayer()      // 반복 재생이 아닐 때 0으로 초기화
                            }
                        }
                        break
                    }
                    if (isPlaying) {
                        sleep(10) // 10 밀리초 대기
                        milis += 10 // 밀리초 증가

                        runOnUiThread {
                            //binding.songSeekbar.progress = ((milis / (playTime) * 1000) * 100).toInt()
                            binding.songSeekbar.progress = mediaPlayer?.currentPosition ?: 0  // SeekBar 진행 업데이트
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

    private fun startTimer() {
        timer = Timer(song.playTime, song.isPlaying)
        timer.start()
    }

    private fun resetPlayer() {
        song.second = 0
        binding.songSeekbar.progress = 0
        binding.songStartTimeTv.text = "00:00"
        mediaPlayer?.seekTo(0)
    }

    fun setRepeatStatus(isRepeat: Boolean) {
        this.isRepeat = isRepeat
        if (isRepeat) {
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