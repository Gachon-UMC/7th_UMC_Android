package com.example.flo

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator

class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding
    private val information = arrayListOf("저장한곡", "음악파일", "저장앨범")
    private var isAllSelected = false



    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        val lockerAdapter = LockerVPAdapter(this)
        binding.lockerContentVp.adapter = lockerAdapter
        TabLayoutMediator(binding.lockerContentTb, binding.lockerContentVp) { tab, position ->
            tab.text = information[position]
        }.attach()

        val bottomSheetFragment = BottomSheetFragment()


        bottomSheetFragment.setOnDeleteClickListener {
            // 선택 해제 및 텍스트 변경
            isAllSelected = false
            binding.lockerSelectAllTv.text = "전체 선택"
            binding.lockerSelectAllTv.setTextColor(R.color.select_color)
        }


        binding.lockerSelectAllTv.setOnClickListener {
            // SavedSongFragment 인스턴스를 가져와서 selectAllSongs 호출
            val savedSongFragment = lockerAdapter.getFragment(0) as SavedSongFragment

            if (isAllSelected) {
                savedSongFragment.deselectAllSongs() // 선택 해제 메서드 호출
                binding.lockerSelectAllTv.text = "전체 선택" // 버튼 텍스트 변경
                binding.lockerSelectAllTv.setTextColor(R.color.select_color)

            } else {
                savedSongFragment.selectAllSongs() // 전체 선택 메서드 호출
                binding.lockerSelectAllTv.text = "선택 해제" // 버튼 텍스트 변경
                binding.lockerSelectAllTv.setTextColor(R.color.gray_color)
            }

            isAllSelected = !isAllSelected // 선택 상태 토글
            bottomSheetFragment.show(requireFragmentManager(), "BottomSheetDialog")


            bottomSheetFragment.dialog?.setOnDismissListener {
                isAllSelected = false
                binding.lockerSelectAllTv.text = "전체 선택"
                binding.lockerSelectAllTv.setTextColor(R.color.select_color)
            }

        }

        return binding.root
    }


}


