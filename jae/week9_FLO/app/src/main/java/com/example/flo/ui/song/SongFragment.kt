package com.example.flo.ui.song

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentSongBinding

class SongFragment : Fragment() {
    lateinit var binding: FragmentSongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater, container, false)

        binding.btnToggleOffIv.setOnClickListener {
            setMixBtnStatus(false)
        }

        binding.btnToggleOnIv.setOnClickListener {
            setMixBtnStatus(true)
        }

        return binding.root
    }

    fun setMixBtnStatus(isPlaying: Boolean) {
        if (isPlaying) {
            binding.btnToggleOffIv.visibility = View.VISIBLE
            binding.btnToggleOnIv.visibility = View.INVISIBLE
        } else {
            binding.btnToggleOffIv.visibility = View.INVISIBLE
            binding.btnToggleOnIv.visibility = View.VISIBLE
        }
    }
}