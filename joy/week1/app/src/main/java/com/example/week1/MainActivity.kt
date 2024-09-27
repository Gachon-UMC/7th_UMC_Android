package com.example.week1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val buttonIds = arrayOf(
            R.id.imageButton,
            R.id.imageButton2,
            R.id.imageButton3 ,
            R.id.imageButton4,
            R.id.imageButton5
        )

        for (id in buttonIds) {
            val button = findViewById<ImageButton>(id)
            button.setOnClickListener {
                val intent = Intent(this, NextActivity::class.java)
                startActivity(intent)
            }
        }
    }
}