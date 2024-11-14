package com.example.flo

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var songDB: SongDatabase
    var nowPos = 0
    val songs = arrayListOf<Song>()
    private var mediaPlayer: MediaPlayer? = null


    private var gson: Gson = Gson()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputDummySongs()
        initPlayList()
        initBottomNavigation()
        initMiniPlayerControls()
        togglePlayPause()


        binding.mainPlayerCl.setOnClickListener {
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId",songs[nowPos].id)
            editor.apply()

            val intent = Intent(this,SongActivity::class.java)
            startActivity(intent)
        }
    }



    override fun onStart() {
        super.onStart()

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songId = sharedPreferences.getInt("songId", 0)

        val songDB = SongDatabase.getInstance(this)!!

        songs[nowPos] = if (songId == 0){
            songDB.songDao().getSong(1)
        } else{
            songDB.songDao().getSong(songId)
        }

        Log.d("song ID", songs[nowPos].id.toString())
        setMiniPlayer(songs[nowPos])
        initMediaPlayer()  // MediaPlayer 초기화
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

    override fun onStop() {
        super.onStop()
        mediaPlayer?.release()  // MediaPlayer 리소스 해제
        mediaPlayer = null
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songId = sharedPreferences.getInt("songId", 0)
        nowPos = getPlayingSongPosition(songId)
        setMiniPlayer(songs[nowPos])
        initMediaPlayer()  // MediaPlayer 초기화
    }

    private fun initMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, resources.getIdentifier(songs[nowPos].music, "raw", packageName))
        mediaPlayer?.setOnCompletionListener {
            moveSong(+1)  // 노래가 끝나면 다음 곡으로 이동
        }
        if (songs[nowPos].isPlaying) {
            mediaPlayer?.start()
        }
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
                1
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
                2
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
                3
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
                4
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
                1
            )
        )


        songDB.songDao().insert(
            Song(
                "APT.",
                "로제 & Bruno Mars",
                0,
                170,
                false,
                "music_apt",
                R.drawable.img_album_apt,
                false,
                5
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
                6
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
                6
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
                7
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
                8
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
                9
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
                10
            )
        )
        val songDBData = songDB.songDao().getSongs()
        Log.d("DB data", songDBData.toString())

    }


    private fun setMiniPlayer(song : Song) {
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        Log.d("songInfo", song.toString())


        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val second = sharedPreferences.getInt("second", 0)
        Log.d("spfSecond", second.toString())
        binding.mainMiniplayerProgressSb.progress = (second * 100000 / song.playTime)
    }

    fun updateMainPlayerCl(albumIdx: Int) {
        // 앨범의 곡 목록을 가져오기
        val albumSongs = songDB.albumDao().getAlbumSongs(albumIdx) // 앨범 인덱스를 통한 곡 목록 가져오기

        // 앨범의 곡이 존재하는 경우
        if (albumSongs.isNotEmpty()) {
            nowPos = 0 // 앨범의 첫 번째 곡으로 설정
            songs.addAll(0, albumSongs)

            // 미니 플레이어 UI 업데이트
            setMiniPlayer(songs[nowPos])
            initMediaPlayer() // MediaPlayer 초기화

            if (mediaPlayer != null) {
                mediaPlayer?.start()
                songs[nowPos].isPlaying = true
                binding.mainMiniplayerBtn.visibility = View.GONE
                binding.mainPauseBtn.visibility = View.VISIBLE
            }
        }
    }




        private fun initMiniPlayerControls() {
        // 재생 버튼 클릭 리스너 설정
        binding.mainMiniplayerBtn.setOnClickListener {
            togglePlayPause()
        }

        binding.mainPauseBtn.setOnClickListener {
            togglePlayPause()
        }

        // 다음 곡 버튼 클릭 리스너 설정
        binding.btnMiniplayerNext.setOnClickListener {
            moveSong(+1)
            mediaPlayer?.start()
        }

        // 이전 곡 버튼 클릭 리스너 설정
        binding.btnMiniplayerPrevious.setOnClickListener {
            moveSong(-1)
            mediaPlayer?.start()
        }
    }

    // 재생/일시정지 상태 전환 함수
    private fun togglePlayPause() {
        val isPlaying = songs[nowPos].isPlaying
        songs[nowPos].isPlaying = !isPlaying

        if (isPlaying) {
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
            mediaPlayer?.start()
        } else {
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }

        }
    }



    // 다음/이전 곡으로 이동하는 함수
    private fun moveSong(direct: Int) {
        if (nowPos + direct < 0) {
            CustomSnackbar.make(binding.root, "처음 곡입니다.").show()
        }
        else if (nowPos + direct in 0 until songs.size) {
            nowPos += direct
            mediaPlayer?.release()  // 이전 곡의 MediaPlayer 해제
            initMediaPlayer()  // 새로운 곡의 MediaPlayer 초기화
            setMiniPlayer(songs[nowPos])

            // 이전 곡이 재생 중이었을 경우 다음 곡도 자동 재생
            if (songs[nowPos].isPlaying) {
                mediaPlayer?.start()
            }
            
            else if (nowPos + direct >= songs.size){
                CustomSnackbar.make(binding.root, "마지막 곡입니다").show()
            }
        }
    }

    private fun saveCurrentPosition() {
        val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
        editor.putInt("currentPosition", mediaPlayer?.currentPosition ?: 0)
        editor.putInt("songId", songs[nowPos].id)
        editor.apply()
    }

    override fun onPause() {
        super.onPause()
        saveCurrentPosition()  // 앱이 일시 중지될 때 재생 위치 저장
        mediaPlayer?.pause()
    }
}




