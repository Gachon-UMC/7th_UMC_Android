package com.example.flo.login

import android.content.Intent
import android.media.session.MediaSession.Token
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.MainActivity
import com.example.flo.signup.SignUpOptionActivity
import com.example.flo.data.entities.User
import com.example.flo.data.remote.AuthService
import com.example.flo.databinding.ActivityLogInBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient

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

        binding.logInKakaoBtn.setOnClickListener {
            kakaoLogin()
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

    private fun kakaoLogin() {
        val callback: (OAuthToken?, Throwable?) -> Unit = {token, error ->
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                    }
                    else -> { // Unknown
                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else if (token != null) {
                Log.d("token", token.accessToken)
                Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }

        if(UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
            UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
        }else{
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }
}