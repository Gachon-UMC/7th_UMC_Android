package com.example.week_5

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.week_5.databinding.ActivityConfirmBinding

class ConfirmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val memoText = intent.getStringExtra("MEMO_TEXT")

        // 메모 내용이 없을 경우 기본 텍스트 설정
        if (memoText.isNullOrEmpty()) {
            binding.textViewMemo.text = "작성한 메모가 없습니다."
        } else {
            binding.textViewMemo.text = memoText
        }
    }
}