package com.example.flo

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson
import java.util.Timer

class SongActivity : AppCompatActivity() {
    lateinit var binding: ActivitySongBinding
    lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null
    private var gson: Gson = Gson()
    private var isRepeatActive = false



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


        binding.songDownIb.setOnClickListener {
            finish()
        }

        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus()
        }

        binding.songPauseIv.setOnClickListener {
            setPlayerStatus()
        }

        binding.songLikeIv.setOnClickListener {
            setLike(songs[nowPos].isLike)
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
        songs[nowPos].second = (songs[nowPos].playTime * binding.songProgressSb.progress) / 100000
        Log.d("second", songs[nowPos].second.toString())
        songs[nowPos].isPlaying = false
        setPlayerStatus()

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("songId", songs[nowPos].id)
        editor.putInt("second", songs[nowPos].second)
        editor.apply()
    }



    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun initClickListener(){
        binding.songDownIb.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("message", songs[nowPos].title + "_" + songs[nowPos].singer)
            setResult(RESULT_OK, intent)
            finish()
        }

        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus()
        }

        binding.songPauseIv.setOnClickListener {
            setPlayerStatus()
        }

        binding.songNextIv.setOnClickListener {
            moveSong(+1)
            mediaPlayer?.start()
        }

        binding.songPreviousIv.setOnClickListener {
            moveSong(-1)
            mediaPlayer?.start()
        }
    }

    private fun moveSong(direct: Int) { // direct는 +1 또는 -1임
        if (nowPos + direct < 0) {
            CustomSnackbar.make(binding.root, "처음 곡입니다.").show()
        }
        else if (nowPos + direct in 0 until songs.size) {
            nowPos += direct
            mediaPlayer?.release()
            mediaPlayer = null
            setPlayer(songs[nowPos])

            if (songs[nowPos].isPlaying) {
                binding.songMiniplayerIv.visibility = View.GONE
                binding.songPauseIv.visibility = View.VISIBLE
                mediaPlayer?.start()
            }
        }
        else if (nowPos + direct >= songs.size){
            CustomSnackbar.make(binding.root, "마지막 곡입니다").show()
        }
    }







    private fun setLike(isLike: Boolean){
        songs[nowPos].isLike = !isLike
        songDB.songDao().updateIsLikeById(!isLike,songs[nowPos].id)

        if (!isLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
            CustomSnackbar.make(binding.root, "좋아요.").show()
        } else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
            CustomSnackbar.make(binding.root, "좋아요 취소.").show()
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

    private fun getPlayingSongPosition(songId: Int): Int{
        for (i in 0 until songs.size){
            if (songs[i].id == songId){
                return i
            }
        }
        return 0
    }



    private fun setPlayer(song : Song) {
        binding.songMusicTitleTv.text = song.title
        binding.songSingerNameTv.text = song.singer
        binding.songStartTimeTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songAlbumIv.setImageResource(song.coverImg!!)
        binding.songProgressSb.progress = (song.second * 1000 / song.playTime)

        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)

        if(song.isLike) {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }
        else {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }

        setPlayerStatus()
    }



    private fun setPlayerStatus() {
        val isPlaying = songs[nowPos].isPlaying
        songs[nowPos].isPlaying = !isPlaying

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
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer.start()
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true) : Thread() {
        private var second : Int = 0
        private var mills : Float = 0F

        override fun run() {
            super.run()

            try {
                while(true) {
                    if(second >= playTime) {
                        break
                    }

                    while (!isPlaying) {
                        sleep(200) // 0.2초 대기
                    }

                    if(isPlaying) {
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            // binding.songProgressSb.progress = ((mills/playTime*1000) * 100).toInt()
                            binding.songProgressSb.progress = ((mills/playTime) * 100).toInt()
                        }

                        if(mills % 1000 == 0F) { // 1초
                            runOnUiThread {
                                binding.songStartTimeTv.text = String.format("%02d:%02d", second / 60, second % 60)
                            }
                            second++
                        }
                    }
                }
            } catch (e: InterruptedException) {
                Log.d("SongActivity", "Thread Terminates! ${e.message}")
            }
        }
    }
}