package com.example.flo

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    lateinit var binding: ActivitySongBinding
    lateinit var song : Song
    lateinit var timer : Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()
        setPlayer(song)
        initClickListener()

        }

    private fun initClickListener(){
        binding.songDownIb.setOnClickListener {
            Toast.makeText(this,intent.getStringExtra("title"), Toast.LENGTH_SHORT).show()
            finish()
        }
        binding.songMiniplayerPlayIv.setOnClickListener {
//            if (binding.songMiniplayerPlayIv.drawable.constantState == resources.getDrawable(R.drawable.btn_miniplayer_play).constantState) {
//                binding.songMiniplayerPlayIv.setImageResource(R.drawable.btn_miniplay_pause)
//            } else {
//                binding.songMiniplayerPlayIv.setImageResource(R.drawable.btn_miniplayer_play)
//            }
            song.isPlaying = !song.isPlaying
            setPlayerStatus(song.isPlaying)
        }
        binding.songMiniplayerPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }
    }

    private fun initSong(){
        if(intent.hasExtra("title") && intent.hasExtra("singer")) {
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second", 0),
                intent.getIntExtra("playTime", 0),
                intent.getBooleanExtra("isPlaying", false)
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

        setPlayerStatus(song.isPlaying)
    }

    private fun setPlayerStatus(isPlaying: Boolean) {
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if (isPlaying) {
            binding.songMiniplayerPlayIv.visibility = View.GONE
            binding.songMiniplayerPauseIv.visibility = View.VISIBLE
        } else {
            binding.songMiniplayerPlayIv.visibility = View.VISIBLE
            binding.songMiniplayerPauseIv.visibility = View.GONE
        }
    }

    private fun startTimer() {
        timer = Timer(song.playTime, song.isPlaying)
        timer.start()
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true):Thread() {
        private var second : Int = 0
        private var mills : Float = 0f

        override fun run() {
            super.run()
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
                            binding.songStartTimeTv.text = String.format("%02d:%02d", second / 60, second % 60)
                        }
                        second++
                    }
                }
            }
        }
    }
}
