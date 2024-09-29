package com.example.chapter1

import android.os.Bundle
import android.content.Intent
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val buttonIds = arrayOf(
            R.id.imageButton1,
            R.id.imageButton2,
            R.id.imageButton3 ,
            R.id.imageButton4,
            R.id.imageButton5
        )

        for (id in buttonIds) {
            val button = findViewById<ImageButton>(id)
            button.setOnClickListener {
                val intent = Intent(this, EmotionActivity::class.java)
                startActivity(intent)
            }
        }
    }
}







