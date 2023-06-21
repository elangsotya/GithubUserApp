package com.example.submissionaplikasigithubuser.repository

import androidx.lifecycle.LiveData
import com.example.submissionaplikasigithubuser.database.dao.favoriteUserDao
import com.example.submissionaplikasigithubuser.database.entity.favoriteUser

class FavoriteUserRepository(private val mFavDao: favoriteUserDao) {

    fun getAllFavorite():LiveData<List<favoriteUser>> = mFavDao.getAllFavoriteUser()

    suspend fun insertFav(fav: favoriteUser) {
         mFavDao.insert(fav)
    }

    suspend fun deleteFav(id: Int){
        mFavDao.deleteFavUser(id)
    }

    fun checkFav(id: Int): Int =
        mFavDao.isFavUserMarked(id)

}