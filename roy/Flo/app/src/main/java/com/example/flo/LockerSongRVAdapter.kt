package com.example.flo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemLockerAlbumBinding
import com.example.flo.databinding.ItemSongBinding
import kotlinx.coroutines.NonDisposableHandle.parent


class LockerSongRVAdapter (private val albumList: ArrayList<Album>) : RecyclerView.Adapter<LockerSongRVAdapter.ViewHolder>() {

    private lateinit var itemClickListener: OnItemClickListener
    private var currentPlayingPosition: Int = RecyclerView.NO_POSITION // 현재 재생 중인 항목의 위치

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LockerSongRVAdapter.ViewHolder {
        val binding: ItemSongBinding = ItemSongBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LockerSongRVAdapter.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.bind(albumList[position])

        if (position == currentPlayingPosition) {
            holder.binding.itemSongPauseIv.visibility = View.VISIBLE
            holder.binding.itemSongPlayIv.visibility = View.GONE
        } else {
            holder.binding.itemSongPauseIv.visibility = View.GONE
            holder.binding.itemSongPlayIv.visibility = View.VISIBLE
        }

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(albumList[position])
        }

        holder.binding.itemSongPlayIv.setOnClickListener {

            if (currentPlayingPosition != RecyclerView.NO_POSITION) {
                notifyItemChanged(currentPlayingPosition)
            }

            currentPlayingPosition = position
            itemClickListener.onPlaySong(albumList[position])

            holder.binding.itemSongPauseIv.visibility = View.VISIBLE
            holder.binding.itemSongPlayIv.visibility = View.GONE
        }

        holder.binding.itemSongPauseIv.setOnClickListener {

            holder.binding.itemSongPauseIv.visibility = View.GONE
            holder.binding.itemSongPlayIv.visibility = View.VISIBLE

            currentPlayingPosition = RecyclerView.NO_POSITION
        }

        holder.binding.itemSongMoreIv.setOnClickListener {
            itemClickListener.onRemoveSong(position)
        }
    }

    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(val binding: ItemSongBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(album: Album){
            binding.itemSongTitleTv.text = album.title
            binding.itemSongSingerTv.text = album.singer
            binding.itemSongImgIv.setImageResource(album.coverImage!!)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(album: Album)
        fun onRemoveSong(position: Int)
        fun onPlaySong(album : Album)
    }


    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    fun removeItem(position: Int){
        albumList.removeAt(position)
        notifyDataSetChanged()
    }

}