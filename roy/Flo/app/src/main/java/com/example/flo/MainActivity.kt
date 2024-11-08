package com.example.flo

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0


    private var gson: Gson = Gson()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputDummySongs()
        initPlayList()
        initBottomNavigation()

        binding.mainPlayerCl.setOnClickListener {

            val currentSong = songs[nowPos]  // 현재 위치의 Song 객체를 가져옴

            val intent = Intent(this, SongActivity::class.java)

            intent.putExtra("title", currentSong.title)
            intent.putExtra("singer", currentSong.singer)
            intent.putExtra("second", currentSong.second)
            intent.putExtra("playTime", currentSong.playTime)
            intent.putExtra("isPlaying", currentSong.isPlaying)
            intent.putExtra("music", currentSong.music)

            startActivity(intent)
        }
    }





    private fun initBottomNavigation() {

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    override fun onResume() {
        super.onResume()

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songId = sharedPreferences.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)
        setMiniPlayer(songs[nowPos])
    }

    private fun initPlayList() {
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun getPlayingSongPosition(songId: Int): Int {
        for (i in 0 until songs.size) {
            if (songs[i].id == songId) {
                return i
            }
        }
        return 0
    }


    private fun inputDummySongs() {
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()

        if (songs.isNotEmpty()) return



        songDB.songDao().insert(
            Song(
                "Welcome to the Show",
                "데이식스 (DAY6)",
                0,
                218,
                false,
                "music_welcometotheshow",
                R.drawable.img_album_happy,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "Whiplash",
                "에스파 (AESPA)",
                0,
                185,
                false,
                "music_whiplash",
                R.drawable.img_album_whiplash,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "내 이름 맑음",
                "QWER",
                0,
                190,
                false,
                "music_blossom",
                R.drawable.img_album_qwer,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "POWER",
                "G-DRAGON",
                0,
                145,
                false,
                "music_power",
                R.drawable.img_album_power,
                false,
            )
        )


        songDB.songDao().insert(
            Song(
                "HAPPY",
                "데이식스 (DAY6)",
                0,
                190,
                false,
                "music_happy",
                R.drawable.img_album_happy,
                false,
            )
        )


        songDB.songDao().insert(
            Song(
                "APT",
                "로제 & Bruno Mars",
                0,
                170,
                false,
                "music_apt",
                R.drawable.img_album_apt,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "Lilac",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_lilac",
                R.drawable.img_album_exp2,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "Flu",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_flu",
                R.drawable.img_album_exp2,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "Butter",
                "방탄소년단 (BTS)",
                0,
                190,
                false,
                "music_butter",
                R.drawable.img_album_exp,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "Next Level",
                "에스파 (AESPA)",
                0,
                210,
                false,
                "music_next",
                R.drawable.img_album_exp3,
                false,
            )
        )


        songDB.songDao().insert(
            Song(
                "Boy with Luv",
                "music_boy",
                0,
                230,
                false,
                "music_boy",
                R.drawable.img_album_exp4,
                false,
            )
        )


        songDB.songDao().insert(
            Song(
                "BBoom BBoom",
                "모모랜드 (MOMOLAND)",
                0,
                240,
                false,
                "music_bboom",
                R.drawable.img_album_exp5,
                false,
            )
        )

    }


    private fun setMiniPlayer(song: Song) {
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val second = sharedPreferences.getInt("second", 0)
        binding.mainMiniplayerProgressSb.progress = (second * 100000 / song.playTime)
    }

    fun updateMainPlayerCl(album : Album) {
        binding.mainMiniplayerTitleTv.text = album.title
        binding.mainMiniplayerSingerTv.text = album.singer
        binding.mainMiniplayerProgressSb.progress = 0
    }
}