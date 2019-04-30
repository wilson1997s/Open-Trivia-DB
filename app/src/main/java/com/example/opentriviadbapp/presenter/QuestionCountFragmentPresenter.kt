package com.example.opentriviadbapp.presenter

import com.example.opentriviadbapp.base.BasePresenter
import com.example.opentriviadbapp.model.Category
import com.example.opentriviadbapp.mvpview.QuestionCountFragmentMvpView
import com.example.opentriviadbapp.retrofit.ApiRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class QuestionCountFragmentPresenter : BasePresenter<QuestionCountFragmentMvpView>() {

    //for RxJava
    private var compositeDisposable: CompositeDisposable? = CompositeDisposable()

    override fun detachView() {
        super.detachView()
        compositeDisposable!!.clear()
    }

    fun setupRecyclerView() {
        getMvpView()!!.setCountProgressBarVisible(true)
        getMvpView()!!.setCountRecyclerViewVisible(false)
        getCategoryList() //followed by get Question count
    }

    private fun getCategoryList() {

        var mCategoryList: ArrayList<Category> = arrayListOf<Category>()

        compositeDisposable!!.add(
            ApiRepo.getCategoryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        result.categoryList.forEach {
                            //add reach Category and name into the list
                            mCategoryList.add(it)
                        }
                        getQuestionCount(mCategoryList)
                    },
                    { error -> getMvpView()!!.showError("Error! " + error.message) }
                )
        )
    }

    private fun getQuestionCount(categoryList: ArrayList<Category>) {
        var counter = 0
        categoryList.forEach {
            compositeDisposable!!.add(
                ApiRepo.getQuestionCountList(it.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { result ->
                            categoryList.forEachIndexed { index, element ->
                                if (categoryList[index].id == it.id) {
                                    categoryList[index].totalEasy = result.categoryCount.totalEasy
                                    categoryList[index].totalMedium = result.categoryCount.totalMedium
                                    categoryList[index].totalHard = result.categoryCount.totalHard
                                    categoryList[index].totalQuestion = result.categoryCount.totalQuestion
                                }
                            }
                            counter++
                            if (counter == categoryList.size) {
                                getMvpView()!!.setupQuestionCountRecyclerView(categoryList)
                                getMvpView()!!.setCountProgressBarVisible(false)
                                getMvpView()!!.setCountRecyclerViewVisible(true)
                            }
                        },
                        { error -> getMvpView()!!.showError("Error! " + error.message) }
                    )
            )
        }
    }
}