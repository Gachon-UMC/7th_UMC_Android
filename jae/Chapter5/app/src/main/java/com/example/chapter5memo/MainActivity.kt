package com.example.chapter5memo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chapter5memo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var memoText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener {
            val intent = Intent(this, ConfirmActivity::class.java)
            intent.putExtra("memo", binding.editTextMemo.text.toString())
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.editTextMemo.setText(memoText ?: "")
    }

    override fun onPause() {
        super.onPause()
        memoText = binding.editTextMemo.text.toString()
    }

    override fun onRestart() {
        super.onRestart()
        val dialog = android.app.AlertDialog.Builder(this)
            .setTitle("메모 이어서 작성하기")
            .setMessage("이어 작성하시겠습니까?")
            .setPositiveButton("네") { _, _ -> }
            .setNegativeButton("아니요") { _, _ ->
                memoText = null
                binding.editTextMemo.text.clear()
                Toast.makeText(this, "메모가 없습니다.", Toast.LENGTH_SHORT).show()
            }
            .create()
        dialog.show()
    }
}