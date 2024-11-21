package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySignUpBinding

class SignUpActivity: AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)

        initClickListener()

        setContentView(binding.root)
    }

    private fun initClickListener() {
        binding.signUpBackBtn.setOnClickListener {
            finish()
        }

        binding.signUpCompleteBtn.setOnClickListener {
            signUp()
            startActivity(Intent(this, LogInActivity::class.java))
        }
    }

    private fun getUser() : User {
        val email : String = binding.signUpEmailEditTv.text.toString() + "@" + binding.signUpDirectEditTv.text.toString()
        val pwd: String = binding.signUpPwEditTv.text.toString()

        return User(email, pwd)
    }

    private fun signUp() {
        if (binding.signUpEmailEditTv.text.toString().isEmpty() || binding.signUpDirectEditTv.text.toString().isEmpty()) {
            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.signUpPwEditTv.text.toString() != binding.logInPwCheckEditTv.text.toString()) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val userDB = SongDatabase.getInstance(this)!!
        userDB.userDao().insert(getUser())

        val user = userDB.userDao().getUsers()
        Log.d("SIGNUPACT", user.toString())
    }
}