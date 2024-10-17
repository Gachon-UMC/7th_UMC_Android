package com.example.flo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import com.example.flo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setTheme(R.style.Theme_FLO)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val song = Song(binding.mainMiniplayerTitleTv.text.toString(), binding.mainMiniplayerSingerTv.text.toString(), 0, 60, false)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding.mainPlayerCl.setOnClickListener {
            //startActivity(Intent(this, SongActivity::class.java) )
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
            intent.putExtra("second", song.second)
            intent.putExtra("playTime", song.playTime)
            intent.putExtra("isPlaying", song.isPlaying)
            startActivity(intent)
        }

        initBottomNavigation()

//        val a = A()
//        val b = B()
//
//        a.start()
//        a.join()
//        b.start()
//
//        val handler = Handler(Looper.getMainLooper())
//
//        val imageList = arrayListOf<Int>()
//
//        imageList.add(R.drawable.ic_main_twitter)
//        imageList.add(R.drawable.ic_main_youtube)
//        imageList.add(R.drawable.ic_main_instagram)
//        imageList.add(R.drawable.ic_main_facebook)
//        imageList.add(R.drawable.ic_main_twitter)
//        imageList.add(R.drawable.ic_main_youtube)
//        imageList.add(R.drawable.ic_main_instagram)
//        imageList.add(R.drawable.ic_main_facebook)
//
//        Thread{
//            for (image in imageList) {
//                runOnUiThread {
//                    binding.iv.setImageResource(image)
//                }
//                Thread.sleep(2000)
//            }
//        }.start()
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
//    class A : Thread() {
//        override fun run() {
//            super.run()
//            for (i in 1..1000){
//                Log.d("test", "first : $i")
//            }
//        }
//    }
//
//    class B : Thread() {
//        override fun run() {
//            super.run()
//            for (i in 1000 downTo 1){
//                Log.d("test", "second : $i")
//            }
//        }
//    }
}