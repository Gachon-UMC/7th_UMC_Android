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

//        savedSongDatas.apply {
//            add(SavedSong("Price and Son Theme / The Most Beautiful Thing in the World", "Full Company & Kinky Boots Ensemble", R.drawable.img_album_cover_5))
//            add(SavedSong("Take What You Got", "Andy Kelso & Stark Sands & Kinky Boots Ensemble", R.drawable.img_album_cover_5))
//            add(SavedSong("Land of Lola", "Billy Porter & Kinky Boots Ensemble", R.drawable.img_album_cover_5))
//            add(SavedSong("Charlie's Soliloquy", "Stark Sands", R.drawable.img_album_cover_5))
//            add(SavedSong("Step One", "Stark Sands", R.drawable.img_album_cover_5))
//            add(SavedSong("Sex Is in the Heel", "Billy Porter & Stark Sands & Kinky Boots Ensemble & Tony Ross & Marcus Neville", R.drawable.img_album_cover_5))
//            add(SavedSong("The History of Wrong Guys", "Annaleigh Ashford", R.drawable.img_album_cover_5))
//            add(SavedSong("Not My Father's Son", "Billy Porter & Stark Sands", R.drawable.img_album_cover_5))
//            add(SavedSong("Everybody Say Yeah", "Billy Porter & Stark Sands & Kinky Boots Ensemble", R.drawable.img_album_cover_5))
//            add(SavedSong("What a Woman Wants", "Billy Porter & Kinky Boots Ensemble & Tony Ross & Daniel Stewart Sherman & Marcus Neville", R.drawable.img_album_cover_5))
//            add(SavedSong("In This Corner", "Billy Porter & Kinky Boots Ensemble & Tony Ross & Jennifer Perry & Daniel Stewart Sherman", R.drawable.img_album_cover_5))
//            add(SavedSong("Charlie's Soliloquy (Reprise)", "Stark Sands", R.drawable.img_album_cover_5))
//            add(SavedSong("Soul of a Man", "Stark Sands", R.drawable.img_album_cover_5))
//            add(SavedSong("Hold Me in Your Heart", "Billy Porter", R.drawable.img_album_cover_5))
//            add(SavedSong("Raise You Up / Just Be", "Full Company & Kinky Boots Ensemble", R.drawable.img_album_cover_5))
//        }

        songDB = SongDatabase.getInstance(requireContext())!!

//        val savedSongRVAdapter = SavedSongRVAdapter(savedSongDatas)
//        binding.lockerSavedSongListRv.adapter = savedSongRVAdapter
//        binding.lockerSavedSongListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//
//        savedSongRVAdapter.setMyItemClickListener(object : SavedSongRVAdapter.MyItemClickListener {
//            override fun onItemClick(album: Album) {
//
//            }
//
//            override fun onRemoveSong(songId: Int) {
//                songDB.songDao().updateIsLikeById(false, songId)
//            }
//
////            override fun onRemoveSavedSong(position: Int) {
////                savedSongRVAdapter.removeItem(position)
////            }
//        })

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