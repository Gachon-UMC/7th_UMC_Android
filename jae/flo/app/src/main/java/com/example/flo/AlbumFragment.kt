package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator

class AlbumFragment : Fragment() {

    lateinit var binding: FragmentAlbumBinding
    private val information = arrayListOf("수록곡", "상세정보", "영상")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        binding.albumAlbumIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }

        binding.albumBackIv.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val albumAdapter = AlbumVPAdapter(this)
        binding.albumContentVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp){
            tab, position ->
            tab.text = information[position]
        }.attach()

        binding.albumLikeOffIv.setOnClickListener {
            if (binding.albumLikeOffIv.drawable.constantState == resources.getDrawable(R.drawable.ic_my_like_off).constantState) {
                binding.albumLikeOffIv.setImageResource(R.drawable.ic_my_like_on)
            }
            else {
                binding.albumLikeOffIv.setImageResource(R.drawable.ic_my_like_off)
            }
        }

        return binding.root
    }
}