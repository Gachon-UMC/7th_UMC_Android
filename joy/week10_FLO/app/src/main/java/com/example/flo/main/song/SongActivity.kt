package com.example.flo.main.song

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.flo.CustomSnackbar
import com.example.flo.R
import com.example.flo.data.db.SongDatabase
import com.example.flo.data.entities.Song
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity: AppCompatActivity() {

    lateinit var binding : ActivitySongBinding
    //lateinit var song : Song
    lateinit var timer : Timer
    private var mediaPlayer: MediaPlayer? = null
    private var gson: Gson = Gson()
    private var isRepeat = false

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayList()
        initSong()
        initClickListener()
        //setPlayer(song)
    }

    private fun initPlayList() {
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun initClickListener() {
        binding.nuguBtnDownIv.setOnClickListener {
            finish()
//            Toast.makeText(this, intent.getStringExtra("title"), Toast.LENGTH_SHORT).show()
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

        binding.nuguBtnSkipNextIv.setOnClickListener {
            moveSong(+1)
        }

        binding.nuguBtnSkipPreviousIv.setOnClickListener {
            moveSong(-1)
        }

        binding.likeIconIv.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }
    }

    private fun initSong() {

        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)

        Log.d("now Song ID", songs[nowPos].id.toString())

        startTimer()
        setPlayer(songs[nowPos])
    }

    private fun moveSong(direct: Int) {
        if (nowPos + direct < 0) {
            //Toast.makeText(this, "first song", Toast.LENGTH_SHORT).show()
            CustomSnackbar.make(binding.root, "처음 곡입니다.").show()
            return
        }

        if (nowPos + direct >= songs.size) {
            //Toast.makeText(this, "last song", Toast.LENGTH_SHORT).show()
            CustomSnackbar.make(binding.root, "마지막 곡입니다.").show()
            return
        }

        nowPos += direct

        timer.interrupt()
        startTimer()

        mediaPlayer?.release()
        mediaPlayer = null

        setPlayer(songs[nowPos])
    }

    private fun getPlayingSongPosition(songId: Int): Int {
        for (i in 0 until songs.size) {
            if (songs[i].id == songId) {
                return i
            }
        }
        return 0
    }

    private fun setPlayer(song : Song) {
        binding.songTitleTv.text = song.title
        binding.songSingerTv.text = song.singer
        binding.albumCoverMainIv.setImageResource(song.coverImg!!)
        binding.songStartTimeTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)

        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)

        song.playTime = mediaPlayer?.duration?.div(1000) ?: 0

        binding.songSeekbar.max = mediaPlayer?.duration ?: 0
        binding.songSeekbar.progress = song.second

        if (song.isLike) {
            binding.likeIconIv.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.likeIconIv.setImageResource(R.drawable.ic_my_like_off)
        }

        setPlayerStatus(song.isPlaying)
    }

    fun setPlayerStatus(isPlaying: Boolean) {
//        song.isPlaying = isPlaying
        songs[nowPos].isPlaying = isPlaying
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
        songs[nowPos].second = mediaPlayer?.currentPosition?.div(1000) ?: 0

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit() //에디터

        editor.putInt("songId", songs[nowPos].id)
        editor.putInt("second", songs[nowPos].second)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() //미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null //미디어 플레이어 해제
    }

    inner class Timer(private val playTime : Int, var isPlaying : Boolean = true):Thread() {
        private var second : Int = 0
        //private var milis : Float = 0f
//        private var milis: Float = (song.second * 1000).toFloat()  // 시작 위치 설정
        private var milis: Float = (songs[nowPos].second * 1000).toFloat()

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

    private fun setLike(isLike: Boolean) {
        songs[nowPos].isLike = !isLike
        songDB.songDao().updateIsLikeById(!isLike, songs[nowPos].id)

        if (!isLike) {
            binding.likeIconIv.setImageResource(R.drawable.ic_my_like_on)
            CustomSnackbar.make(binding.root, "좋아요 한 곡에 담겼습니다.").show()
        } else {
            binding.likeIconIv.setImageResource(R.drawable.ic_my_like_off)
            CustomSnackbar.make(binding.root, "좋아요 한 곡이 취소되었습니다.").show()
        }
    }

    private fun startTimer() {
//        timer = Timer(song.playTime, song.isPlaying)
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer.start()
    }

    private fun resetPlayer() {
//        song.second = 0
        songs[nowPos].second = 0
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