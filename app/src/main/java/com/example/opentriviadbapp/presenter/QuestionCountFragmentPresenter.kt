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

    private var mCategoryList: ArrayList<Category> = arrayListOf<Category>()
    private var counter = 0

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
                        getQuestionCount()
                    },
                    { error -> getMvpView()!!.showError("Error! " + error.message) }
                )
        )
    }

    private fun getQuestionCount() {

        mCategoryList.forEach {
            compositeDisposable!!.add(
                ApiRepo.getQuestionCountList(it.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { result ->
                            mCategoryList.forEachIndexed { index, element ->
                                if (mCategoryList[index].id == it.id) {
                                    mCategoryList[index].totalEasy = result.categoryCount.totalEasy
                                    mCategoryList[index].totalMedium = result.categoryCount.totalMedium
                                    mCategoryList[index].totalHard = result.categoryCount.totalHard
                                    mCategoryList[index].totalQuestion = result.categoryCount.totalQuestion
                                }
                            }
                            counter++
                            if (counter == mCategoryList.size) {
                                getMvpView()!!.setupQuestionCountRecyclerView(mCategoryList)
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