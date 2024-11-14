package com.example.flo

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flo.databinding.FragmentBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment : BottomSheetDialogFragment() {
    lateinit var binding: FragmentBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomSheetView1.setOnClickListener {
            CustomSnackbar.make(binding.root, "재생을 시작합니다.").show()
        }

        binding.bottomSheetView2.setOnClickListener {
            CustomSnackbar.make(binding.root, "곡이 음악 재생목록에 담겼습니다.").show()
        }

        binding.bottomSheetView3.setOnClickListener {
            CustomSnackbar.make(binding.root, "선택한 곡을 리스트에 추가합니다.").show()
        }

        binding.bottomSheetView4.setOnClickListener {
            CustomSnackbar.make(binding.root, "곡을 저장합니다.").show()
        }

        binding.bottomSheetView5.setOnClickListener {

            parentFragmentManager.setFragmentResult("deleteRequestKey", Bundle())
            dismiss()
        }
    }
}