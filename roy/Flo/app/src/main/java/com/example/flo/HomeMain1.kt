package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentHome1Binding
import com.example.flo.databinding.FragmentLockerMusicfileBinding
import com.example.flo.databinding.FragmentVideoBinding

class HomeMain1: Fragment() {

    lateinit var binding: FragmentHome1Binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHome1Binding.inflate(inflater, container, false)

        return binding.root
    }
}