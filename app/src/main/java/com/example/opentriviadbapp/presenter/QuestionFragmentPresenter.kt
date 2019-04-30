package com.example.opentriviadbapp.presenter

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.text.Html
import com.example.opentriviadbapp.Constant
import com.example.opentriviadbapp.base.BasePresenter
import com.example.opentriviadbapp.model.Question
import com.example.opentriviadbapp.mvpview.QuestionFragmentMvpView
import com.example.opentriviadbapp.retrofit.ApiRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class QuestionFragmentPresenter : BasePresenter<QuestionFragmentMvpView>() {

    //for Rxjava
    private var compositeDisposable = CompositeDisposable()

    private var mActivity: FragmentActivity? = null

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    fun checkAnswer(selectedAns: String, correctAns: String) {
        getMvpView()!!.resetAnswerBackground()
        if (selectedAns.equals(correctAns)) {
            getMvpView()!!.setAnswerFeedback(true, selectedAns)
        } else {
            getMvpView()!!.setAnswerFeedback(false, selectedAns)
        }
    }

    fun getToken(activity: FragmentActivity?, category: String, difficulty: String, type: String) {
        mActivity = activity

        //reading from SharedPreference
        var sharedPref = activity!!.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE)

        if (sharedPref!!.contains(Constant.PAIR_NAME)) {
            val mToken = sharedPref!!.getString(Constant.PAIR_NAME, "AAAA")!!
            getQuestion(mToken, category!!, difficulty!!, type!!)
        } else {
            requestNewToken(category!!, difficulty!!, type!!)
        }
    }

    fun getQuestion(token: String, category: String, difficulty: String, type: String) {
        getMvpView()!!.setQuestionVisible(false)
        getMvpView()!!.setAnswerVisible(false, 4)
        getMvpView()!!.setRollButtonEnable(false)
        getMvpView()!!.setQuestionProgressBarVisible(true)
        getMvpView()!!.resetAnswerBackground()

        compositeDisposable.add(
            ApiRepo.getQuestion(token, category!!, difficulty!!, type!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        if (result.responseCode != Constant.RESPONSE_CODE_4) {
                            handleResponse(result.responseCode, result.results, category!!, difficulty!!, type!!)
                        } else {
                            handleResponse(result.responseCode, listOf(), category!!, difficulty!!, type!!)
                        }
                    },
                    { error -> getMvpView()!!.showError("Error! " + error.message) }
                )
        )
    }

    private fun handleResponse(
        responseCode: Int,
        results: List<Question>,
        category: String,
        difficulty: String,
        type: String
    ) {

        val sharedPref = mActivity!!.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val mToken = sharedPref!!.getString(Constant.PAIR_NAME, "AAAA")!!

        when (responseCode) {
            Constant.RESPONSE_CODE_0 -> {  //return successfully
                val mSelectedQuestion = results[0]
                val mType = mSelectedQuestion.type
                val mDifficulty = mSelectedQuestion.difficulty
                val mQuestion = Html.fromHtml(mSelectedQuestion.question).toString()
                val mCorrectAnswer = Html.fromHtml(mSelectedQuestion.correctAnswer).toString()
                val tempIncorrectAnswer = mSelectedQuestion.incorrectAnswer

                val mIncorrectAnswer = arrayListOf<String>()
                tempIncorrectAnswer.forEach {
                    mIncorrectAnswer.add(Html.fromHtml(it).toString())
                }

                getMvpView()!!.setQuestion(mQuestion)
                getMvpView()!!.setDifficulty(mDifficulty)


                if (mType.toLowerCase().equals("boolean")) {
                    getMvpView()!!.setAnswerVisible(true, 2)

                } else {
                    getMvpView()!!.setAnswerVisible(true, 4)
                }

                getMvpView()!!.setAnswer(mType, mCorrectAnswer, mIncorrectAnswer)
                getMvpView()!!.setQuestionProgressBarVisible(false)
                getMvpView()!!.setRollButtonEnable(true)
                getMvpView()!!.setQuestionVisible(true)
            }

            Constant.RESPONSE_CODE_1 -> { //could not return result
                getMvpView()!!.setSnackbar("No result found, please refine criteria")
                getMvpView()!!.setQuestionProgressBarVisible(false)
            }

            Constant.RESPONSE_CODE_3 -> { //token not found
                requestNewToken(category, difficulty, type)
            }

            Constant.RESPONSE_CODE_4 -> { //token empty session
                resetToken()
                getQuestion(mToken, category!!, difficulty!!, type!!)
            }
        }
    }

    private fun requestNewToken(category: String, difficulty: String, type: String) {

        compositeDisposable.add(
            ApiRepo.getToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        //write to sharedPreference
                        val sharedPref =
                            mActivity?.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE)

                        sharedPref!!.edit().putString(Constant.PAIR_NAME, result.token).apply()

                        getQuestion(result.token, category!!, difficulty!!, type!!)
                    }, { error -> getMvpView()!!.showError("Error! " + error.message) }
                )
        )
    }

    private fun resetToken() {

        val sharedPref = mActivity!!.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val mToken = sharedPref!!.getString(Constant.PAIR_NAME, "AAAA")!!

        compositeDisposable.add(
            ApiRepo.resetToken(mToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                    }, { error -> getMvpView()!!.showError("Error! " + error.message) }
                )
        )
    }

}