package com.example.dreamofyou.repository

import Users
import retrofit2.Call

interface GithubRepository {
    fun getUserGithub(userId: String, page: Int): Call<Users>
}