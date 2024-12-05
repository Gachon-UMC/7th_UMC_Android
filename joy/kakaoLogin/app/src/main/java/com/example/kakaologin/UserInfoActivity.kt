package com.example.kakaologin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kakaologin.databinding.ActivityUserInfoBinding
import com.squareup.picasso.Picasso

class UserInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserInfoBinding
    private var nickname: String? = null
    private var profileImageUrl: String? = null
    private var email: String? = null
    private var onConfirmListener: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent로 전달된 사용자 정보 받기
        nickname = intent.getStringExtra("nickname")
        profileImageUrl = intent.getStringExtra("profileImageUrl")
        email = intent.getStringExtra("email")

        // 사용자 정보 설정
        binding.kakaoLoginNicknameTv.text = nickname
        binding.kakaoLoginEmailTv.text = email ?: "이메일 정보 없음"

        // 프로필 이미지 로드
        if (profileImageUrl != null) {
            Picasso.get().load(profileImageUrl).into(binding.kakaoLoginProfileImgIv)
        }

        // CONFIRM 버튼 클릭 시
        binding.kakaoLoginBtn.setOnClickListener {
            onConfirmListener?.invoke() // 리스너 호출
            finish() // 액티비티 종료
        }
    }

    // Confirm 리스너 설정
    fun setOnConfirmListener(listener: () -> Unit) {
        this.onConfirmListener = listener
    }
}
