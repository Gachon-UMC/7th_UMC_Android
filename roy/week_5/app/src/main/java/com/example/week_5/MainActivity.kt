package com.example.week_5

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.week_5.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var memoText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonNext.setOnClickListener {
            // EditText의 내용을 memoText에 저장
            memoText = binding.editTextMemo.text.toString()

            val intent = Intent(this, ConfirmActivity::class.java)
            intent.putExtra("MEMO_TEXT", memoText)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!memoText.isNullOrEmpty()) {
            binding.editTextMemo.setText(memoText)
        }
    }

    override fun onPause() {
        super.onPause()
        // EditText의 내용을 memoText에 저장
        memoText = binding.editTextMemo.text.toString()
    }

    override fun onRestart() {
        super.onRestart()
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("이어서 작성할까요?")
            .setCancelable(false)
            .setPositiveButton("네") { dialog, id ->
                // Nothing to do, just continue editing
            }
            .setNegativeButton("아니요") { dialog, id ->
                memoText = null
                binding.editTextMemo.setText("") // EditText 내용도 비워줍니다.
            }
        val alert = dialogBuilder.create()
        alert.show()
    }
}