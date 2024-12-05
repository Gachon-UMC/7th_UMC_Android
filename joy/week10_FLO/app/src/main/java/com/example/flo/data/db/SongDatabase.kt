package com.example.flo.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flo.data.entities.Album
import com.example.flo.data.entities.Like
import com.example.flo.data.entities.Song
import com.example.flo.data.entities.User

@Database(entities = [Song::class, Album::class, User::class, Like::class], version = 12)
abstract class SongDatabase: RoomDatabase() {
    abstract fun albumDao(): AlbumDao
    abstract fun songDao(): SongDao
    abstract fun userDao(): UserDao

    companion object {
        private var instance: SongDatabase? = null

        @Synchronized
        fun getInstance(context: Context): SongDatabase? {
            if (instance == null) {
                synchronized(SongDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "song-database"
                    ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
                }
            }

            return instance
        }
    }


}