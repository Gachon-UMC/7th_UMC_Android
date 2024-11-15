package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        songDB = SongDatabase.getInstance(requireContext())!!

        view.findViewById<Button>(R.id.deleteButton).setOnClickListener {
            dismiss()

            // 모든 항목의 좋아요 취소
            val likedSongs = songDB.songDao().getLikedSongs(true)
            likedSongs.forEach { song ->
                songDB.songDao().updateIsLikeById(false, song.id)
            }

            // 좋아요 취소 후 처리할 로직 (예: UI 갱신)
        }
    }
}
