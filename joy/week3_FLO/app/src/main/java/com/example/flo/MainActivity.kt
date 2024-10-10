package com.example.flo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.WindowCompat
import com.example.flo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val song = Song(binding.mainMiniplayerTitleTv.text.toString(), binding.mainMiniplayerSingerTv.text.toString())

        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding.mainPlayerCl.setOnClickListener {
            //startActivity(Intent(this, SongActivity::class.java) )
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
            startActivity(intent)
        }

        initBottomNavigation()
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
}