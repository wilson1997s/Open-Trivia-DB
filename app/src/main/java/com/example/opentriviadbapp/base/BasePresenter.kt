package com.example.opentriviadbapp.base

open class BasePresenter<T : MvpView> : Presenter<T> {

    private var mMvpView: T? = null

    override fun attachView(mvpView: T) {
        mMvpView = mvpView
    }

    override fun detachView() {
        mMvpView = null
    }

    fun getMvpView(): T? {
        return mMvpView
    }

    fun isViewAttached(): Boolean {
        return mMvpView != null
    }

}