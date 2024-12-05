package com.example.kakaologin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kakaologin.databinding.ActivityMainBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClickListener()

        val keyHash = Utility.getKeyHash(this)
        Log.d("KeyHash", keyHash ?: "Key hash not found")
    }

    private fun initClickListener() {

        binding.kakaoLoginIv.setOnClickListener {
            kakaoLogin()
        }
    }

    private fun kakaoLogin() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
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
                val spf = getSharedPreferences("auth3", MODE_PRIVATE)
                val editor = spf.edit()
                editor.putString("kakao_token", token.accessToken)
                editor.apply()
                //           Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                //         val intent = Intent(this, MainActivity::class.java)
                //       startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                //     finish()

                getKakaoUserInfo()
            }
        }

        //    if(UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
        //        UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
        //    }else{
        //        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        //    }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
        } else {
            // 이메일 권한을 요청하면서 로그인
            UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                if (error != null) {
                    Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                } else if (token != null) {
                    // 이메일 권한이 이미 동의된 경우 정보 가져오기
                    getKakaoUserInfo()
                }
            }
        }
    }

    private fun getKakaoUserInfo() {
        UserApiClient.instance.me { kakaoUser, error ->
            if (error != null) {
                Toast.makeText(this, "사용자 정보 요청 실패", Toast.LENGTH_SHORT).show()
            } else if (kakaoUser != null) {
                // 사용자 정보를 가져왔으므로 팝업창을 띄웁니다
                showUserInfoPopup(kakaoUser)
            }
        }
    }

    private fun showUserInfoPopup(kakaoUser: User) {
        val nickname = kakaoUser.kakaoAccount?.profile?.nickname
        val profileImageUrl = kakaoUser.kakaoAccount?.profile?.profileImageUrl
        val email = kakaoUser.kakaoAccount?.email

        // 사용자 정보를 팝업창으로 띄우기
//        val dialog = UserInfoDialog(this, nickname, profileImageUrl, email)
//        dialog.setOnConfirmListener {
//            // CONFIRM 버튼 클릭 시 메인 액티비티로 이동
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()  // 현재 액티비티 종료
//        }
//        dialog.show()

        val intent = Intent(this, UserInfoActivity::class.java).apply {
            putExtra("nickname", nickname)
            putExtra("profileImageUrl", profileImageUrl)
            putExtra("email", email)
        }
        startActivity(intent)
        finish()
    }
}