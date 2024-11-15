package com.example.week_7

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Profile::class], version = 1)
abstract class ProfileDatabase: RoomDatabase() {
    abstract fun profileDao(): ProfileDao

    companion object {
        private var instance: ProfileDatabase? = null

        @Synchronized
        fun getInstance(context: Context): ProfileDatabase? {
            if (instance == null) {
                synchronized(ProfileDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ProfileDatabase::class.java,
                        "user-database" // 다른 데이터 베이스랑 이름이 겹치지 않도록 주의
                    ).build() // 편의상 메인 쓰레드에서 처리하게 한다.
                }
            }

            return instance
        }
    }
}