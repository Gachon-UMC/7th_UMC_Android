package com.example.flo.main.locker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.data.entities.Album
import com.example.flo.data.entities.Song
import com.example.flo.databinding.ItemSavedSongBinding

class SavedSongRVAdapter() :
    RecyclerView.Adapter<SavedSongRVAdapter.ViewHolder>() {

    private var song: Song = Song()
    private var songs = ArrayList<Song>()

    interface MyItemClickListener {
        fun onItemClick(album: Album)
        //fun onRemoveSavedSong(position: Int)
        fun onRemoveSong(songId: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

//    fun removeItem(position: Int) {
//        savedSongList.removeAt(position)
//        notifyDataSetChanged()
//    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSavedSongBinding = ItemSavedSongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songs[position])
        holder.binding.savedSongBtnMore.setOnClickListener {
            //mItemClickListener.onRemoveSavedSong(position)

            mItemClickListener.onRemoveSong(songs[position].id)
            removeSong(position)
        }
    }

    override fun getItemCount(): Int = songs.size

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs: ArrayList<Song>) {
        this.songs.clear()
        this.songs.addAll(songs)

        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeSong(position: Int) {
        songs.removeAt(position)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemSavedSongBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(song: Song) {
            binding.lockerSongTitleTv.text = song.title
            binding.lockerSongSingerTv.text = song.singer
            binding.lockerSongIv.setImageResource(song.coverImg!!)

            setPlayerStatus(song.isPlaying)

            binding.savedSongPlayBtn.setOnClickListener {
                setPlayerStatus(true)
                song.isPlaying = true
                notifyItemChanged(adapterPosition)
            }

            binding.savedSongPauseBtn.setOnClickListener {
                setPlayerStatus(false)
                song.isPlaying = false
                notifyItemChanged(adapterPosition)
            }

            binding.savedSongBtnMore.setOnClickListener {
                //mItemClickListener.onRemoveSavedSong(adapterPosition)
            }
        }

        fun setPlayerStatus(isPlaying: Boolean) {
            song.isPlaying = isPlaying

            if (isPlaying) {
                binding.savedSongPlayBtn.visibility = View.INVISIBLE
                binding.savedSongPauseBtn.visibility = View.VISIBLE
            } else {
                binding.savedSongPlayBtn.visibility = View.VISIBLE
                binding.savedSongPauseBtn.visibility = View.INVISIBLE
            }
        }
    }
}