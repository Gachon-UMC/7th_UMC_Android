package com.example.flo.signup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.data.entities.User
import com.example.flo.data.remote.AuthService
import com.example.flo.databinding.ActivitySignUpBinding
import com.example.flo.login.LogInActivity

class SignUpActivity: AppCompatActivity(), SignUpView {

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
//            startActivity(Intent(this, LogInActivity::class.java))
        }
    }

    private fun getUser() : User {
        val email : String = binding.signUpEmailEditTv.text.toString() + "@" + binding.signUpDirectEditTv.text.toString()
        val pwd: String = binding.signUpPwEditTv.text.toString()
        val name : String = binding.signUpNameEditTv.text.toString()

        return User(email, pwd, name)
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

        if (binding.signUpNameEditTv.text.toString().isEmpty() || binding.signUpDirectEditTv.text.toString().isEmpty()) {
            Toast.makeText(this, "이름 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val authService = AuthService()
        authService.setSignUpView(this)

        authService.signUp(getUser())
    }

    override fun onSignUpSuccess() {
        Toast.makeText(this, "회원 가입에 성공했습니다.", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LogInActivity::class.java))
        finish()
    }

    override fun onSignUpFailure() {
        Toast.makeText(this, "회원가입에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
    }
}