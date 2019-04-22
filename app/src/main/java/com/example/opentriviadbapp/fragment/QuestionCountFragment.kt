package com.example.opentriviadbapp.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.opentriviadbapp.R
import com.example.opentriviadbapp.adapter.QuestionCountAdapter
import com.example.opentriviadbapp.model.Category
import com.example.opentriviadbapp.retrofit.ApiRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_question_count.*
import kotlinx.android.synthetic.main.fragment_question_count.view.*


class QuestionCountFragment : Fragment() {

    private var disposable: Disposable? = null
    private var countAdapter: QuestionCountAdapter? = null
    private var categoryList: ArrayList<Category> = arrayListOf<Category>()
    var counter = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_question_count, container, false)

        activity!!.tb_main_act.title = "Question Category"

        val layoutManager = LinearLayoutManager(view.context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        view.rv_question_count.layoutManager = layoutManager

        countAdapter = QuestionCountAdapter(view.context, categoryList)
        view.rv_question_count.adapter = countAdapter
        //set it to be invisible at first
        view.rv_question_count.visibility = View.INVISIBLE
        //get category list first (doing it this way, so it doesnt need to depend on others)
        disposable =
            ApiRepo.getCategoryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        result.categoryList.forEach {
                            //add reach Category and name into the list
                            categoryList.add(it)
                        }
                        //after complete, get question count, one category by one category
                        getQuestionCount()
                    },
                    { error -> println("Error:" + error.message) }
                )

        return view
    }

    fun getQuestionCount() {
        categoryList.forEach {

            disposable =
                ApiRepo.getQuestionCountList(it.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { result ->
                            categoryList.forEachIndexed { index, element ->
                                if (categoryList[index].id == it.id) {
                                    categoryList[index].totalEasy = result.categoryCount.totalEasy
                                    categoryList[index].totalMedium = result.categoryCount.totalMedium
                                    categoryList[index].totalHard = result.categoryCount.totalHard
                                    categoryList[index].totalQuestion = result.categoryCount.totalQuestion
                                }
                            }
                            counter += 1
                            if (counter == categoryList.size) {
                                pb_count.visibility = View.INVISIBLE
                                rv_question_count.visibility = View.VISIBLE
                                countAdapter!!.notifyDataSetChanged()
                            }
                        },
                        { error -> println("Error:" + error.message) }
                    )

        }
    }

}