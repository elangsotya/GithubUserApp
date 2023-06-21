package com.example.submissionaplikasigithubuser.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.submissionaplikasigithubuser.database.entity.favoriteUser
import com.example.submissionaplikasigithubuser.database.room.favoriteUserRoomDatabse
import com.example.submissionaplikasigithubuser.repository.FavoriteUserRepository

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = FavoriteUserRepository(favoriteUserRoomDatabse.getDatabase(application).favDao())
    fun getFavUser(): LiveData<List<favoriteUser>> = repository.getAllFavorite()

}