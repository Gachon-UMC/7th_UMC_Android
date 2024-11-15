package com.example.flo

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemAlbumBinding

class AlbumRVAdapter(
    private val albumList: ArrayList<Album>,
    private val context: Context,
    private val songDao: SongDao
) : RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {

    interface MyItemClickListener {
        fun onItemClick(album: Album)
        fun onRemoveAlbum(position: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumRVAdapter.ViewHolder {
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
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

    override fun onBindViewHolder(holder: AlbumRVAdapter.ViewHolder, position: Int) {
        val album = albumList[position]
        holder.bind(album)

        holder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(albumList[position])
        }
        //holder.binding.itemAlbumTitle1Tv.setOnClickListener { mItemClickListener.onRemoveAlbum(position) }

        holder.binding.itemAlbumPlayBtn.setOnClickListener {

//            mItemClickListener.onItemClick(albumList[position])
            val songs = songDao.getSongs().filter { it.albumIdx == album.id }  // albumIdx로 Song 테이블에서 노래 필터링

            val firstSong = songs.firstOrNull()

            if (firstSong != null) {
                (context as? MainActivity)?.setMiniPlayer(firstSong)


//                val intent = Intent(context, SongActivity::class.java)
//                intent.putExtra("title", firstSong.title)
//                intent.putExtra("singer", firstSong.singer)
//                intent.putExtra("second", firstSong.second)
//                intent.putExtra("playTime", firstSong.playTime)
//                intent.putExtra("isPlaying", firstSong.isPlaying)
//                intent.putExtra("music", firstSong.music)
//                intent.putExtra("coverImg", firstSong.coverImg)
//                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(val binding:ItemAlbumBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumIv.setImageResource(album.coverImg!!)
        }
    }
}