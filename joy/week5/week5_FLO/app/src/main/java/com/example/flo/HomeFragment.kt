package com.example.flo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    private lateinit var homePanelAdapter: HomePanelVpAdapter
    private val slideDelay: Long = 4000

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeAlbum1Iv.setOnClickListener {

            val song = Song(
                binding.mainAlbumTitle1Tv.text.toString(),
                binding.mainAlbumSinger1Tv.text.toString(),
            )

            val bundle = Bundle().apply {
                putString("album_title", song.title)
                putString("album_artist", song.singer)
            }

            val albumFragment = AlbumFragment().apply {
                arguments = bundle
            }

            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm, albumFragment).commitAllowingStateLoss()
        }

        binding.homeAlbum2Iv.setOnClickListener {

            val song = Song(
                binding.mainAlbumTitle2Tv.text.toString(),
                binding.mainAlbumSinger2Tv.text.toString()
            )

            val bundle = Bundle().apply {
                putString("album_title", song.title)
                putString("album_artist", song.singer)
            }

            val albumFragment = AlbumFragment().apply {
                arguments = bundle
            }

            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm, albumFragment).commitAllowingStateLoss()
        }

        binding.homeAlbum3Iv.setOnClickListener {

            val song = Song(
                binding.mainAlbumTitle3Tv.text.toString(),
                binding.mainAlbumSinger3Tv.text.toString()
            )

            val bundle = Bundle().apply {
                putString("album_title", song.title)
                putString("album_artist", song.singer)
            }

            val albumFragment = AlbumFragment().apply {
                arguments = bundle
            }

            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm, albumFragment).commitAllowingStateLoss()
        }

        binding.homeAlbum4Iv.setOnClickListener {

            val song = Song(
                binding.mainAlbumTitle4Tv.text.toString(),
                binding.mainAlbumSinger4Tv.text.toString()
            )

            val bundle = Bundle().apply {
                putString("album_title", song.title)
                putString("album_artist", song.singer)
            }

            val albumFragment = AlbumFragment().apply {
                arguments = bundle
            }

            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm, albumFragment).commitAllowingStateLoss()
        }

        //ViewPager2 설정 (홈 상단 패널)
        homePanelAdapter = HomePanelVpAdapter(this)
        homePanelAdapter.addFragment(HomePanel1Fragment())
        homePanelAdapter.addFragment(HomePanel2Fragment())
        homePanelAdapter.addFragment(HomePanel3Fragment())
        binding.homePanelBackgroundVp.adapter = homePanelAdapter
        binding.homePanelBackgroundVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        //인디케이터 연결
        val indicator = binding.homePanelIndicator
        indicator.setViewPager(binding.homePanelBackgroundVp)

        //데이터 변경 시 인디케이터 업데이트
        homePanelAdapter.registerAdapterDataObserver(indicator.getAdapterDataObserver())

        //자동 슬라이드 기능
        setupAutoSlide()

        //ViewPager2 설정 (홈 배너)
        val bannerAdapter = BannerVpAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        return binding.root
    }

    private fun setupAutoSlide() {
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            var currentItem = 0

            override fun run() {
                if (currentItem == homePanelAdapter.itemCount) {
                    currentItem = 0 // 마지막 페이지에서 처음으로 돌아감
                }
                binding.homePanelBackgroundVp.currentItem = currentItem++
                handler.postDelayed(this, slideDelay) // 슬라이드 딜레이 설정
            }
        }
        handler.postDelayed(runnable, slideDelay) // 초기 딜레이 설정
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::handler.isInitialized) {
            handler.removeCallbacks(runnable)
        }
    }
}