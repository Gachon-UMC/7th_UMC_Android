package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentSavedsongBinding

class SavedSongFragment : Fragment() {

    lateinit var binding: FragmentSavedsongBinding
    private var savedSongDatas = ArrayList<SavedSong>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedsongBinding.inflate(inflater, container, false)

        savedSongDatas.apply {
            add(SavedSong("라일락", "아이유 (IU)", R.drawable.img_album_cover_5))
            add(SavedSong("Flu", "아이유 (IU)", R.drawable.img_album_cover_5))
            add(SavedSong("Coin", "아이유 (IU)", R.drawable.img_album_cover_5))
            add(SavedSong("봄 안녕 봄", "아이유 (IU)", R.drawable.img_album_cover_5))
            add(SavedSong("Celebrity", "아이유 (IU)", R.drawable.img_album_cover_5))
            add(SavedSong("돌림노래 (feat. DEAN)", "아이유 (IU)", R.drawable.img_album_cover_5))
            add(SavedSong("빈 컵 (Empty Cup)", "아이유 (IU)", R.drawable.img_album_cover_5))
            add(SavedSong("아이와 나의 바다", "아이유 (IU)", R.drawable.img_album_cover_5))
            add(SavedSong("어푸 (Ah puh)", "아이유 (IU)", R.drawable.img_album_cover_5))
            add(SavedSong("에필로그", "아이유 (IU)", R.drawable.img_album_cover_5))
            add(SavedSong("이 지금", "아이유 (IU)", R.drawable.img_album_cover_4))
            add(SavedSong("팔레트 (feat. G-DRAGON)", "아이유 (IU)", R.drawable.img_album_cover_4))
            add(SavedSong("이런 엔딩", "아이유 (IU)", R.drawable.img_album_cover_4))
            add(SavedSong("사랑이 잘 (feat. 오혁)", "아이유 (IU)", R.drawable.img_album_cover_4))
            add(SavedSong("잼잼", "아이유 (IU)", R.drawable.img_album_cover_4))
            add(SavedSong("Black Out", "아이유 (IU)", R.drawable.img_album_cover_4))
            add(SavedSong("마침표", "아이유 (IU)", R.drawable.img_album_cover_4))
            add(SavedSong("밤편지", "아이유 (IU)", R.drawable.img_album_cover_4))
            add(SavedSong("그렇게 사랑은", "아이유 (IU)", R.drawable.img_album_cover_4))
            add(SavedSong("이름에게", "아이유 (IU)", R.drawable.img_album_cover_4))
            add(SavedSong("을의 연애 (WITH 박주원)", "아이유 (IU)", R.drawable.img_album_cover_3))
            add(SavedSong("누구나 비밀은 있다 (feat. GAIN)", "아이유 (IU)", R.drawable.img_album_cover_3))
            add(SavedSong("입술 사이 (50CM)", "아이유 (IU)", R.drawable.img_album_cover_3))
            add(SavedSong("분홍신", "아이유 (IU)", R.drawable.img_album_cover_3))
            add(SavedSong("MODERN TIMES", "아이유 (IU)", R.drawable.img_album_cover_3))
            add(SavedSong("싫은 날", "아이유 (IU)", R.drawable.img_album_cover_3))
            add(SavedSong("OBLIVIATE", "아이유 (IU)", R.drawable.img_album_cover_3))
            add(SavedSong("아이야 나랑 걷자 (feat. 최백호)", "아이유 (IU)", R.drawable.img_album_cover_3))
            add(SavedSong("HAVANA", "아이유 (IU)", R.drawable.img_album_cover_3))
            add(SavedSong("우울시계 (feat. JONGHYUN)", "아이유 (IU)", R.drawable.img_album_cover_3))
            add(SavedSong("기다려", "아이유 (IU)", R.drawable.img_album_cover_3))
            add(SavedSong("Voice Mail (Bonus Track)", "아이유 (IU)", R.drawable.img_album_cover_3))


        }

        val savedSongRVAdapter = SavedSongRVAdapter(savedSongDatas)
        binding.lockerSavedSongListRv.adapter = savedSongRVAdapter
        binding.lockerSavedSongListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        savedSongRVAdapter.setMyItemClickListener(object : SavedSongRVAdapter.MyItemClickListener {
            override fun onItemClick(album: Album) {

            }

            override fun onRemoveSavedSong(position: Int) {
                savedSongRVAdapter.removeItem(position)
            }
        })

        return binding.root
    }
}