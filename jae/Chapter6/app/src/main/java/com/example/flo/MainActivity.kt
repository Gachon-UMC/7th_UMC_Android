package com.example.flo

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.view.WindowCompat
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private var song: Song = Song("Default Title", "Default Singer", 0, 60, false, "default_music_file", null, false, 1)
    private var gson: Gson = Gson()
    private var mediaPlayer: MediaPlayer ? = null
    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0
    private lateinit var previousButton: ImageView
    private lateinit var nextButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setTheme(R.style.Theme_FLO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputDummySongs()
        initBottomNavigation()
        initPlayList()

        //val song = Song(binding.mainMiniplayerTitleTv.text.toString(), binding.mainMiniplayerSingerTv.text.toString(), 0, 60, false, "music_playing_pretend" )

        WindowCompat.setDecorFitsSystemWindows(window, false)

        previousButton = findViewById(R.id.btn_miniplayer_previous)
        nextButton = findViewById(R.id.btn_miniplayer_next)

        binding.mainPlayerCl.setOnClickListener {
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", song.id)
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

        previousButton.setOnClickListener {
            moveSong(-1) // 이전 노래로 이동
        }

        // 다음 버튼 클릭 리스너
        nextButton.setOnClickListener {
            moveSong(1) // 다음 노래로 이동
        }

        // marquee 효과를 위해 TextView가 선택 가능하도록 설정
        binding.mainMiniplayerTitleTv.isSelected = true
        binding.mainMiniplayerSingerTv.isSelected = true

        // 포커스 요청
        binding.mainMiniplayerTitleTv.requestFocus()
        binding.mainMiniplayerSingerTv.requestFocus()
    }


//    fun updateMainPlayerCl(album: Album) {
//        binding.mainMiniplayerTitleTv.text = album.title
//        binding.mainMiniplayerSingerTv.text = album.singer
//        binding.mainProgressSb.progress = 0
//    }




    override fun onResume() {
        super.onResume()

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songId = sharedPreferences.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)
        setMiniPlayer(songs[nowPos])
    }

    private fun getPlayingSongPosition(songId: Int): Int{
        for (i in 0 until songs.size){
            if (songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun initBottomNavigation() {
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

    fun setMiniPlayer(song: Song) {
        Log.d("MainActivity", "Received song: ${song.title} by ${song.singer}")
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        Log.d("songInfo", song.toString())
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val second = sharedPreferences.getInt("second", 0)
        Log.d("spfSecond", second.toString())
        if (song.playTime > 0) {
            binding.mainProgressSb.progress = (second * 100000 / song.playTime)
        } else {
            binding.mainProgressSb.progress = 0 // playTime이 0이면 진행 상태를 0으로 설정
        }
    }

    private fun inputDummySongs() {
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()

        // 데이터가 이미 있으면 아무 작업도 하지 않음
        if (songs.isNotEmpty()) return

        // 데이터 삽입
        songDB.songDao().insert(
            Song("라일락", "아이유 (IU)", 60, 0, false, "music_lilac", R.drawable.img_album_cover_5, false, 1))
        songDB.songDao().insert(
            Song("이 지금", "아이유 (IU)", 60, 0, false, "music_dlwlrma", R.drawable.img_album_cover_4, false, 1))
        songDB.songDao().insert(
            Song("을의 연애 (WITH 박주원)", "아이유 (IU)", 60, 0, false, "music_loveofb", R.drawable.img_album_cover_3, false, 1))
        songDB.songDao().insert(
            Song("비밀", "아이유 (IU)", 60, 0, false, "music_secret", R.drawable.img_album_cover_2, false, 1))
        songDB.songDao().insert(
            Song("바라보기", "아이유 (IU)", 60, 0, false, "music_lookingatyou", R.drawable.img_album_cover_1, false, 1))

        // 삽입 후 확인용 로그
        val _songs = songDB.songDao().getSongs()
        Log.d("DB data", _songs.toString())

        // 삽입 후 데이터가 제대로 들어갔는지 확인
        val insertedSongs = songDB.songDao().getSongs()
        Log.d("inputDummySongs", "Inserted Songs Size: ${insertedSongs.size}")
        insertedSongs.forEach { song ->
            Log.d("inputDummySongs", "Song: ${song.title} by ${song.singer}")
        }
    }
    override fun onStart() {
        super.onStart()
//        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
//        val songJson = sharedPreferences.getString("songData", null)
//
//        song = if (songJson == null) {
//            Song("라일락", "아이유 (IU)", 0, 60, false, "music_lilac")
//        } else {
//            gson.fromJson(songJson, Song::class.java).apply {
//                Log.d("MainActivity", "Loaded song data: $songJson")
//            }
//        }
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        val songDB = SongDatabase.getInstance(this)!!

        song = if (songId == 0) {
            songDB.songDao().getSong(1)
        }
        else {
            songDB.songDao().getSong(songId)
        }
        Log.d("song ID for search", song.id.toString())
        setMiniPlayer(song)
        setPlayerStatus(song.isPlaying)
    }

    fun setPlayerStatus(isPlaying: Boolean) {
        song.isPlaying = isPlaying

        if (isPlaying) {
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE

            // 기존 MediaPlayer 해제
            mediaPlayer?.release()
            mediaPlayer = null  // 명시적으로 null 처리

            // 현재 song 객체의 music 파일로 MediaPlayer 초기화
            val musicResId = resources.getIdentifier(song.music, "raw", packageName)
            mediaPlayer = MediaPlayer.create(this, musicResId)
            mediaPlayer?.start()
        } else {
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
            mediaPlayer?.pause()
        }
    }


}