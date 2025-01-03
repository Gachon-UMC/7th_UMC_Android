package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.Button1.setOnClickListener {
            val intent = Intent(this, sub1::class.java)
            startActivity(intent)
        }
        binding.Button2.setOnClickListener {
            val intent = Intent(this, sub2::class.java)
            startActivity(intent)
        }
        binding.Button3.setOnClickListener {
            val intent = Intent(this, sub3::class.java)
            startActivity(intent)
        }
        binding.Button4.setOnClickListener {
            val intent = Intent(this, sub4::class.java)
            startActivity(intent)
        }
        binding.Button5.setOnClickListener {
            val intent = Intent(this, sub5::class.java)
            startActivity(intent)
        }












    }
}
