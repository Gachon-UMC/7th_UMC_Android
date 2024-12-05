package com.example.flo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

import com.example.flo.databinding.ActivityProfileBinding
import com.kakao.sdk.user.Constants
import com.kakao.sdk.user.UserApiClient

class ProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(Constants.TAG, "사용자 정보 요청 실패 $error")
            } else if (user != null) {
                Log.d(Constants.TAG, "사용자 정보 요청 성공 : $user")
                binding.txtNickName.text = user.kakaoAccount?.profile?.nickname
                binding.txtEmail.text = user.kakaoAccount?.email

                val profileUrl = user.kakaoAccount?.profile?.thumbnailImageUrl
                if (profileUrl != null) {
                    Glide.with(this)
                        .load(profileUrl)
                        .into(binding.profileImg)
                } else {
                    binding.profileImg.setImageResource(R.drawable.ic_launcher_background) // 기본 이미지 설정
                }
            }
        }
    }
}
