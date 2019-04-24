package com.example.opentriviadbapp.base

interface Presenter<V : MvpView> {

    fun attachView(mvpView: V)

    fun detachView()
}