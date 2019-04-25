package com.example.opentriviadbapp.mvpview

import com.example.opentriviadbapp.base.MvpView

interface QuestionFragmentMvpView : MvpView {

    fun resetAnswerBackground()

    fun setAnswer(type: String, correctAns: String, incorrectAns: ArrayList<String>)

    fun setAnswerFeedback(correct: Boolean, selectedAns: String)

    fun setAnswerVisible(visible: Boolean, number: Int)

    fun setDifficulty(difficulty: String)

    fun setQuestion(question: String)

    fun setQuestionProgressBarVisible(visible: Boolean)

    fun setQuestionVisible(visible: Boolean)

    fun setRollButtonEnable(enable: Boolean)

    fun setSnackbar(text: String)
}