package com.example.submissionaplikasigithubuser.viewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.*
import com.example.submissionaplikasigithubuser.config.ApiConfig
import com.example.submissionaplikasigithubuser.preferences.themePreferences
import com.example.submissionaplikasigithubuser.response.GithubResponse
import com.example.submissionaplikasigithubuser.response.ItemsItem
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: themePreferences) : ViewModel() {

    private val _githubUser = MutableLiveData<ArrayList<ItemsItem>>()
    val githubUser: LiveData<ArrayList<ItemsItem>> = _githubUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "MainViewModel"
    }

    init {
        findUserList("\"\"")
    }

    fun findUserList(data: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserList(data)
        client.enqueue(object : Callback<GithubResponse> {
            @SuppressLint("NullSafeMutableLiveData")
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _githubUser.value = response.body()?.items
                }else {
                    Log.e(TAG, "\"onFailure: ${response.message()}\"")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}