package com.example.flo.main.look

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.R
import com.example.flo.databinding.FragmentLookBinding

class LookFragment : Fragment() {

    lateinit var binding: FragmentLookBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLookBinding.inflate(inflater, container, false)

        val chartSongs = listOf(
            ChartSong("Whiplash", "aespa", R.drawable.img_chart_song_album_cover_1, "1"),
            ChartSong("APT.", "로제 (ROSÉ) & Bruno Mars", R.drawable.img_chart_song_album_cover_2, "2"),
            ChartSong("HAPPY", "DAY6 (데이식스)", R.drawable.img_chart_song_album_cover_3, "3"),
            ChartSong("Drowning", "WOODZ", R.drawable.img_chart_song_album_cover_4, "4"),
            ChartSong("UP (KARINA Solo)", "aespa", R.drawable.img_chart_song_album_cover_5, "5"),
        )

        val adapter = ChartSongRVAdapter(chartSongs)
        binding.lookChartRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.lookChartRv.adapter = adapter

        return binding.root
    }
    override fun onStart() {
        super.onStart()
        val windowInsetsController = WindowInsetsControllerCompat(requireActivity().window, requireActivity().window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = true
    }

    override fun onStop() {
        super.onStop()
        val windowInsetsController = WindowInsetsControllerCompat(requireActivity().window, requireActivity().window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = false
    }
}