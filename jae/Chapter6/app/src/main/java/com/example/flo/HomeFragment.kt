package com.example.flo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
    private lateinit var songDB: SongDatabase
    private lateinit var songDao: SongDao
    private var albumDatas = ArrayList<Album>()

    private lateinit var homePanelAdapter: HomePanelVPAdapter
    private val slideDelay: Long = 4000

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        songDB = SongDatabase.getInstance(requireContext())!!
        songDao = songDB.songDao()

        // 더미 데이터 삽입
        inputDummyAlbums()

        // 데이터베이스에서 앨범 리스트 가져오기
        albumDatas.addAll(songDB.albumDao().getAlbums())
        Log.d("albumlist", albumDatas.toString()) // 데이터 확인용 로그

        val albumRVAdapter = AlbumRVAdapter(albumDatas, requireContext(), songDB.songDao())

        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter

        albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener {
            override fun onItemClick(album: Album) {
                // 앨범 ID로 곡 목록 가져오기
                val songs = songDao.getSongsByAlbumId(album.id)
                if (songs.isNotEmpty()) {
                    val firstSong = songs[0]
                    // 미니 플레이어 업데이트
                    (activity as MainActivity).setMiniPlayer(firstSong)
                    // 첫 번째 곡 재생
                    (activity as MainActivity).setPlayerStatus(true)
                }
                // 프래그먼트 변경
                changeAlbumFragment(album)
            }

            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.removeItem(position)
            }
        })

        binding.homeTodayMusicAlbumRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        //ViewPager2 설정 (홈 상단 패널)
        homePanelAdapter = HomePanelVPAdapter(this)
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

    private fun inputDummyAlbums() {
        val songDB = SongDatabase.getInstance(requireActivity())!!
        val songs = songDB.albumDao().getAlbums()

        if (songs.isNotEmpty()) return

        songDB.albumDao().insert(
            Album(5, "IU 5th Album 'LILAC'", "아이유 (IU)", R.drawable.img_album_cover_5)
        )
        songDB.albumDao().insert(
            Album(4, "Palette", "아이유 (IU)", R.drawable.img_album_cover_4)
        )
        songDB.albumDao().insert(
            Album(3, "Modern Times", "아이유 (IU)", R.drawable.img_album_cover_3)
        )
        songDB.albumDao().insert(
            Album(2, "Last Fantasy", "아이유 (IU)", R.drawable.img_album_cover_2)
        )
        songDB.albumDao().insert(
            Album(1, "Growing Up", "아이유 (IU)", R.drawable.img_album_cover_1)
        )

        val songDBData = songDB.albumDao().getAlbums()
        Log.d("DB data", songDBData.toString())
    }

    private fun changeAlbumFragment(album: Album) {
        (activity as MainActivity).supportFragmentManager.beginTransaction()
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