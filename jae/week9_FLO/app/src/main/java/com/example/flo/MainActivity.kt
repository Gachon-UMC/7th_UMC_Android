package com.example.flo

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.WindowCompat
import com.example.flo.data.entities.Song
import com.example.flo.databinding.ActivityMainBinding
import com.example.flo.ui.main.home.HomeFragment
import com.example.flo.ui.main.locker.LockerFragment
import com.example.flo.ui.main.look.LookFragment
import com.example.flo.ui.main.search.SearchFragment
import com.example.flo.ui.song.SongActivity
import com.example.flo.ui.song.SongDatabase
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var song: Song = Song()
    private var gson: Gson = Gson()

    private var mediaPlayer: MediaPlayer ? = null

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    private lateinit var previousButton: ImageView
    private lateinit var nextButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        songDB = SongDatabase.getInstance(this)!!

        inputDummySongs()
        inputDummyAlbums()

        initBottomNavigation()

        initPlayList()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        previousButton = findViewById(R.id.btn_miniplayer_previous)
        nextButton = findViewById(R.id.btn_miniplayer_next)

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

        // marquee 효과를 위해 TextView가 선택 가능하도록 설정
        binding.mainMiniplayerTitleTv.isSelected = true
        binding.mainMiniplayerSingerTv.isSelected = true

        // 포커스 요청
        binding.mainMiniplayerTitleTv.requestFocus()
        binding.mainMiniplayerSingerTv.requestFocus()

        previousButton.setOnClickListener {
            moveSong(+1)
        }

        nextButton.setOnClickListener {
            moveSong(-1)
        }

        Log.d("MAIN/JWT_TO_SERVER", getJwt().toString())
    }

//    override fun onDeleteClicked() {
//
//        Toast.makeText(this, "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show()
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
        Log.d("songInfo", song.toString())

        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val second = spf.getInt("second", 0)
        Log.d("spfSecond", second.toString())

        if (song.playTime > 0) { // playTime이 0이 아닐 때만 진행
            binding.mainProgressSb.progress = (second * 100000 / song.playTime)
        } else {
            binding.mainProgressSb.progress = 0 // playTime이 0인 경우 SeekBar 초기화
            Log.e("setMiniPlayer", "Invalid playTime: ${song.playTime}")
        }
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

//    override fun onStart() {
//        super.onStart()
//
//        val spf = getSharedPreferences("song", MODE_PRIVATE)
//        val songId = spf.getInt("songId", 0)
//
//        val songDB = SongDatabase.getInstance(this)!!
//
//        song = if (songId == 0) {
//            songDB.songDao().getSong(1)
//        } else {
//            songDB.songDao().getSong(songId)
//        }
//
//        Log.d("song ID", songs[nowPos].id.toString())
//
//        //nowPos = songs.indexOfFirst { it.id == songId }
////        setMiniPlayer(song)
////        setPlayerStatus(song.isPlaying)
//
//        setMiniPlayer(songs[nowPos])
//        setPlayerStatus(songs[nowPos].isPlaying)
//    }

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

    private fun inputDummyAlbums() {
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.albumDao().getAlbums()

        if (songs.isNotEmpty()) return
        if (albums.isNotEmpty()) return

        songDB.songDao().insert(
            Song("라일락", "아이유 (IU)", 60, 0, false, "music_lilac", R.drawable.img_album_cover_5, false, 1)
        )
        songDB.songDao().insert(
            Song("이 지금", "아이유 (IU)", 60, 0, false, "music_dlwlrma", R.drawable.img_album_cover_4, false, 1)
        )
        songDB.songDao().insert(
            Song("을의 연애 (WITH 박주원)", "아이유 (IU)", 60, 0, false, "music_loveofb", R.drawable.img_album_cover_3, false, 1)
        )
        songDB.songDao().insert(
            Song("비밀", "아이유 (IU)", 60, 0, false, "music_secret", R.drawable.img_album_cover_2, false, 1)
        )
        songDB.songDao().insert(
            Song("바라보기", "아이유 (IU)", 60, 0, false, "music_lookingatyou", R.drawable.img_album_cover_1, false, 1)
        )

        val _album = songDB.albumDao().getAlbums()
        Log.d("Album DB data", _album.toString())
    }

    private fun inputDummySongs() {
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()

        if (songs.isNotEmpty()) return

        songDB.songDao().insert(
            Song("라일락", "아이유 (IU)", 60, 0, false, "music_lilac", R.drawable.img_album_cover_5, false, 1)
        )
        songDB.songDao().insert(
            Song("이 지금", "아이유 (IU)", 60, 0, false, "music_dlwlrma", R.drawable.img_album_cover_4, false, 1)
        )
        songDB.songDao().insert(
            Song("을의 연애 (WITH 박주원)", "아이유 (IU)", 60, 0, false, "music_loveofb", R.drawable.img_album_cover_3, false, 1)
        )
        songDB.songDao().insert(
            Song("비밀", "아이유 (IU)", 60, 0, false, "music_secret", R.drawable.img_album_cover_2, false, 1)
        )
        songDB.songDao().insert(
            Song("바라보기", "아이유 (IU)", 60, 0, false, "music_lookingatyou", R.drawable.img_album_cover_1, false, 1)
        )

        val _songs = songDB.songDao().getSongs()
        Log.d("Song DB data", _songs.toString())
    }
}