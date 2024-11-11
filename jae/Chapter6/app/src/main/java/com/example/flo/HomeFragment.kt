package com.example.flo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding
import com.google.gson.Gson

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private var albumDatas = ArrayList<Album>()

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

        albumDatas.apply {
            add(Album("LILAC", "아이유 (IU)", R.drawable.img_album_cover_5, listOf(
                Song("라일락", "아이유 (IU)", 0, 60, false, "music_lilac", R.drawable.img_album_cover_5)
            )))

            add(Album("Pallette", "아이유 (IU)", R.drawable.img_album_cover_4, listOf(
                Song("이 지금", "아이유 (IU)", 0, 60, false, "music_dlwlrma", R.drawable.img_album_cover_4)
            )))

            add(Album("Modern Times", "아이유 (IU)", R.drawable.img_album_cover_3, listOf(
                Song("을의 연애 (WITH 박주원)", "아이유 (IU)", 0, 60, false, "music_loveofb", R.drawable.img_album_cover_3)
            )))

            add(Album("Last Fantasy", "아이유 (IU)", R.drawable.img_album_cover_2, listOf(
                Song("비밀", "아이유 (IU)", 0, 60, false, "music_secret", R.drawable.img_album_cover_2)
            )))

            add(Album("Growing Up", "아이유 (IU)", R.drawable.img_album_cover_1, listOf(
                Song("바라보기", "아이유 (IU)", 0, 60, false, "music_lookingatyou", R.drawable.img_album_cover_1)
            )))
        }

        val albumRVAdapter = AlbumRVAdapter(albumDatas, requireContext())
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        albumRVAdapter.setMyItemClickListener(object: AlbumRVAdapter.MyItemClickListener{
            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }

            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.removeItem(position)
            }

//            override fun onPlayAlbum(album: Album) {
//                sendData(album)
//            }
        })

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

//    override fun sendData(album: Album) {
//        if (activity is MainActivity) {
//            val activity = activity as MainActivity
//            activity.updateMainPlayerCl(album)
//        }
//    }

    private fun changeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()
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