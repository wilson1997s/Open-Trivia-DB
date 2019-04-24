package com.example.opentriviadbapp.presenter

import android.os.Bundle
import com.example.opentriviadbapp.base.BasePresenter
import com.example.opentriviadbapp.model.Category
import com.example.opentriviadbapp.mvpview.MainFragmentMvpView
import com.example.opentriviadbapp.retrofit.ApiRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainFragmentPresenter : BasePresenter<MainFragmentMvpView>() {

    private var mCategoryNameList = arrayListOf<String>()
    private var mCategoryList = arrayListOf<Category>()
    private var compositeDisposable: CompositeDisposable? = CompositeDisposable()

    override fun attachView(mvpView: MainFragmentMvpView) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        compositeDisposable!!.clear()
    }

    fun setupCategorySpinner() {

        getMvpView()!!.setButtonNextEnable(false)

        mCategoryNameList.clear()
        mCategoryList.clear()

        compositeDisposable!!.add(
            ApiRepo.getCategoryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        mCategoryNameList.add("Default")  //default value for category

                        result.categoryList.forEach {
                            //add each Category and name into the list
                            mCategoryNameList.add(it.name)
                            mCategoryList.add(it)
                        }

                        getMvpView()!!.setCategoryProgressBarVisible(false)
                        getMvpView()!!.setButtonNextEnable(true)
                        getMvpView()!!.setupCategorySpinner(mCategoryList, mCategoryNameList)

                    },
                    { error -> getMvpView()!!.showError("Error! " + error.message) }
                )
        )
    }

    fun makeQuestionBundle(category: String, difficulty: String, type: String): Bundle {
        var bundle = Bundle()
        var tempId = "default"
        if (!category.equals("default")) {
            mCategoryList.forEach {
                if (it.name.toLowerCase().equals(category)) {
                    tempId = it.id.toString()
                }
            }
        }
        bundle.putString("category", tempId)
        bundle.putString("difficulty", difficulty)
        bundle.putString("type", type)
        return bundle
    }


}