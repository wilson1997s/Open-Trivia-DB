package com.example.opentriviadbapp.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.opentriviadbapp.R
import com.example.opentriviadbapp.adapter.QuestionCountAdapter
import com.example.opentriviadbapp.base.BaseFragment
import com.example.opentriviadbapp.model.Category
import com.example.opentriviadbapp.mvpview.QuestionCountFragmentMvpView
import com.example.opentriviadbapp.presenter.QuestionCountFragmentPresenter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_question_count.*


class QuestionCountFragment : BaseFragment(), QuestionCountFragmentMvpView {

    //view
    private var countAdapter: QuestionCountAdapter? = null

    //presenter
    private var questionCountFragmentPresenter: QuestionCountFragmentPresenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_question_count, container, false)

        questionCountFragmentPresenter = QuestionCountFragmentPresenter()
        questionCountFragmentPresenter!!.attachView(this)

        activity!!.tb_main_act.title = "Question Category"

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        questionCountFragmentPresenter!!.setupRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        questionCountFragmentPresenter!!.detachView()
    }

    override fun setCountProgressBarVisible(visible: Boolean) {
        if (visible) {
            pb_count.visibility = View.VISIBLE
        } else {
            pb_count.visibility = View.INVISIBLE
        }
    }

    override fun setCountRecyclerViewVisible(visible: Boolean) {
        if (visible) {
            rv_question_count.visibility = View.VISIBLE
        } else {
            rv_question_count.visibility = View.INVISIBLE
        }
    }

    override fun setupQuestionCountRecyclerView(categoryList: ArrayList<Category>) {
        val layoutManager = LinearLayoutManager(view!!.context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_question_count.layoutManager = layoutManager

        countAdapter = QuestionCountAdapter(view!!.context, categoryList)
        rv_question_count.adapter = countAdapter

        countAdapter!!.notifyDataSetChanged()
    }

    override fun showError(errorCode: String) {
        Toast.makeText(activity, errorCode, Toast.LENGTH_SHORT).show()
    }
}