package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentHomepanel3Binding

class HomePanel3Fragment : Fragment() {
    lateinit var binding: FragmentHomepanel3Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomepanel3Binding.inflate(inflater, container, false)

        return binding.root
    }
}