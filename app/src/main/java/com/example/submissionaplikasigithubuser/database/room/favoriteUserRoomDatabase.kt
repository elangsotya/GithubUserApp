package com.example.submissionaplikasigithubuser.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.submissionaplikasigithubuser.database.dao.favoriteUserDao
import com.example.submissionaplikasigithubuser.database.entity.favoriteUser

@Database(entities = [favoriteUser::class], version = 1)
abstract class favoriteUserRoomDatabse : RoomDatabase() {

    abstract fun favDao(): favoriteUserDao

    companion object{
        @Volatile
        private var INSTANCE: favoriteUserRoomDatabse? = null

        fun getDatabase(context: Context): favoriteUserRoomDatabse=
                INSTANCE ?: synchronized(favoriteUserRoomDatabse::class) {
                    INSTANCE ?: Room.databaseBuilder(context.applicationContext,
                        favoriteUserRoomDatabse::class.java, "fav.db")
                        .build()
                }
        }
}


