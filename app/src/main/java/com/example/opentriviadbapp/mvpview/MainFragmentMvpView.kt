package com.example.opentriviadbapp.mvpview

import com.example.opentriviadbapp.base.MvpView
import com.example.opentriviadbapp.model.Category

interface MainFragmentMvpView : MvpView {

    fun setCategoryProgressBarVisible(visible: Boolean)

    fun setButtonNextEnable(enable: Boolean)

    fun setupCategorySpinner(categoryList: ArrayList<Category> ,categoryNameList: ArrayList<String> )

}