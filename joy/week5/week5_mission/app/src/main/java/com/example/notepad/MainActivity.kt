package com.example.notepad

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.notepad.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var memoText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener {
            val intent = Intent(this, MemoDisplayActivity::class.java)
            intent.putExtra("memo", binding.etMemo.text.toString())
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if (memoText != "") {
            val text = memoText.toString()
        }
    }

    override fun onPause() {
        super.onPause()
        memoText = binding.etMemo.text.toString()
    }

    override fun onRestart() {
        super.onRestart()
        AlertDialog.Builder(this)
            .setMessage("이어서 작성하시겠습니까?")
            .setPositiveButton("확인") { _, _ -> }
            .setNegativeButton("취소") { _, _ ->
                memoText = null
                binding.etMemo.text.clear()
            }
            .show()
    }
}