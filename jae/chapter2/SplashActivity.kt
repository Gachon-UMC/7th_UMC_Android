package com.example.chapter2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 2초 후에 MainActivity로 이동
        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish() // 현재 SplashActivity 종료
        }, 2000) // 2초 대기
    }
}
