package com.example.flo

data class Album(
    var title: String? = "",
    var singer: String? = "",
    var coverImg: Int? = null,
    var songs: List<Song>? = null,
)
