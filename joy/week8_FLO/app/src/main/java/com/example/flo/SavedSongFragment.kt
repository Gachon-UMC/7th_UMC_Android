package com.example.flo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentSavedsongBinding

class SavedSongFragment : Fragment() {

    lateinit var binding: FragmentSavedsongBinding
    //private var savedSongDatas = ArrayList<SavedSong>()
    lateinit var songDB: SongDatabase

    private var isAllSelected = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedsongBinding.inflate(inflater, container, false)

        songDB = SongDatabase.getInstance(requireContext())!!

        val bottomSheetFragment = BottomSheetFragment()

        binding.savedSongWholeSelectTv.setOnClickListener {

            if (!isAllSelected) {
                bottomSheetFragment.show(requireFragmentManager(), "BottomSheetDialog")
                setAllSelectedState(true)
            } else {
                setAllSelectedState(false)
            }
        }

        parentFragmentManager.setFragmentResultListener("deleteRequestKey", viewLifecycleOwner) {
                _, _ -> setAllSelectedState(false)
        }

        return binding.root
    }

    private fun setAllSelectedState(selected: Boolean) {
        isAllSelected = selected
        if (selected) {
            binding.savedSongWholeSelectBtn.setImageResource(R.drawable.btn_playlist_select_on)
            binding.savedSongWholeSelectTv.text = "선택해제"
            binding.savedSongWholeSelectTv.setTextColor(resources.getColor(R.color.select_color))
        } else {
            binding.savedSongWholeSelectTv.text = "전체선택"
            binding.savedSongWholeSelectTv.setTextColor(resources.getColor(R.color.black))
            binding.savedSongWholeSelectBtn.setImageResource(R.drawable.btn_playlist_select_off)
        }
    }

    override fun onStart() {
        super.onStart()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.lockerSavedSongListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val songRVAdapter = SavedSongRVAdapter()

        songRVAdapter.setMyItemClickListener(object : SavedSongRVAdapter.MyItemClickListener {
            override fun onItemClick(album: Album) {

            }

            override fun onRemoveSong(songId: Int) {
                songDB.songDao().updateIsLikeById(false, songId)
            }
        })

        binding.lockerSavedSongListRv.adapter = songRVAdapter

        songRVAdapter.addSongs(songDB.songDao().getLikedSongs(true) as ArrayList<Song>)
    }
}