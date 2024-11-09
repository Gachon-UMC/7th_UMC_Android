package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.core.view.WindowInsetsControllerCompat
import com.example.flo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment : Fragment() {

    lateinit var binding: FragmentAlbumBinding
    private var gson: Gson = Gson()

    private var information = arrayListOf("수록곡", "상세정보", "영상")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        val albumJson = arguments?.getString("album")
        val album = gson.fromJson(albumJson, Album::class.java)
        setInit(album)

        binding.btnArrowBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment()).commitAllowingStateLoss()
        }

//        val albumTitle = arguments?.getString("album_title")
//        val artistName = arguments?.getString("album_artist")
//
//        Log.d("AlbumFragment", "Album Title: $albumTitle, Artist Name: $artistName")
//
//        if (albumTitle != null && artistName != null) {
//            binding.albumTitleTv.text = albumTitle
//            binding.albumArtistTv.text = artistName
//        }

        val albumAdapter = AlbumVpAdapter(this)
        binding.albumContentVp.adapter = albumAdapter

        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp){
            tab, position ->
            tab.text = information[position]
        }.attach()

        return binding.root
    }

    private fun setInit(album: Album) {
        binding.albumCoverIv.setImageResource(album.coverImg!!)
        binding.albumTitleTv.text = album.title.toString()
        binding.albumArtistTv.text = album.singer.toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.albumTitleTv.isSelected = true
        binding.albumTitleTv.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()
        val windowInsetsController = WindowInsetsControllerCompat(requireActivity().window, requireActivity().window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = true
    }

    override fun onStop() {
        super.onStop()
        val windowInsetsController = WindowInsetsControllerCompat(requireActivity().window, requireActivity().window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = false
    }
}