package com.example.flo.ui.main.album

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.data.entities.Album
import com.example.flo.data.entities.Song
import com.example.flo.databinding.ItemAlbumBinding
import com.example.flo.ui.song.SongDatabase

class AlbumRVAdapter(
    private val albumList: ArrayList<Album>,
    private val songDatabase: SongDatabase
) : RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemAlbumBinding =
            ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    fun addItem(album: Album) {
        albumList.add(album)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        albumList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val album = albumList[position]
        holder.bind(album)

        holder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(albumList[position])
        }
        //holder.binding.itemAlbumTitle1Tv.setOnClickListener { mItemClickListener.onRemoveAlbum(position) }

        holder.binding.itemAlbumPlayBtn.setOnClickListener {

            //mItemClickListener.onItemClick(albumList[position])

            val firstSong = getFirstSongInAlbum(album.id)
            firstSong?.let { song ->
                mItemClickListener.onPlayAlbum(song) // 첫 곡을 MainActivity로 전달
            }
        }
    }

    private fun getFirstSongInAlbum(albumId: Int): Song? {
        // SongDatabase에서 해당 albumId에 맞는 첫 번째 곡을 가져옴
        val songs = songDatabase.songDao().getSongsByAlbumId(albumId)
        return songs.firstOrNull() // 첫 번째 곡 반환
    }

    override fun getItemCount(): Int = albumList.size

    interface MyItemClickListener {
        fun onItemClick(album: Album)
        fun onRemoveAlbum(position: Int)
        fun onPlayAlbum(song: Song)
    }

    interface CommunicationInterface {
        fun sendData(song: Song)
    }

    inner class ViewHolder(val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumIv.setImageResource(album.coverImg!!)
        }
    }
}