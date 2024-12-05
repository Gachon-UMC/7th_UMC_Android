package com.example.flo.main.locker

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.example.flo.login.LogInActivity
import com.example.flo.MainActivity
import com.example.flo.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.kakao.sdk.user.UserApiClient

class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding

    private var information = arrayListOf("저장한 곡","저장한 앨범", "음악파일")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        val lockerAdapter = LockerVpAdapter(this)
        binding.lockerContentVp.adapter = lockerAdapter

        TabLayoutMediator(binding.lockerContentTb, binding.lockerContentVp){
                tab, position ->
            tab.text = information[position]
        }.attach()

        binding.lockerLoginTv.setOnClickListener {
            startActivity(Intent(activity, LogInActivity::class.java))
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val windowInsetsController = WindowInsetsControllerCompat(requireActivity().window, requireActivity().window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = true

        initViews()
    }

    override fun onStop() {
        super.onStop()
        val windowInsetsController = WindowInsetsControllerCompat(requireActivity().window, requireActivity().window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = false
    }

    private fun getJwt(): Int {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt", 0)
    }

    private fun getKakaoToken(): String? {
        val spf = activity?.getSharedPreferences("auth3", AppCompatActivity.MODE_PRIVATE)
        return spf?.getString("kakao_token", null)
    }

    private fun initViews() {
        val jwt : Int = getJwt()
        val kakaoToken = getKakaoToken()

   //     if (jwt == 0) {
    //        binding.lockerLoginTv.text = "로그인"
    //        binding.lockerLoginTv.setOnClickListener {
    //            startActivity(Intent(activity, LogInActivity::class.java))
    //        }
    //    } else {
    //        binding.lockerLoginTv.text = "로그아웃"
    //        binding.lockerLoginTv.setOnClickListener {
    //            logOut()
    //            startActivity(Intent(activity, MainActivity::class.java))
     //       }
      //  }

        // 카카오 로그인 처리
        if (!kakaoToken.isNullOrEmpty()) {
            // 카카오 로그인된 상태
            binding.lockerLoginTv.text = "로그아웃"
            binding.lockerLoginTv.setOnClickListener {
                kakaoLogOut()
            }
        }
        // 일반 로그인 처리
        else if (jwt != 0) {
            // 일반 로그인된 상태
            binding.lockerLoginTv.text = "로그아웃"
            binding.lockerLoginTv.setOnClickListener {
                logOut()
            }
        }
        // 로그인 안 된 상태
        else {
            binding.lockerLoginTv.text = "로그인"
            binding.lockerLoginTv.setOnClickListener {
                startActivity(Intent(activity, LogInActivity::class.java))
            }
        }
    }

    private fun kakaoLogOut() {
        // 카카오 로그아웃
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Toast.makeText(activity, "로그아웃 실패", Toast.LENGTH_SHORT).show()
            } else {
                // 카카오 로그아웃 후 UI 갱신
                val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
                val editor = spf?.edit()
                editor?.remove("kakao_token")  // SharedPreferences에서 카카오 토큰 제거
                editor?.apply()
                binding.lockerLoginTv.text = "로그인"
                Toast.makeText(activity, "카카오 로그아웃 성공", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun logOut() {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val editor = spf!!.edit()
        editor.remove("jwt")
        editor.apply()
    }
}