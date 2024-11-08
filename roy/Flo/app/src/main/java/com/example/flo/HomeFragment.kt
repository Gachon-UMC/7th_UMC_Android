package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.GeneratedAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHome1Binding
import com.example.flo.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import java.util.Timer
import kotlin.concurrent.scheduleAtFixedRate

class HomeFragment : Fragment(),CommunicationInterface  {

    private val timer = Timer()
    private val handler = Handler(Looper.getMainLooper())
    lateinit var binding: FragmentHomeBinding
    private var albumDatas = ArrayList<Album>()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentHomeBinding.inflate(inflater, container, false)


        albumDatas.apply {
            add(Album("APT.", "로제 & Bruno Mars", R.drawable.img_album_apt, R.raw.music_apt))
            add(Album("HAPPY", "데이식스 (DAY6)", R.drawable.img_album_happy, R.raw.music_happy))
            add(Album("POWER", "G-DRAGON", R.drawable.img_album_power, R.raw.music_power))
            add(Album("내 이름 맑음", "QWER", R.drawable.img_album_qwer, R.raw.music_blossom))
            add(Album("Whiplash", "에스파 (AESPA)", R.drawable.img_album_whiplash, R.raw.music_whiplash))
            add(Album("Welcome to the Show", "데이식스 (DAY6)", R.drawable.img_album_happy, R.raw.music_welcometotheshow))
            add(Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp, R.raw.music_butter))
            add(Album("Lilac", "아이유 (IU)", R.drawable.img_album_exp2, R.raw.music_lilac))
            add(Album("Next Level", "에스파 (AESPA)", R.drawable.img_album_exp3, R.raw.music_next))
            add(Album("Boy with Luv", "방탄소년단 (BTS)", R.drawable.img_album_exp4, R.raw.music_boy))
            add(Album("BBoom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5, R.raw.music_bboom))
            add(Album("Weekend", "태연 (Tae Yeon)", R.drawable.img_album_exp6, R.raw.music_blossom))
        }


        val albumRVAdapter = AlbumRVAdapter(albumDatas)
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
        binding.homeTodayMusicAlbumRv.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

        albumRVAdapter.setItemClickListener(object : AlbumRVAdapter.OnItemClickListener {
            override fun onItemClick(album: Album) {
                changeToAlbumFragment(album)
            }

            override fun onPlayAlbum(album: Album) {
                sendData(album)
            }
        })


        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))

        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL


        val HomeAdapter = HomeVPAdapter(this)
        binding.homeIndicatorVp.adapter = HomeAdapter
        binding.homeIndicatorVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.indicator.setViewPager(binding.homeIndicatorVp)

        startAutoSlide(HomeAdapter)
        return binding.root
    }


    private fun changeToAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumToJson = gson.toJson(album)
                    putString("album", albumToJson)
                }
            })
            .commitAllowingStateLoss()
    }


    private fun startAutoSlide(adapter: HomeVPAdapter) {
        timer.scheduleAtFixedRate(3000, 3000) {
            handler.post {
                val nextItem = binding.homeIndicatorVp.currentItem + 1
                if (nextItem < adapter.itemCount) {
                    binding.homeIndicatorVp.currentItem = nextItem
                } else {
                    binding.homeIndicatorVp.currentItem = 0
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel()
    }

    override fun sendData(album: Album) {
        if (activity is MainActivity) {
            val activity = activity as MainActivity
            activity.updateMainPlayerCl(album)
        }

    }

}