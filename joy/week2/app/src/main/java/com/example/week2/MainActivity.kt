package com.example.week2

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity

import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.example.week2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    // 현재 선택된 메뉴 항목 ID를 저장하여 중복 네비게이션을 방지
    private var currentNavItemId: Int = R.id.fragment_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT

        val windowInsetController = WindowInsetsControllerCompat(window, window.decorView)
        windowInsetController.isAppearanceLightStatusBars = true //아이콘을 검정으로 설정

        // View Binding 초기화
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // NavHostFragment에서 NavController 가져오기
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // BottomNavigationView의 아이템 선택 리스너 설정
        binding.bottomNavi.setOnItemSelectedListener { item ->
            if(item.itemId != currentNavItemId) {
                val navOptions = NavOptions.Builder()
                    .setEnterAnim(R.anim.slide_in_right)
                    .setExitAnim(R.anim.slide_out_left)
                    .setPopEnterAnim(R.anim.slide_in_left)
                    .setPopExitAnim(R.anim.slide_out_right)
                    .build()

                navController.navigate(item.itemId, null, navOptions)
                currentNavItemId = item.itemId
                true
            } else {
                false
            }
        }
    }

    // BottomNavigationView의 표시/숨김을 제어하는 함수
    fun hideBottomNavigation(state: Boolean) {
        binding.bottomNavi.visibility = if (state) View.GONE else View.VISIBLE
    }
}