package com.example.dreamofyou.api

import Users
import com.example.dreamofyou.repository.GithubRepository
import retrofit2.Call

class RestClient : AbstractNetwork(), GithubRepository {

    override fun getBaseUrl(): String {
        return "https://api.github.com"
    }

    fun getService(): GithubAPI {
        return getRetrofit().create(GithubAPI::class.java)
    }

    override fun getUserGithub(userId: String, page: Int): Call<Users> {
        return getService().getUserList(userId, page)
    }
}
