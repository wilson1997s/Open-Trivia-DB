package com.example.opentriviadbapp.fragment

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.findNavController
import com.example.opentriviadbapp.R
import com.example.opentriviadbapp.model.Category
import com.example.opentriviadbapp.retrofit.ApiRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : Fragment() {

    private var dataAdapter: ArrayAdapter<String>? = null
    private var disposable: Disposable? = null
    private var categoryName = arrayListOf<String>()
    private var categoryList = arrayListOf<Category>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_main, container, false)

        //set onclick listener
        view.tv_question_count.setOnClickListener { view ->
            view.findNavController().navigate(R.id.question_count_action)
        }

        view.btn_next.setOnClickListener { view ->
            var bundle = Bundle()

            var tempId = "default"
            if (!spn_category.selectedItem.toString().toLowerCase().equals("default")) {
                categoryList.forEach {
                    if (it.name.equals(spn_category.selectedItem.toString())) {
                        tempId = it.id.toString()
                    }
                }
            }

            bundle.putString("category", tempId)
            bundle.putString("difficulty", spn_difficulty.selectedItem.toString().toLowerCase())
            bundle.putString("type", spn_type.selectedItem.toString().toLowerCase())

            view.findNavController().navigate(R.id.action_start, bundle)
        }

        getCategoryList()
        dataAdapter =
            ArrayAdapter<String>(
                activity!!.applicationContext,
                android.R.layout.simple_spinner_item,
                categoryName
            )
        dataAdapter!!.notifyDataSetChanged()
        dataAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        view.spn_category.adapter = dataAdapter;

        view.btn_next.isEnabled = false
        view.btn_next.setBackgroundColor(Color.LTGRAY)

        return view
    }


    fun getCategoryList() {

        //make sure the category list (for Category object, and Name) is empty
        categoryName.clear()
        categoryList.clear()

        disposable =
            ApiRepo.getCategoryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        //add default value for name
                        categoryName.add("Default")

                        result.categoryList.forEach {
                            //add reach Category and name into the list
                            categoryName.add(it.name)
                            categoryList.add(it)
                        }
                        //call adapter to update view
                        dataAdapter!!.notifyDataSetChanged()
                        //hide the progressBar
                        setProgressBarVisibility(false)
                        view!!.btn_next.isEnabled = true
                        view!!.btn_next.setBackgroundColor(Color.parseColor("#D81B60"))
                    },
                    { error -> println("Error:" + error.message) }
                )
    }

    override fun onResume() {
        super.onResume()
        activity!!.tb_main_act.title = "OTDB App"
    }

    // to change progress bar visibility
    fun setProgressBarVisibility(visible: Boolean) {
        if (visible) {
            pb_category.visibility = View.VISIBLE
        } else {
            pb_category.visibility = View.INVISIBLE
        }
    }

}