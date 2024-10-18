package com.example.flo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.GeneratedAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHome1Binding
import com.example.flo.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.util.Timer
import kotlin.concurrent.scheduleAtFixedRate

class HomeFragment : Fragment() {

    private val timer = Timer()
    private val handler = Handler(Looper.getMainLooper())
    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeAlbumImgIv1.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("title", binding.titleLilac.text.toString())
            bundle.putString("singer", binding.singerIu.text.toString())

            val albumFragment = AlbumFragment()
            albumFragment.arguments = bundle

            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, AlbumFragment())
                .commitAllowingStateLoss()
        }

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
}