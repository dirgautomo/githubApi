package com.example.dreamofyou.api

import Users
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubAPI {

    @GET("/search/users?per_page=15")
    fun getUserList(
        @Query("q") user: String,
        @Query("page") page: Int
    ): Call<Users>

}