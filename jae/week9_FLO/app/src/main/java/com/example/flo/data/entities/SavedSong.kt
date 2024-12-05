package com.example.flo.data.entities

data class SavedSong(
    var title: String? = "",
    var singer: String? = "",
    var coverImg: Int? = null,
    var isPlaying: Boolean = false,
    var songs: ArrayList<Song>? = null
)