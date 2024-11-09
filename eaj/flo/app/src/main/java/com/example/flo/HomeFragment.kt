package com.example.flo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentHomeBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.IndicatorAdapter

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    private lateinit var pannelAdapter: PannelVPAdapter
    private val slideDelay: Long = 2000

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeAlbumImgIv1.setOnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, AlbumFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

        val bannerAdapter = BannerVPAdapter(this)

        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        pannelAdapter = PannelVPAdapter(this)

        pannelAdapter.addFragment(PannelFragment(R.drawable.img_first_album_default))
        pannelAdapter.addFragment(PannelFragment(R.drawable.img_album_exp2))
        pannelAdapter.addFragment(PannelFragment(R.drawable.img_album_exp3))
        pannelAdapter.addFragment(PannelFragment(R.drawable.img_album_exp4))
        binding.homeViewPager.adapter = pannelAdapter
        binding.homeViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val indicator = binding.homeIndicator
        indicator.setViewPager(binding.homeViewPager)

        setupAutoSlide()

        return binding.root
    }

    private fun setupAutoSlide() {
        var handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            var currentItem = 0

            override fun run() {
                if (currentItem == pannelAdapter.itemCount) {
                    currentItem = 0 // 마지막 페이지에서 처음으로 돌아감
                }
                binding.homeViewPager.currentItem = currentItem++
                handler.postDelayed(this, slideDelay) // 슬라이드 딜레이 설정
            }
        }
        handler.postDelayed(runnable, slideDelay) // 초기 딜레이 설정
    }

}