package com.example.dreamofyou.base

interface BasePresenter<T : BaseView?> {

    fun onAttach(view: T)

    fun onDetach()
}