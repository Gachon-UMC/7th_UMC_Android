package com.example.flo.ui.main.savedlabum

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.data.entities.Album
import com.example.flo.databinding.ItemSavedAlbumBinding
import com.example.flo.ui.main.album.AlbumDao

class SavedAlbumRVAdapter(
    private val albumDao: AlbumDao,
    private val userId: Int) :
    RecyclerView.Adapter<SavedAlbumRVAdapter.ViewHolder>() {

    private val albums = ArrayList<Album>()

    interface MyItemClickListener {
        fun onRemoveAlbum(songId: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSavedAlbumBinding = ItemSavedAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(albums[position])

        holder.binding.lockerAlbumIv.setOnClickListener {
            val albumId = albums[position].id

            albumDao.disLikedAlbum(userId, albumId)

            removeAlbum(position)
//            mItemClickListener.onRemoveAlbum(albums[position].id)

            mItemClickListener.onRemoveAlbum(albumId)
        }
    }

    override fun getItemCount(): Int = albums.size

    @SuppressLint("NotifyDataSetChanged")
    fun addAlbums(albums: ArrayList<Album>) {
        this.albums.clear()
        this.albums.addAll(albums)

        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeAlbum(position: Int){
        albums.removeAt(position)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemSavedAlbumBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(album: Album) {
            binding.lockerAlbumTitleTv.text = album.title
            binding.lockerAlbumArtistTv.text = album.singer
            binding.lockerAlbumIv.setImageResource(album.coverImg!!)

//            binding.lockerAlbumIv.setOnClickListener {
//                mItemClickListener.onRemoveAlbum(adapterPosition)
//            }
        }
    }
}