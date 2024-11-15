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

class SavedSongFragment : Fragment() {
    lateinit var binding: FragmentLockerSavedsongBinding
    lateinit var songDB: SongDatabase
    private lateinit var songRVAdapter: SavedSongRVAdapter
    private var selectedSongCount = 0 // 선택된 곡 수를 추적하는 변수

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerSavedsongBinding.inflate(inflater, container, false)

        songDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview() {
        binding.lockerSavedSongRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        songRVAdapter = SavedSongRVAdapter()

        songRVAdapter.setMyItemClickListener(object : SavedSongRVAdapter.MyItemClickListener {
            override fun onRemoveSong(songId: Int) {
                songDB.songDao().updateIsLikeById(false, songId)
                selectedSongCount-- // 선택된 곡 수 감소
            }
        })

        binding.lockerSavedSongRecyclerView.adapter = songRVAdapter
        songRVAdapter.addSongs(songDB.songDao().getLikedSongs(true) as ArrayList<Song>)
    }

    fun selectAllSongs() {
        val songs = songDB.songDao().getLikedSongs(true) as ArrayList<Song>
        selectedSongCount = songs.size // 전체 곡 수로 초기화
        songRVAdapter.selectAllSongs(songs)
    }

    fun deselectAllSongs() {
        songRVAdapter.deselectAllSongs() // 어댑터에서 선택 해제 메서드 호출
    }

    fun getSelectedSongCount(): Int {
        return selectedSongCount
    }



}