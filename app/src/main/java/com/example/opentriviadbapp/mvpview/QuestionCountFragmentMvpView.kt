package com.example.opentriviadbapp.mvpview

import com.example.opentriviadbapp.base.MvpView
import com.example.opentriviadbapp.model.Category

interface QuestionCountFragmentMvpView: MvpView {

    fun setCountProgressBarVisible(visible: Boolean)

    fun setCountRecyclerViewVisible(visible: Boolean)

    fun setupQuestionCountRecyclerView(categoryList: ArrayList<Category>)


}