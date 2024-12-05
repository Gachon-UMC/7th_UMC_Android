package com.example.flo

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.WindowCompat
import com.example.flo.data.db.SongDatabase
import com.example.flo.data.entities.Album
import com.example.flo.data.entities.Song
import com.example.flo.databinding.ActivityMainBinding
import com.example.flo.main.home.HomeFragment
import com.example.flo.main.locker.LockerFragment
import com.example.flo.main.look.LookFragment
import com.example.flo.main.search.SearchFragment
import com.example.flo.main.song.SongActivity
import com.google.gson.Gson
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var song: Song = Song()
    private var gson: Gson = Gson()

    private var mediaPlayer: MediaPlayer ? = null

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val keyHash = Utility.getKeyHash(this)
        Log.d("KeyHash", keyHash ?: "Key hash not found")

        songDB = SongDatabase.getInstance(this)!!

        inputDummySongs()
        inputDummyAlbums()

        initBottomNavigation()
        initPlayList()

        setOnClickListener()

        WindowCompat.setDecorFitsSystemWindows(window, false)



        // marquee 효과를 위해 TextView가 선택 가능하도록 설정
        binding.mainMiniplayerTitleTv.isSelected = true
        binding.mainMiniplayerSingerTv.isSelected = true

        // 포커스 요청
        binding.mainMiniplayerTitleTv.requestFocus()
        binding.mainMiniplayerSingerTv.requestFocus()



        Log.d("MAIN/JWT_TO_SERVER", getJwt().toString())
    }

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
        Log.d("songInfo", song.toString())
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val second = spf.getInt("second", 0)
        Log.d("spfSecond", second.toString())
        binding.mainProgressSb.progress = (second * 100000 / song.playTime ) //10000: seekbar max값
    }

    fun updateMainPlayerCl(song: Song) {
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainProgressSb.progress = 0
    }

    private fun getJwt(): String? {
        val spf = this.getSharedPreferences("auth2", MODE_PRIVATE)
        return spf!!.getString("jwt", "")
    }

    private fun setOnClickListener() {
        binding.mainPlayerCl.setOnClickListener {

            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", songs[nowPos].id)
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)

            setPlayerStatus(false)
        }

        binding.mainMiniplayerBtn.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.mainPauseBtn.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.nuguBtnSkipNextIv.setOnClickListener {
            moveSong(+1)
        }

        binding.nuguBtnSkipPreviousIv.setOnClickListener {
            moveSong(-1)
        }
    }

    override fun onResume() {
        super.onResume()

        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)
        setMiniPlayer(songs[nowPos])
    }

    private fun getPlayingSongPosition(songId: Int): Int {
        for (i in 0 until songs.size) {
            if (songs[i].id == songId) {
                return i
            }
        }
        return 0
    }

    private fun initPlayList() {
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    fun setPlayerStatus(isPlaying: Boolean) {
        songs[nowPos].isPlaying = isPlaying

        if (isPlaying) {
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE

//            mediaPlayer = MediaPlayer.create(this, R.raw.music_old_love)
            mediaPlayer = MediaPlayer.create(this, resources.getIdentifier(songs[nowPos].music, "raw", packageName))
            mediaPlayer?.start()

            val handler = Handler(mainLooper)
            val updateProgressRunnable = object : Runnable {
                override fun run() {
                    val currentPosition = mediaPlayer?.currentPosition ?: 0
                    val progress = (currentPosition * 100) / songs[nowPos].playTime // Assuming playTime is in milliseconds
                    binding.mainProgressSb.progress = progress

                    if (mediaPlayer?.isPlaying == true) {
                        handler.postDelayed(this, 100) // Update every 100ms
                    }
                }
            }
            handler.post(updateProgressRunnable)
        } else {
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
            mediaPlayer?.pause()
        }
    }

    private fun moveSong(direct: Int) {
        if (nowPos + direct < 0) {
            Toast.makeText(this, "first song", Toast.LENGTH_SHORT).show()
            return
        }

        if (nowPos + direct >= songs.size) {
            Toast.makeText(this, "last song", Toast.LENGTH_SHORT).show()
            return
        }

        nowPos += direct

        val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
        editor.putInt("songId", songs[nowPos].id)
        editor.apply()

        //timer.interrupt()
        //startTimer()

        mediaPlayer?.release()
        mediaPlayer = null

        setMiniPlayer(songs[nowPos])
        setPlayerStatus(true)
    }

//    private fun getKeyHash(): String? {
//        return try {
//            val packageInfo = packageManager.getPackageInfo(packageName, android.content.pm.PackageManager.GET_SIGNATURES)
//            val signatures = packageInfo.signatures
//            val md = MessageDigest.getInstance("SHA")
//            signatures.forEach { signature ->
//                md.update(signature.toByteArray())
//            }
//            Base64.encodeToString(md.digest(), Base64.DEFAULT).trim()
//        } catch (e: Exception) {
//            Log.e("KeyHash", "Error getting key hash", e)
//            null
//        }
//    }

    private fun inputDummyAlbums() {
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.albumDao().getAlbums()

//        if (songs.isNotEmpty()) return
        if (albums.isNotEmpty()) return

        songDB.albumDao().insert(
            Album(
                1,
                "영화 대도시의 사랑법 (Original Motion Picture Soundtrack)",
                "Various Artists",
                R.drawable.img_album_cover_1,
            )
        )

        songDB.albumDao().insert(
            Album(
                2,
                "YOUNHA 7th Album 'GROWTH THEORY'",
                "윤하",
                R.drawable.img_album_cover_3
            )
        )

        songDB.albumDao().insert(
            Album(
                3,
                "Hadestown (Original Broadway Cast Recording)",
                "Anais Mitchell",
                R.drawable.img_album_cover_2
            )
        )

        songDB.albumDao().insert(
            Album(
                4,
                "Dear Evan Hason (Original Broadway Cast Recording)",
                "Various Artists",
                R.drawable.img_album_cover_4
            )
        )

        songDB.albumDao().insert(
            Album(
                5,
                "Kinky Boots (Original Broadway Cast Recording)",
                "Original Broadway Cast of Kinky Boots",
                R.drawable.img_album_cover_5
            )
        )

        val _album = songDB.albumDao().getAlbums()
        Log.d("Album DB data", _album.toString())
    }

    private fun inputDummySongs() {
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()

        if (songs.isNotEmpty()) return

        songDB.songDao().insert(
            Song(
                "Old Love",
                "Die Boy",
                0,
                251,
                false,
                "music_old_love",
                R.drawable.img_album_cover_1,
                false,
                1,
            )
        )

        songDB.songDao().insert(
            Song(
                "Friends",
                "Jihae Kimm",
                0,
                333,
                false,
                "music_friends",
                R.drawable.img_album_cover_1,
                false,
                1,
            )
        )

        songDB.songDao().insert(
            Song(
                "맹그로브",
                "윤하(YOUNHA)",
                0,
                259,
                false,
                "music_mangrove_tree",
                R.drawable.img_album_cover_3,
                false,
                2,
            )
        )

        songDB.songDao().insert(
            Song(
                "죽음의 나선",
                "윤하(YOUNHA)",
                0,
                357,
                false,
                "music_antmill",
                R.drawable.img_album_cover_3,
                false,
                2,
            )
        )

        songDB.songDao().insert(
            Song(
                "Road to Hell",
                "André De Sheilds & Hadestown Original Broadway Company",
                0,
                517,
                false,
                "music_road_to_hell",
                R.drawable.img_album_cover_2,
                false,
                3,
            )
        )

        songDB.songDao().insert(
            Song(
                "Any Way the Wind Blows",
                "Eva Noblezada & Jewelle Blackman &  Yvette Gonzalez-Nacer & Kay Trinidad",
                0,
                346,
                false,
                "music_any_way_the_wind_blows",
                R.drawable.img_album_cover_2,
                false,
                3,
            )
        )

        songDB.songDao().insert(
            Song(
                "Anybody Have a Map?",
                "Rachel Bay Jones & Jennifer Laura Thompson",
                0,
                226,
                false,
                "music_anybody_have_a_map",
                R.drawable.img_album_cover_4,
                false,
                4,
            )
        )

        songDB.songDao().insert(
            Song(
                "Waving Through A Window",
                "Ben Platt & Original Broadway Cast Of Dear Even Hansen",
                0,
                357,
                false,
                "music_waving_through_a_window",
                R.drawable.img_album_cover_4,
                false,
                4,
            )
        )

        songDB.songDao().insert(
            Song(
                "Price and Son Theme / The Most Beautiful Thing in the World",
                "Full Company & Kinky Boots Ensemble",
                0,
                620,
                false,
                "music_price_and_son_theme_the_most_beautiful_thing_in_the_world",
                R.drawable.img_album_cover_5,
                false,
                5,
            )
        )

        songDB.songDao().insert(
            Song(
                "Take What You Got",
                "Andy Kelso & Stark Sands & Kinky Boots Ensemble",
                0,
                319,
                false,
                "music_take_what_you_got",
                R.drawable.img_album_cover_5,
                false,
                5,
            )
        )

        val _songs = songDB.songDao().getSongs()
        Log.d("Song DB data", _songs.toString())
    }
}