package com.example.flo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentLockerSavedsongBinding
import com.google.gson.Gson

class SavedSongFragment : Fragment(),CommunicationInterface  {
    lateinit var binding: FragmentLockerSavedsongBinding
    private var albumDatas = ArrayList<Album>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLockerSavedsongBinding.inflate(inflater, container, false)

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

        val lockerSongRVAdapter = LockerSongRVAdapter(albumDatas)
        binding.lockerSavedSongRecyclerView.adapter = lockerSongRVAdapter
        binding.lockerSavedSongRecyclerView.layoutManager = LinearLayoutManager(requireActivity())


        lockerSongRVAdapter.setItemClickListener(object : LockerSongRVAdapter.OnItemClickListener {

            override fun onItemClick(album: Album) {
                changeSongFragment(album)
            }

            override fun onRemoveSong(position: Int) {
                lockerSongRVAdapter.removeItem(position)
            }

            override fun onPlaySong(album: Album) {
                sendData(album)
            }

        })

        return binding.root
    }

    override fun sendData(album: Album) {
        if (activity is MainActivity) {
            val activity = activity as MainActivity
            activity.updateMainPlayerCl(album)
        }
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview() {
        binding.lockerSavedSongRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }




    private fun changeSongFragment(album: Album) {
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


}