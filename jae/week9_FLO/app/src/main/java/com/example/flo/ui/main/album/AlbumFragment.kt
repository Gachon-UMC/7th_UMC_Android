package com.example.flo.ui.main.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.core.view.WindowInsetsControllerCompat
import com.example.flo.ui.main.home.HomeFragment
import com.example.flo.MainActivity
import com.example.flo.R
import com.example.flo.data.entities.Album
import com.example.flo.data.entities.Like
import com.example.flo.databinding.FragmentAlbumBinding
import com.example.flo.ui.song.SongDatabase
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment : Fragment() {

    lateinit var binding: FragmentAlbumBinding
    private var gson: Gson = Gson()

    private var information = arrayListOf("수록곡", "상세정보", "영상")

    private var isLiked : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        val albumJson = arguments?.getString("album")
        val album = gson.fromJson(albumJson, Album::class.java)

        isLiked = isLikedAlbum(album.id)

        setInit(album)
        initViewPager()
        setClickListener(album)

        return binding.root
    }

    private fun setInit(album: Album) {
        binding.albumCoverIv.setImageResource(album.coverImg!!)
        binding.albumTitleTv.text = album.title.toString()
        binding.albumArtistTv.text = album.singer.toString()

        if (isLiked) {
            binding.icMyLikeOffIv.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.icMyLikeOffIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun getJwt(): Int {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt", 0)
    }

    private fun likeAlbum(userId : Int, albumId: Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val like = Like(userId, albumId)

        songDB.albumDao().likeAlbum(like)
    }

    private fun disLikeAlbum(userId: Int, albumId: Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
//        val userId = getJwt()

        songDB.albumDao().disLikedAlbum(userId, albumId)
    }

    private fun isLikedAlbum(albumId: Int) : Boolean {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        val likeId = songDB.albumDao().isLikedAlbum(userId, albumId)

        return likeId != null
    }

    private fun setClickListener(album: Album) {

        val userId = getJwt()

        binding.icMyLikeOffIv.setOnClickListener {

            if (isLiked) {
                binding.icMyLikeOffIv.setImageResource(R.drawable.ic_my_like_off)
                disLikeAlbum(userId, album.id)
            } else {
                binding.icMyLikeOffIv.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId, album.id)
            }

            isLiked = ! isLiked
        }

        binding.btnArrowBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment()).commitAllowingStateLoss()
        }
    }

    private fun initViewPager() {
        val albumAdapter = AlbumVpAdapter(this)
        binding.albumContentVp.adapter = albumAdapter

        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp){
                tab, position ->
            tab.text = information[position]
        }.attach()
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