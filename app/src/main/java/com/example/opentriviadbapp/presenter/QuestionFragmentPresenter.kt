package com.example.opentriviadbapp.presenter

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.text.Html
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

    // from bundle (prefix b)
    private var bCategory: String? = null
    private var bDifficulty: String? = null
    private var bType: String? = null

    // from API response (prefix m)
    private var mSelectedQuestion: Question? = null
    private var mType: String? = null
    private var mDifficulty: String? = null
    private var mQuestion: String? = null
    private var mCorrectAnswer: String? = null
    private var mIncorrectAnswer = arrayListOf<String>()

    private var mToken = "AAAA"
    private var mActivity: FragmentActivity? = null

    override fun detachView() {
        super.detachView()
        compositeDisposable.clear()
    }

    fun checkAnswer(selectedAns: String) {
        getMvpView()!!.resetAnswerBackground()
        if (selectedAns.equals(mCorrectAnswer)) {
            getMvpView()!!.setAnswerFeedback(true, selectedAns)
        } else {
            getMvpView()!!.setAnswerFeedback(false, selectedAns)
        }
    }

    fun getToken(activity: FragmentActivity?) {
        mActivity = activity

        //reading from SharedPreference
        var sharedPref = activity!!.getSharedPreferences("token1", Context.MODE_PRIVATE)

        if (sharedPref!!.contains("token")) {
            mToken = sharedPref!!.getString("token", "AAAA")!!
            setQuestion(mToken)
        } else {
            requestNewToken()
        }
    }

    fun passBundle(category: String, difficulty: String, type: String) {
        bCategory = category
        bDifficulty = difficulty
        bType = type
    }

    fun setQuestion(token: String) {
        getMvpView()!!.setQuestionVisible(false)
        getMvpView()!!.setAnswerVisible(false, 4)
        getMvpView()!!.setRollButtonEnable(false)
        getMvpView()!!.setQuestionProgressBarVisible(true)
        getMvpView()!!.resetAnswerBackground()

        var url = "https://opentdb.com/api.php?amount=1&token=$token"
        if (!bCategory.equals("default")) {
            url = "$url&category=$bCategory"
        }
        if (!bDifficulty.equals("default")) {
            url = "$url&difficulty=$bDifficulty"
        }
        if (!bType.equals("default")) {
            url = "$url&type=$bType"
        }

        compositeDisposable.add(
            ApiRepo.getQuestion(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        if (result.responseCode != 4) {
                            handleResponse(result.responseCode, result.results)
                        } else {
                            handleResponse(result.responseCode, listOf())
                        }
                    },
                    { error -> getMvpView()!!.showError("Error! " + error.message) }
                )
        )
    }

    private fun handleResponse(responseCode: Int, results: List<Question>) {
        when (responseCode) {
            0 -> {  //return successfully
                mSelectedQuestion = results[0]
                mType = mSelectedQuestion!!.type
                mDifficulty = mSelectedQuestion!!.difficulty
                mQuestion = Html.fromHtml(mSelectedQuestion!!.question).toString()
                mCorrectAnswer = Html.fromHtml(mSelectedQuestion!!.correctAnswer).toString()
                val tempIncorrectAnswer = mSelectedQuestion!!.incorrectAnswer

                mIncorrectAnswer.clear()
                tempIncorrectAnswer.forEach {
                    mIncorrectAnswer.add(Html.fromHtml(it).toString())
                }

                getMvpView()!!.setQuestion(mQuestion!!)
                getMvpView()!!.setDifficulty(mDifficulty!!)


                if (mType!!.toLowerCase().equals("boolean")) {
                    getMvpView()!!.setAnswerVisible(true, 2)

                } else {
                    getMvpView()!!.setAnswerVisible(true, 4)
                }

                getMvpView()!!.setAnswer(mType!!, mCorrectAnswer!!, mIncorrectAnswer)
                getMvpView()!!.setQuestionProgressBarVisible(false)
                getMvpView()!!.setRollButtonEnable(true)
                getMvpView()!!.setQuestionVisible(true)
            }

            1 -> { //could not return result
                getMvpView()!!.setSnackbar("No result found, please refine criteria")
                getMvpView()!!.setQuestionProgressBarVisible(false)
            }

            3 -> { //token not found
                requestNewToken()
                setQuestion(mToken)
            }

            4 -> { //token empty session
                resetToken()
                setQuestion(mToken)
            }
        }
    }

    private fun requestNewToken() {
        compositeDisposable.add(
            ApiRepo.getToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        //write to sharedPreference
                        val sharedPref = mActivity?.getSharedPreferences("token1", Context.MODE_PRIVATE)

                        sharedPref!!.edit().putString("token", result.token).apply()
                        mToken = result.token

                        setQuestion(mToken)
                    }, { error -> getMvpView()!!.showError("Error! " + error.message) }
                )
        )
    }

    private fun resetToken() {
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