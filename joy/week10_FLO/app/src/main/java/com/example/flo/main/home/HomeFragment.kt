package com.example.flo.main.home

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
import com.example.flo.MainActivity
import com.example.flo.R
import com.example.flo.data.db.SongDatabase
import com.example.flo.data.entities.Album
import com.example.flo.data.entities.Song
import com.example.flo.databinding.FragmentHomeBinding
import com.example.flo.main.album.AlbumFragment
import com.example.flo.main.album.AlbumRVAdapter
import com.google.gson.Gson

class HomeFragment : Fragment(), AlbumRVAdapter.CommunicationInterface {

    lateinit var binding: FragmentHomeBinding
    private var albumDatas = ArrayList<Album>()
    private lateinit var songDB: SongDatabase

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

//        inputDummyAlbums()

        songDB = SongDatabase.getInstance(requireContext())!!
        albumDatas.addAll(songDB.albumDao().getAlbums())
        Log.d("albumList", albumDatas.toString())

//        albumDatas.apply {
//            add(Album("영화 대도시의 사랑법 (Original Motion Picture Soundtrack)", "Various Artists", R.drawable.img_album_cover_1, listOf(
//                Song("Old Love", "Die Boy", 0, 251, false, "music_old_love", R.drawable.img_album_cover_1)
//            )))
//
//            add(Album("YOUNHA 7th Album 'GROWTH THEORY'", "윤하", R.drawable.img_album_cover_3, listOf(
//                Song("맹그로브", "윤하(YOUNHA)", 0, 259, false, "music_mangrove_tree", R.drawable.img_album_cover_3)
//            )))
//
//            add(Album("Hadestown (Original Broadway Cast Recording)", "Anais Mitchell", R.drawable.img_album_cover_2, listOf(
//                Song("Road to Hell", "André De Sheilds & Hadestown Original Broadway Company", 0, 517, false, "music_road_to_hell", R.drawable.img_album_cover_2)
//            )))
//
//            add(Album("Dear Evan Hason (Original Broadway Cast Recording)", "Various Artists", R.drawable.img_album_cover_4, listOf(
//                Song("Anybody Have a Map?", "Rachel Bay Jones & Jennifer Laura Thompson", 0, 226, false, "music_anybody_have_a_map", R.drawable.img_album_cover_4)
//            )))
//
//            add(Album("Kinky Boots (Original Broadway Cast Recording)", "Original Broadway Cast of Kinky Boots", R.drawable.img_album_cover_5, listOf(
//                Song("Price and Son Theme / The Most Beautiful Thing in the World", "Full Company & Kinky Boots Ensemble", 0, 620, false, "music_price_and_son_theme_the_most_beautiful_thing_in_the_world", R.drawable.img_album_cover_5)
//            )))
//        }

        val albumRVAdapter = AlbumRVAdapter(albumDatas, songDB)
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        albumRVAdapter.setMyItemClickListener(object: AlbumRVAdapter.MyItemClickListener{
            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }

            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.removeItem(position)
            }

            override fun onPlayAlbum(song: Song) {
                sendData(song)
            }
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

//    private fun inputDummyAlbums() {
//        val songDB = SongDatabase.getInstance(requireContext())!!
//        albumDatas.addAll(songDB.albumDao().getAlbums())

//        if (songs.isNotEmpty()) return
//
//        songDB.albumDao().insert(
//            Album(
//                1,
//                "영화 대도시의 사랑법 (Original Motion Picture Soundtrack)",
//                "Various Artists",
//                R.drawable.img_album_cover_1,
//            )
//        )
//
//        songDB.albumDao().insert(
//            Album(
//                2,
//                "YOUNHA 7th Album 'GROWTH THEORY'",
//                "윤하",
//                R.drawable.img_album_cover_3
//            )
//        )
//
//        songDB.albumDao().insert(
//            Album(
//                3,
//                "Hadestown (Original Broadway Cast Recording)",
//                "Anais Mitchell",
//                R.drawable.img_album_cover_2
//            )
//        )
//
//        songDB.albumDao().insert(
//            Album(
//                4,
//                "Dear Evan Hason (Original Broadway Cast Recording)",
//                "Various Artists",
//                R.drawable.img_album_cover_4
//            )
//        )
//
//        songDB.albumDao().insert(
//            Album(
//                5,
//                "Kinky Boots (Original Broadway Cast Recording)",
//                "Original Broadway Cast of Kinky Boots",
//                R.drawable.img_album_cover_5
//            )
//        )

//        val songDBData = songDB.albumDao().getAlbums()
//        Log.d("DB data", songDBData.toString())
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

    override fun sendData(song: Song) {
        if (activity is MainActivity) {
            val activity = activity as MainActivity
            activity.updateMainPlayerCl(song) // MiniPlayer 업데이트
        }
    }
}