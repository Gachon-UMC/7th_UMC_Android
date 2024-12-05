package com.example.flo.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.MainActivity
import com.example.flo.data.entities.User
import com.example.flo.data.remote.AuthService
import com.example.flo.databinding.ActivityLogInBinding
import com.example.flo.ui.signup.SignUpOptionActivity

class LogInActivity: AppCompatActivity(), LogInView {

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

//        val songDB = SongDatabase.getInstance(this)!!
//        val user = songDB.userDao().getUser(email, pwd)
//
//        user?.let {
//            Log.d("LOGIN_ACT/GET_USER", "userId : ${user.id}, $user")
////            saveJwt(user.id)
//            startMainActivity()
//        }

        val authService = AuthService()
        authService.setLogInView(this)

        authService.logIn(User(email, pwd, ""))
    }

//    private fun saveJwt(jwt: Int) {
//        val spf = getSharedPreferences("auth", MODE_PRIVATE)
//        val editor = spf.edit()
//
//        editor.putInt("jwt", jwt)
//        editor.apply()
//    }

    private fun saveJwt2(jwt: String) {
        val spf = getSharedPreferences("auth2", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putString("jwt", jwt)
        editor.apply()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onLogInSuccess(isSuccess: Boolean) {
//        when (code) {
//            "COMMON200" -> {
//                saveJwt2(result.accessToken)
//                startMainActivity()
//            }
//        }
        if (isSuccess == true) {
//            saveJwt2(result.accessToken)
            Toast.makeText(this, "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show()
            startMainActivity()
        }
    }

    override fun onLogInFailure() {
        Toast.makeText(this, "회원 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
    }
}