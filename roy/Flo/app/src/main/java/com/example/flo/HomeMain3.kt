package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentHome3Binding

class HomeMain3: Fragment() {

    lateinit var binding: FragmentHome3Binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHome3Binding.inflate(inflater, container, false)

        return binding.root
    }
}