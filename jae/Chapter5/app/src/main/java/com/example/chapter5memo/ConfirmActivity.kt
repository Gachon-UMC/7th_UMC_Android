package com.example.chapter5memo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chapter5memo.databinding.ActivityConfirmBinding

class ConfirmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val memo = intent.getStringExtra("memo")
        binding.textViewMemo.text = memo
    }
}
