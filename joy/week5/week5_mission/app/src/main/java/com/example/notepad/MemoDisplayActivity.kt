package com.example.notepad

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.notepad.databinding.ActivityMemoDisplayBinding

class MemoDisplayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMemoDisplayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemoDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val memo = intent.getStringExtra("memo")
        if (memo.isNullOrEmpty()) {
            binding.tvMemo.text = "메모가 없습니다."
            binding.tvMemo.setTextColor(resources.getColor(R.color.gray))
            binding.tvMemo.textSize = 20f
        } else {
            binding.tvMemo.text  = memo
        }
    }
}
