package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityLogInBinding
import kotlin.math.log

class LogInActivity: AppCompatActivity() {

    lateinit var binding: ActivityLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogInBinding.inflate(layoutInflater)

        initClickListener()

        setContentView(binding.root)
    }

    private fun initClickListener() {
        binding.logInCloseBtn.setOnClickListener {
            finish()
        }

        binding.logInToSignUpTv.setOnClickListener {
            startActivity(Intent(this, SignUpOptionActivity::class.java))
        }

        binding.logInActivateBtn.setOnClickListener {
            logIn()
        }
    }

    private fun logIn() {
        if (binding.logInEmailEditTv.text.toString().isEmpty() || binding.logInDirectEditTv.text.toString().isEmpty()) {
            Toast.makeText(this, "이메일을 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.logInPwEditTv.text.toString().isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val email : String = binding.logInEmailEditTv.text.toString() + "@" + binding.logInDirectEditTv.text.toString()
        val pwd: String = binding.logInPwEditTv.text.toString()

        val songDB = SongDatabase.getInstance(this)!!
        val user = songDB.userDao().getUser(email, pwd)

        user?.let {
            Log.d("LOGIN_ACT/GET_USER", "userId : ${user.id}, $user")
            saveJwt(user.id)
            startActivity()
        }
        Toast.makeText(this, "회원 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun saveJwt(jwt: Int) {
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putInt("jwt", jwt)
        editor.apply()
    }

    private fun startActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}