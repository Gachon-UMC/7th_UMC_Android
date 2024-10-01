package com.example.week_2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import com.example.week_2.databinding.FragmentCatagoryBinding
import com.example.week_2.databinding.FragmentSnapBinding

class Fragment2: Fragment() {
    private lateinit var binding: FragmentSnapBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.slide_right)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentSnapBinding.inflate(inflater,container,false)
        return binding.root
    }
}