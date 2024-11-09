package com.example.flo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.AlbumRVAdapter.MyItemClickListener
import com.example.flo.databinding.ItemSavedSongBinding

class SavedSongRVAdapter(private var savedSongList: ArrayList<SavedSong>) : RecyclerView.Adapter<SavedSongRVAdapter.ViewHolder>() {

    private var song: Song = Song()

    interface MyItemClickListener {
        fun onItemClick(album: Album)
        fun onRemoveSavedSong(position: Int)
    }

    private lateinit var mItemClickListener: SavedSongRVAdapter.MyItemClickListener

    fun setMyItemClickListener(itemClickListener: SavedSongRVAdapter.MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): SavedSongRVAdapter.ViewHolder {
        val binding: ItemSavedSongBinding = ItemSavedSongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    fun removeItem(position: Int) {
        savedSongList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = savedSongList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(savedSongList[position])
        holder.binding.savedSongBtnMore.setOnClickListener { mItemClickListener.onRemoveSavedSong(position) }
    }

    inner class ViewHolder(val binding: ItemSavedSongBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(savedSong: SavedSong) {
            binding.lockerSongTitleTv.text = savedSong.title
            binding.lockerSongSingerTv.text = savedSong.singer
            binding.lockerSongIv.setImageResource(savedSong.coverImg!!)

            setPlayerStatus(savedSong.isPlaying)

            binding.savedSongPlayBtn.setOnClickListener {
                setPlayerStatus(true)
                savedSong.isPlaying = true
                notifyItemChanged(adapterPosition)
            }

            binding.savedSongPauseBtn.setOnClickListener {
                setPlayerStatus(false)
                savedSong.isPlaying = false
                notifyItemChanged(adapterPosition)
            }

            binding.savedSongBtnMore.setOnClickListener {
                mItemClickListener.onRemoveSavedSong(adapterPosition)
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