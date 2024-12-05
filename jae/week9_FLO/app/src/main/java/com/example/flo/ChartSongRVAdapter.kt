package com.example.flo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.data.entities.ChartSong
import com.example.flo.databinding.ItemChartSongBinding

class ChartSongRVAdapter(private val chartSong: List<ChartSong>) : RecyclerView.Adapter<ChartSongRVAdapter.ChartSongViewHolder>() {

    class ChartSongViewHolder(val binding: ItemChartSongBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartSongViewHolder {
        val binding = ItemChartSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChartSongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChartSongViewHolder, position: Int) {
        val chartSong = chartSong[position]
        holder.binding.itemChartSongTitleTv.text = chartSong.title
        holder.binding.itemChartSongSingerTv.text = chartSong.singer
        holder.binding.itemChartSongAlbumCoverIv.setImageResource(chartSong.coverImg!!)
        holder.binding.itemChartSongNumTv.text = chartSong.num
    }

    override fun getItemCount(): Int  = chartSong.size

}