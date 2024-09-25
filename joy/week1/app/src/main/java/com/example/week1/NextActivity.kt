package com.example.week1

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class NextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next)

        val backButton = findViewById<ImageButton>(R.id.imageButton6)
        backButton.setOnClickListener{
            finish()
        }
    }
}