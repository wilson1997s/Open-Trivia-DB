package com.example.opentriviadbapp.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.opentriviadbapp.R
import com.example.opentriviadbapp.base.BaseFragment
import com.example.opentriviadbapp.mvpview.QuestionFragmentMvpView
import com.example.opentriviadbapp.presenter.QuestionFragmentPresenter
import kotlinx.android.synthetic.main.fragment_question.*
import kotlinx.android.synthetic.main.fragment_question.view.*

class QuestionFragment : BaseFragment(), QuestionFragmentMvpView {

    //presenter
    private var questionFragmentPresenter: QuestionFragmentPresenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_question, container, false)

        questionFragmentPresenter = QuestionFragmentPresenter()
        questionFragmentPresenter!!.attachView(this)

        setActionBarTitle("Question")

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var category = arguments?.getString("category")
        var difficulty = arguments?.getString("difficulty")
        var type = arguments?.getString("type")

        questionFragmentPresenter!!.passBundle(category!!, difficulty!!, type!!)
        //to set question, first get token
        questionFragmentPresenter!!.getToken(activity)

        view!!.btn_roll.setOnClickListener {
            questionFragmentPresenter!!.setQuestion(
                activity!!.getSharedPreferences(
                    "token1",
                    Context.MODE_PRIVATE
                ).getString("token", "")!!
            )
        }

        cv_answer_1.setOnClickListener {
            questionFragmentPresenter!!.checkAnswer(tv_answer_1.text.toString())
        }

        cv_answer_2.setOnClickListener {
            questionFragmentPresenter!!.checkAnswer(tv_answer_2.text.toString())
        }

        cv_answer_3.setOnClickListener {
            questionFragmentPresenter!!.checkAnswer(tv_answer_3.text.toString())
        }

        cv_answer_4.setOnClickListener {
            questionFragmentPresenter!!.checkAnswer(tv_answer_4.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        questionFragmentPresenter!!.detachView()
    }

    override fun resetAnswerBackground() {
        cv_answer_1.setCardBackgroundColor(Color.WHITE)
        cv_answer_2.setCardBackgroundColor(Color.WHITE)
        cv_answer_3.setCardBackgroundColor(Color.WHITE)
        cv_answer_4.setCardBackgroundColor(Color.WHITE)
    }

    override fun setAnswer(type: String, correctAns: String, incorrectAns: ArrayList<String>) {

        if (type.equals("boolean")) {
            var position = listOf(correctAns, incorrectAns[0]).shuffled()

            tv_answer_1.text = position[0]
            tv_answer_2.text = position[1]
        } else {
            var position = listOf(correctAns, incorrectAns[0], incorrectAns[1], incorrectAns[2]).shuffled()

            tv_answer_1.text = position[0]
            tv_answer_2.text = position[1]
            tv_answer_3.text = position[2]
            tv_answer_4.text = position[3]
        }
    }

    override fun setAnswerFeedback(correct: Boolean, selectedAns: String) {
        var colorList: ArrayList<Int> = arrayListOf(Color.GREEN, Color.RED)

        var color = if (correct) {
            colorList[0]
        } else {
            colorList[1]
        }

        when {
            tv_answer_1.text.equals(selectedAns) -> {
                cv_answer_1!!.setCardBackgroundColor(color)
                if (!correct) {
                    cv_answer_1.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.animation))
                }
            }
            tv_answer_2.text.equals(selectedAns) -> {
                cv_answer_2.setCardBackgroundColor(color)
                if (!correct) {
                    cv_answer_2.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.animation))
                }
            }
            tv_answer_3.text.equals(selectedAns) -> {
                cv_answer_3.setCardBackgroundColor(color)
                if (!correct) {
                    cv_answer_3.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.animation))
                }
            }
            tv_answer_4.text.equals(selectedAns) -> {
                cv_answer_4.setCardBackgroundColor(color)
                if (!correct) {
                    cv_answer_4.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.animation))
                }
            }
        }
    }

    override fun setAnswerVisible(visible: Boolean, number: Int) {
        var valueArray = arrayListOf<Int>(View.VISIBLE, View.INVISIBLE)
        var visibleValue: Int = if (visible) {
            0
        } else {
            1
        }

        if (number == 2) {   //for boolean's question
            cv_answer_1.visibility = valueArray[visibleValue]
            cv_answer_2.visibility = valueArray[visibleValue]
        } else if (number == 4) { //for multiple's question
            cv_answer_1.visibility = valueArray[visibleValue]
            cv_answer_2.visibility = valueArray[visibleValue]
            cv_answer_3.visibility = valueArray[visibleValue]
            cv_answer_4.visibility = valueArray[visibleValue]
        }
    }

    override fun setDifficulty(difficulty: String) {
        tv_difficulty.text = difficulty
        //set color
        val drawable = tv_difficulty.getBackground() as GradientDrawable
        when (difficulty.toLowerCase()) {
            "easy" -> drawable.setColor(Color.GREEN)
            "medium" -> drawable.setColor(Color.parseColor("#FFA500"))
            "hard" -> drawable.setColor(Color.RED)
        }
    }

    override fun setQuestion(question: String) {
        tv_question.text = question
    }

    override fun setQuestionProgressBarVisible(visible: Boolean) {
        if (visible) {
            pb_question.visibility = View.VISIBLE
        } else {
            pb_question.visibility = View.INVISIBLE
        }
    }

    override fun setQuestionVisible(visible: Boolean) {
        if (visible) {
            tv_question.visibility = View.VISIBLE
            tv_difficulty.visibility = View.VISIBLE
        } else {
            tv_question.visibility = View.INVISIBLE
            tv_difficulty.visibility = View.INVISIBLE
        }
    }

    override fun setRollButtonEnable(enable: Boolean) {
        if (enable) {
            btn_roll.isEnabled = true
            btn_roll.setBackgroundColor(Color.parseColor("#D81B60"))
        } else {
            btn_roll.isEnabled = false
            btn_roll.setBackgroundColor(Color.LTGRAY)
        }
    }

    override fun setSnackbar(text: String) {
        Snackbar
            .make(view!!, text, Snackbar.LENGTH_INDEFINITE)
            .setAction("Back") {
                view!!.findNavController()?.navigate(R.id.action_back)
            }.show()
    }

    override fun showError(errorCode: String) {
        Toast.makeText(activity, errorCode, Toast.LENGTH_LONG).show()
    }

}