package com.example.flo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentSavedsongBinding

class SavedSongFragment : Fragment() {
    lateinit var binding: FragmentSavedsongBinding
    lateinit var songDB: SongDatabase
    private var isSelectingAll = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedsongBinding.inflate(inflater, container, false)

        songDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
        initSelectAllButton()
    }

    private fun initRecyclerview(){
        binding.lockerSavedSongListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val songRVAdapter = SavedSongRVAdapter()

        songRVAdapter.setMyItemClickListener(object : SavedSongRVAdapter.MyItemClickListener{
            override fun onRemoveSong(songId: Int) {
                songDB.songDao().updateIsLikeById(false,songId)
            }

        })

        binding.lockerSavedSongListRv.adapter = songRVAdapter

        songRVAdapter.addSongs(songDB.songDao().getLikedSongs(true) as ArrayList<Song>)
    }
    private fun initSelectAllButton() {
        // 전체선택 버튼 클릭 시 처리
        binding.savedSongWholeSelectBtn.setOnClickListener {
            if (isSelectingAll) {
                showDeselectionDialog()
            } else {
                showSelectionDialog()
            }
        }
    }

    private fun showSelectionDialog() {
        // 전체선택 상태로 변경
        isSelectingAll = true
        binding.savedSongWholeSelectTv.text = "선택해제"
        binding.savedSongWholeSelectTv.setTextColor(resources.getColor(R.color.select_color))
        binding.savedSongWholeSelectBtn.setImageResource(R.drawable.btn_playlist_select_on)

        // BottomSheet 띄우기
        val bottomSheetFragment = BottomSheetFragment()
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
    }

    private fun showDeselectionDialog() {
        // 선택 해제 상태로 변경
        isSelectingAll = false
        binding.savedSongWholeSelectTv.text = "전체선택"
        binding.savedSongWholeSelectTv.setTextColor(resources.getColor(R.color.black))
        binding.savedSongWholeSelectBtn.setImageResource(R.drawable.btn_playlist_select_off)
    }
}