package com.example.dreamofyou.main

import Users
import android.util.Log
import com.example.dreamofyou.api.RestClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainPresenter : MainContract.Presenter {

    private companion object {
        const val ERROR_VALIDATION = 422

        const val RATE_LIMIT = 403

        const val RESPONSE_SUCCESS = 200
    }

    private var mainView: MainContract.View? = null

    private var restClient = RestClient()

    override fun onSearchUserGithub(userName: String, page: Int) {
        if (userName.isNotEmpty()) {
            restClient.getService().getUserList(userName, page)
                .enqueue(object : Callback<Users> {
                    override fun onFailure(call: Call<Users>, t: Throwable) {
                        Log.e("error", t.toString())
                    }

                    override fun onResponse(call: Call<Users>, response: Response<Users>) {
                        mainView?.verifyUrl(call.request().url().toString())
                        when {
                            response.code() == ERROR_VALIDATION -> {
                                mainView?.onFailedGetUser("Validation Failed")
                            }
                            response.code() == RESPONSE_SUCCESS -> {
                                mainView?.onSuccessGetUser(response.body()?.items)
                            }
                            response.code() == RATE_LIMIT -> {
                                mainView?.onFailedGetUser("Rate limit exceeded")
                            }
                        }
                    }
                })
        } else {
            mainView?.onHideProgressBar(false)
        }
    }

    override fun onAttach(view: MainContract.View) {
        mainView = view
    }


    override fun onDetach() {
        mainView = null
    }


}