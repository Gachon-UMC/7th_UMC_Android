package com.example.flo

import androidx.room.*


@Dao
interface AlbumDao {
    @Insert
    fun insert(album: Album)

    @Update
    fun update(album: Album)

    @Delete
    fun delete(album: Album)

    @Query("SELECT * FROM AlbumTable") // 테이블의 모든 값을 가져와라
    fun getAlbums(): List<Album>

    @Query("SELECT * FROM AlbumTable WHERE id = :id")
    fun getAlbum(id: Int): Album

    @Query("SELECT * FROM SongTable WHERE albumIdx = :albumIdx") // 특정 앨범의 곡 목록 가져오기
    fun getAlbumSongs(albumIdx: Int): List<Song>
}
