package com.example.opentriviadbapp.base

import android.support.v4.app.Fragment

abstract class BaseFragment : Fragment() {

    fun setActionBarTitle(title: String) {
        activity!!.title = title
    }
}