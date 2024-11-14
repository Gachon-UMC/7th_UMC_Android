package com.example.flo

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity: AppCompatActivity() {

    lateinit var binding : ActivitySongBinding
    lateinit var timer : Timer
    private var mediaPlayer: MediaPlayer? = null
    private var gson: Gson = Gson()
    private var isRepeat = false
    val songs = arrayListOf<Song>()
    lateinit var songDB : SongDatabase
    var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()
        initPlayList()
        initClickListner()
        setPlayer(songs[nowPos])

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

    private fun initPlayList() {
        songDB = SongDatabase.getInstance(this)!!
        songs.clear()
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun initSong() {
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)

        if (songs.isNotEmpty() && nowPos < songs.size) {
            startTimer()
            setPlayer(songs[nowPos])
        } else {
            Log.d("Error", "The songs list is empty or nowPos is out of bounds.")
        }
    }

    private fun getPlayingSongPosition(songId : Int) : Int{
        for (i in 0 until songs.size) {
            if (songs[i].id == songId) {
                return i
            }
        }
        return 0
    }

    private fun setPlayer(song: Song) {
        binding.songTitleTv.text = song.title
        binding.songSingerTv.text = song.singer
        binding.albumCoverMainIv.setImageResource(song.coverImg!!)

        // MediaPlayer를 설정하고 playTime 값을 song에 업데이트
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)
        song.playTime = mediaPlayer?.duration?.div(1000) ?: 0  // 전체 재생 시간(초)을 할당
        binding.songSeekbar.max = mediaPlayer?.duration ?: 0
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songStartTimeTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)

        if (song.playTime > 0) {
            binding.songSeekbar.progress = (song.second * 100000) / song.playTime  // SeekBar 진행 상태 설정
        } else {
            binding.songSeekbar.progress = 0
        }

        if (song.isLike) {
            binding.likeIconIv.setImageResource(R.drawable.ic_my_like_on)
        }
        else {
            binding.likeIconIv.setImageResource(R.drawable.ic_my_like_off)
        }

        binding.songSeekbar.progress = song.second
        setPlayerStatus(song.isPlaying)
    }

    fun setPlayerStatus(isPlaying: Boolean) {
        if (!::timer.isInitialized) {
            startTimer()
        }

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
        songs[nowPos].second = (songs[nowPos].playTime * binding.songSeekbar.progress) / 100000
        Log.d("second", songs[nowPos].second.toString())
        songs[nowPos].isPlaying = false
        setPlayerStatus(false)
        //song.second = ((binding.songSeekbar.progress * song.playTime)/100)/1000
        //songs[nowPos].second = mediaPlayer?.currentPosition?.div(1000) ?: 0
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val songJson = gson.toJson(songs[nowPos])
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

    private fun initClickListner() {
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

    private fun setLike(isLike : Boolean) {
        songs[nowPos].isLike = !isLike
        songDB.songDao().updateIsLikeById(!isLike, songs[nowPos].id)

        if (!isLike) {
            binding.likeIconIv.setImageResource(R.drawable.ic_my_like_on)
            showCustomToast("좋아요를 눌렀어요.")  // 커스텀 Toast 표시
        }
        else {
            binding.likeIconIv.setImageResource(R.drawable.ic_my_like_off)
            showCustomToast("좋아요를 취소했어요.")  // 커스텀 Toast 표시
        }
    }

    private fun moveSong(direct : Int) {
        if (nowPos + direct < 0) {
            Toast.makeText(this,"first song",Toast.LENGTH_SHORT).show()
        }

        else if (nowPos + direct >= songs.size){
            Toast.makeText(this,"last song",Toast.LENGTH_SHORT).show()
        }

        else {
            nowPos += direct
            timer.interrupt()
            startTimer()

            mediaPlayer?.release()
            mediaPlayer = null

            setPlayer(songs[nowPos])
        }
    }

    private fun showCustomToast(message: String) {
        val toast = Toast(applicationContext)
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.toast, null)  // 커스텀 레이아웃 적용

        val textView: TextView = layout.findViewById(R.id.toast_message)
        textView.text = message  // 메시지 설정

        toast.duration = Toast.LENGTH_SHORT
        toast.setGravity(Gravity.BOTTOM, 0, 200)  // 위치 설정
        toast.view = layout  // 커스텀 레이아웃 적용
        toast.show()
    }

    inner class Timer(private val playTime : Int, var isPlaying : Boolean = true):Thread() {
        private var second : Int = 0
        //private var milis : Float = 0f
        private var milis: Float = (songs[nowPos].second * 1000).toFloat()  // 시작 위치 설정
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
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer.start()
    }

    private fun resetPlayer() {
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