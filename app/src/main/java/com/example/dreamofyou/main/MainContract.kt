package com.example.dreamofyou.main

import ListUsers
import com.example.dreamofyou.base.BasePresenter
import com.example.dreamofyou.base.BaseView

interface MainContract {

    interface View : BaseView {

        fun onSuccessGetUser(listUser: List<ListUsers>?)

        fun onFailedGetUser(errorMessage: String)

        fun onHideProgressBar(status: Boolean)

        fun verifyUrl(url: String)

    }

    interface Presenter : BasePresenter<View> {
        fun onSearchUserGithub(userName: String, page: Int)
    }
}