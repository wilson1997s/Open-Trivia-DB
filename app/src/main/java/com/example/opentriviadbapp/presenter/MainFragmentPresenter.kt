package com.example.opentriviadbapp.presenter

import android.os.Bundle
import com.example.opentriviadbapp.Constant
import com.example.opentriviadbapp.base.BasePresenter
import com.example.opentriviadbapp.model.Category
import com.example.opentriviadbapp.mvpview.MainFragmentMvpView
import com.example.opentriviadbapp.retrofit.ApiRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainFragmentPresenter : BasePresenter<MainFragmentMvpView>() {

    //for RxJava
    private var compositeDisposable: CompositeDisposable? = CompositeDisposable()

    override fun detachView() {
        super.detachView()
        compositeDisposable!!.clear()
    }

    fun makeQuestionBundle(categoryList: ArrayList<Category>, category: String, difficulty: String, type: String): Bundle {
        var bundle = Bundle()
        var tempId = Constant.DEFAULT
        if (!category.equals(Constant.DEFAULT)) {
            categoryList.forEach {
                if (it.name.toLowerCase().equals(category)) {
                    tempId = it.id.toString()
                }
            }
        }
        bundle.putString(Constant.BUNDLE_KEY_CATEGORY, tempId)
        bundle.putString(Constant.BUNDLE_KEY_DIFFICULTY, difficulty)
        bundle.putString(Constant.BUNDLE_KEY_TYPE, type)
        return bundle
    }

    fun getCategorySpinnerData() {

        getMvpView()!!.setButtonNextEnable(false)

        var mCategoryNameList = arrayListOf<String>()
        var mCategoryList = arrayListOf<Category>()

        compositeDisposable!!.add(
            ApiRepo.getCategoryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        mCategoryNameList.add(Constant.DEFAULT.capitalize())  //default value for category

                        result.categoryList.forEach {
                            //add each Category and name into the list
                            mCategoryNameList.add(it.name)
                            mCategoryList.add(it)
                        }

                        getMvpView()!!.setCategoryProgressBarVisible(false)
                        getMvpView()!!.setButtonNextEnable(true)
                        getMvpView()!!.displayCategorySpinner(mCategoryList, mCategoryNameList)

                    },
                    { error -> getMvpView()!!.showError("Error! " + error.message) }
                )
        )
    }

}