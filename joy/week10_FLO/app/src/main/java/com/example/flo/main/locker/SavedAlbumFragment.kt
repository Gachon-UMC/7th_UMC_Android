package com.example.flo.main.locker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.data.db.SongDatabase
import com.example.flo.databinding.FragmentSavedAlbumBinding

class SavedAlbumFragment: Fragment() {

    lateinit var binding: FragmentSavedAlbumBinding
    lateinit var albumDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedAlbumBinding.inflate(inflater, container, false)

        albumDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.lockerSavedAlbumListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val albumRVAdapter = SavedAlbumRVAdapter(albumDB.albumDao(), getJwt())

        albumRVAdapter.setMyItemClickListener(object : SavedAlbumRVAdapter.MyItemClickListener {
            override fun onRemoveAlbum(songId: Int) {
                albumDB.albumDao().getLikedAlbums(getJwt())
            }
        })

        binding.lockerSavedAlbumListRv.adapter = albumRVAdapter

        albumRVAdapter.addAlbums(albumDB.albumDao().getLikedAlbums(getJwt()) as ArrayList)
    }

    private fun getJwt(): Int {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt", 0)
    }
}