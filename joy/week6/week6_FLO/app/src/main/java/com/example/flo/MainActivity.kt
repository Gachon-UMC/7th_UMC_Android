package com.example.flo

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.WindowCompat
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var song: Song = Song("Default Title", "Default Singer", 0, 60, false, "default_music_file")
    private var gson: Gson = Gson()

    private var mediaPlayer: MediaPlayer ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setTheme(R.style.Theme_FLO)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val song = Song(binding.mainMiniplayerTitleTv.text.toString(), binding.mainMiniplayerSingerTv.text.toString(), 0, 60, false, "music_playing_pretend" )

        WindowCompat.setDecorFitsSystemWindows(window, false)

//        if (intent.hasExtra("title")) {
//            intent.getStringExtra("title")
//            intent.getStringExtra("singer")
//            intent.getIntExtra("second", 0)
//            intent.getIntExtra("playTime", 0)
//            intent.getBooleanExtra("isPlaying", false)
//            intent.getStringExtra("music")
//            intent.getIntExtra("coverImg", R.drawable.img_album_cover_1)
//        }

        binding.mainPlayerCl.setOnClickListener {
            //startActivity(Intent(this, SongActivity::class.java) )

            val intent = Intent(this, SongActivity::class.java)
//            intent.putExtra("title", song.title)
//            intent.putExtra("singer", song.singer)
            intent.putExtra("title", binding.mainMiniplayerTitleTv.text.toString())
            intent.putExtra("singer", binding.mainMiniplayerSingerTv.text.toString())
            intent.putExtra("second", song.second)
            intent.putExtra("playTime", song.playTime)
            intent.putExtra("isPlaying", song.isPlaying)
            intent.putExtra("music", song.music)
            if (binding.mainMiniplayerTitleTv.text.toString().equals("Old Love")) {
                intent.putExtra("coverImg", R.drawable.img_album_cover_1)
            }
            if (binding.mainMiniplayerTitleTv.text.toString().equals("맹그로브")) {
                intent.putExtra("coverImg", R.drawable.img_album_cover_3)
            }
            if (binding.mainMiniplayerTitleTv.text.toString().equals("Road to Hell")) {
                intent.putExtra("coverImg", R.drawable.img_album_cover_2)
            }
            if (binding.mainMiniplayerTitleTv.text.toString().equals("Anybody Have a Map?")) {
                intent.putExtra("coverImg", R.drawable.img_album_cover_4)
            }
            if (binding.mainMiniplayerTitleTv.text.toString().equals("Price and Son Theme / The Most Beautiful Thing in the World")) {
                intent.putExtra("coverImg", R.drawable.img_album_cover_5)
            }
            startActivity(intent)
        }

        binding.mainMiniplayerBtn.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.mainPauseBtn.setOnClickListener {
            setPlayerStatus(false)
        }

        // marquee 효과를 위해 TextView가 선택 가능하도록 설정
        binding.mainMiniplayerTitleTv.isSelected = true
        binding.mainMiniplayerSingerTv.isSelected = true

        // 포커스 요청
        binding.mainMiniplayerTitleTv.requestFocus()
        binding.mainMiniplayerSingerTv.requestFocus()

        initBottomNavigation()
    }

//    fun updateMainPlayerCl(album: Album) {
//        binding.mainMiniplayerTitleTv.text = album.title
//        binding.mainMiniplayerSingerTv.text = album.singer
//        binding.mainProgressSb.progress = 0
//    }

    private fun initBottomNavigation(){

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener{ item ->
            val fragment = when (item.itemId) {
                R.id.homeFragment -> HomeFragment()
                R.id.lookFragment -> LookFragment()
                R.id.searchFragment -> SearchFragment()
                R.id.lockerFragment -> LockerFragment()
                else -> null
            }

            fragment?.let {
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .replace(R.id.main_frm, it)
                    .commitAllowingStateLoss()
            }
            return@setOnItemSelectedListener true
        }
    }

    fun setMiniPlayer(song: Song) {
        Log.d("MainActivity", "Received song: ${song.title} by ${song.singer}")
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainProgressSb.progress = (song.second*100000) / song.playTime //10000: seekbar max값
    }

    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songJson = sharedPreferences.getString("songData", null)

        song = if(songJson == null) {
            Song("Playing Pretend", "샘김 (Sam Kim)", 0, 60, false, "music_playing_pretend")
        } else {
            gson.fromJson(songJson, Song::class.java).apply {
                Log.d("MainActivity", "Loaded song data: $songJson")
            }
        }

        setMiniPlayer(song)
        setPlayerStatus(song.isPlaying)
    }

    fun setPlayerStatus(isPlaying: Boolean) {
        song.isPlaying = isPlaying

        if (isPlaying) {
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE

            mediaPlayer = MediaPlayer.create(this, R.raw.music_old_love)
            mediaPlayer?.start()
        } else {
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
            mediaPlayer?.pause()
        }
    }
}