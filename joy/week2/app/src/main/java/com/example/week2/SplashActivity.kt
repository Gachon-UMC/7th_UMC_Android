package com.example.week2

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.week2.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 시스템 창과의 레이아웃을 확장하여 상태바 뒤로 그리기
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // View Binding 초기화 및 setContentView 호출
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 상태바를 투명하게 설정
        window.statusBarColor = Color.TRANSPARENT

        // WindowInsetsControllerCompat을 사용하여 상태바 아이콘 색상 설정
        val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = true // 아이콘을 검정색으로 설정

        showSplashScreen1()

        Handler(Looper.getMainLooper()).postDelayed({
            showSplashScreen2()
        }, 2000)
    }

    private fun showSplashScreen1() {
        binding.splashScreen1Layout.visibility = View.VISIBLE

        val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = true
    }

    private fun showSplashScreen2() {
        binding.splashScreen1Layout.visibility = View.GONE
        binding.splashScreen2Layout.visibility = View.VISIBLE

        window.statusBarColor = Color.TRANSPARENT
        val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = false

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            //이전 키를 눌렀을 때 Splash 화면으로 이동을 방지하기 위해
            //이동한 다음 사용하지 않음으로 finish 처리
            finish()
        }, 2000)
    }
}