package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.MainActivity
import com.example.flo.SongDatabase
import com.example.flo.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity(), LoginView {

    lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.loginSignUpTv.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.loginSignInBtn.setOnClickListener {
            login()
        }
    }

    private fun login() {
        if (binding.loginIdEt.text.toString().isEmpty() || binding.loginDirectInputEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if (binding.loginPasswordEt.text.toString().isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val email : String = binding.loginIdEt.text.toString() + "@" + binding.loginDirectInputEt.text.toString()
        val pwd: String = binding.loginPasswordEt.text.toString()

        val authService = AuthService()
        authService.setLoginView(this)

        authService.login(User(email, pwd, ""))
    }


    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


    private fun saveJwt2(jwt: String) {
        val spf = getSharedPreferences("auth2" , MODE_PRIVATE)
        val editor = spf.edit()

        editor.putString("jwt", jwt)
        editor.apply()
    }

    override fun onLogInSuccess(isSuccess: Boolean) {
        if (isSuccess == true) {
            Toast.makeText(this, "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show()
            startMainActivity()
        }
    }


    override fun onLoginFailure() {
        Toast.makeText(this, "회원 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
    }
}

