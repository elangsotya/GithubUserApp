package com.example.submissionaplikasigithubuser.service


import com.example.submissionaplikasigithubuser.response.DetailUserResponse
import com.example.submissionaplikasigithubuser.response.GithubResponse
import com.example.submissionaplikasigithubuser.response.ItemsItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getUserList(@Query("q") q: String): Call<GithubResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getDetailUser(@Path("username") username: String): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollow(@Path("username") username: String): Call<ArrayList<ItemsItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(@Path("username") username: String): Call<ArrayList<ItemsItem>>

}