package com.example.opentriviadbapp.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.opentriviadbapp.R
import com.example.opentriviadbapp.base.BaseFragment
import com.example.opentriviadbapp.model.Category
import com.example.opentriviadbapp.mvpview.MainFragmentMvpView
import com.example.opentriviadbapp.presenter.MainFragmentPresenter
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : BaseFragment(), MainFragmentMvpView {

    //view
    private var dataAdapter: ArrayAdapter<String>? = null

    //presenter
    private var mainFragmentPresenter: MainFragmentPresenter? = null

    private var mCategoryList = arrayListOf<Category>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_main, container, false)

        mainFragmentPresenter = MainFragmentPresenter()  //create presenter
        mainFragmentPresenter!!.attachView(this) //attach presenter

        setActionBarTitle("OTDB App")

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainFragmentPresenter!!.setupCategorySpinner()  //setup category

        tv_question_count.setOnClickListener { view ->
            view.findNavController().navigate(R.id.question_count_action)
        }

        btn_next.setOnClickListener { view ->
            var bundle = mainFragmentPresenter!!.makeQuestionBundle(
                spn_category.selectedItem.toString().toLowerCase(),
                spn_difficulty.selectedItem.toString().toLowerCase(),
                spn_type.selectedItem.toString().toLowerCase()
            )
            view.findNavController().navigate(R.id.action_start, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainFragmentPresenter!!.detachView()

    }

    override fun setButtonNextEnable(enable: Boolean) {
        btn_next.isEnabled = enable
        if (enable) {
            btn_next.setBackgroundColor(Color.parseColor("#D81B60")) //color accent
        } else {
            btn_next.setBackgroundColor(Color.LTGRAY)
        }
    }

    override fun setCategoryProgressBarVisible(visible: Boolean) {
        if (visible) {
            pb_category.visibility = View.VISIBLE
        } else {
            pb_category.visibility = View.INVISIBLE
        }
    }

    override fun setupCategorySpinner(categoryList: ArrayList<Category>, categoryNameList: ArrayList<String>) {
        mCategoryList.clear()
        mCategoryList.addAll(categoryList)

        dataAdapter =
            ArrayAdapter(activity!!.applicationContext, android.R.layout.simple_spinner_item, categoryNameList)
        dataAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spn_category.adapter = dataAdapter
        dataAdapter!!.notifyDataSetChanged()
    }

    override fun showError(errorCode: String) {
        Toast.makeText(activity, errorCode, Toast.LENGTH_SHORT).show()
    }
}