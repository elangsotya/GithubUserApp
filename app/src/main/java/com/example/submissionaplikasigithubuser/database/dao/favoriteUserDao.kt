package com.example.submissionaplikasigithubuser.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.example.submissionaplikasigithubuser.database.entity.favoriteUser

@Dao
interface favoriteUserDao {

    @Insert
    suspend fun insert(favUser: favoriteUser)

    @Query("DELETE FROM favUser WHERE favUser.id = :id")
    suspend fun deleteFavUser(id: Int) : Int

    @Query("SELECT EXISTS (SELECT * FROM favUser WHERE favUser.id = :id)")
    fun isFavUserMarked(id: Int): Int

    @Query("SELECT * from favUser")
    fun getAllFavoriteUser(): LiveData<List<favoriteUser>>
}