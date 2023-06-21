package com.example.submissionaplikasigithubuser.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.submissionaplikasigithubuser.config.ApiConfig
import com.example.submissionaplikasigithubuser.database.entity.favoriteUser
import com.example.submissionaplikasigithubuser.database.room.favoriteUserRoomDatabse
import com.example.submissionaplikasigithubuser.repository.FavoriteUserRepository
import com.example.submissionaplikasigithubuser.response.DetailUserResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val _detailUser = MutableLiveData<DetailUserResponse>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val repository = FavoriteUserRepository(favoriteUserRoomDatabse.getDatabase(application).favDao())

    companion object {
        private const val TAG = "DetailViewModel"
    }

    fun setDetailUser(name: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(name)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getDetailUser(): LiveData<DetailUserResponse> {
        return _detailUser
    }

    fun insertFavorite(id: Int, userName: String, avatarUrl: String) = viewModelScope.launch{
        val fav = favoriteUser(
            id, userName, avatarUrl
        )
        repository.insertFav(fav)
    }

    fun checkFavorite(id: Int): Int = repository.checkFav(id)

    fun deleteFavorite(id: Int) = viewModelScope.launch {
        repository.deleteFav(id)
    }
}