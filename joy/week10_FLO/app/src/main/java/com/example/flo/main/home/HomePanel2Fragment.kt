package com.example.flo.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentHomepanel2Binding

class HomePanel2Fragment : Fragment() {
    lateinit var binding: FragmentHomepanel2Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomepanel2Binding.inflate(inflater, container, false)

        return binding.root
    }
}