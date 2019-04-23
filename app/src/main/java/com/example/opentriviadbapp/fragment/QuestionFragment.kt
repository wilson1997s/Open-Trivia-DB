package com.example.opentriviadbapp.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.findNavController
import com.example.opentriviadbapp.R
import com.example.opentriviadbapp.retrofit.ApiRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_question.*
import kotlinx.android.synthetic.main.fragment_question.view.*


class QuestionFragment : Fragment() {

    private var disposable: Disposable? = null
    var correctAnswer: String = ""
    var token = "0A"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_question, container, false)
        var sharedPref = activity?.getSharedPreferences("token", Context.MODE_PRIVATE)
        token = sharedPref!!.getString("token", "0A")

        if (token.equals("0A")) {
            getNewToken()
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val category = arguments!!.getString("category")
        val difficulty = arguments!!.getString("difficulty")
        val type = arguments!!.getString("type")

        view!!.btn_roll.setOnClickListener {
            setQuestion(token, category, difficulty, type)
        }
        setQuestion(token, category, difficulty, type)

        cv_answer1.setOnClickListener {
            if (tv_answer1.text.equals(correctAnswer)) {
                cv_answer1.setCardBackgroundColor(Color.GREEN)
                cv_answer2.setCardBackgroundColor(Color.WHITE)
                cv_answer3.setCardBackgroundColor(Color.WHITE)
                cv_answer4.setCardBackgroundColor(Color.WHITE)
            } else {
                cv_answer1.setCardBackgroundColor(Color.RED)
                cv_answer2.setCardBackgroundColor(Color.WHITE)
                cv_answer3.setCardBackgroundColor(Color.WHITE)
                cv_answer4.setCardBackgroundColor(Color.WHITE)
                cv_answer1.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.animation))
            }
        }

        cv_answer2.setOnClickListener {
            if (tv_answer2.text.equals(correctAnswer)) {
                cv_answer1.setCardBackgroundColor(Color.WHITE)
                cv_answer2.setCardBackgroundColor(Color.GREEN)
                cv_answer3.setCardBackgroundColor(Color.WHITE)
                cv_answer4.setCardBackgroundColor(Color.WHITE)
            } else {
                cv_answer1.setCardBackgroundColor(Color.WHITE)
                cv_answer2.setCardBackgroundColor(Color.RED)
                cv_answer3.setCardBackgroundColor(Color.WHITE)
                cv_answer4.setCardBackgroundColor(Color.WHITE)
                cv_answer2.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.animation))
            }
        }

        cv_answer3.setOnClickListener {
            if (tv_answer3.text.equals(correctAnswer)) {
                cv_answer1.setCardBackgroundColor(Color.WHITE)
                cv_answer2.setCardBackgroundColor(Color.WHITE)
                cv_answer3.setCardBackgroundColor(Color.GREEN)
                cv_answer4.setCardBackgroundColor(Color.WHITE)
            } else {
                cv_answer1.setCardBackgroundColor(Color.WHITE)
                cv_answer2.setCardBackgroundColor(Color.WHITE)
                cv_answer3.setCardBackgroundColor(Color.RED)
                cv_answer4.setCardBackgroundColor(Color.WHITE)
                cv_answer3.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.animation))
            }
        }

        cv_answer4.setOnClickListener {
            if (tv_answer4.text.equals(correctAnswer)) {
                cv_answer1.setCardBackgroundColor(Color.WHITE)
                cv_answer2.setCardBackgroundColor(Color.WHITE)
                cv_answer3.setCardBackgroundColor(Color.WHITE)
                cv_answer4.setCardBackgroundColor(Color.GREEN)
            } else {
                cv_answer1.setCardBackgroundColor(Color.WHITE)
                cv_answer2.setCardBackgroundColor(Color.WHITE)
                cv_answer3.setCardBackgroundColor(Color.WHITE)
                cv_answer4.setCardBackgroundColor(Color.RED)
                cv_answer4.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.animation))
            }
        }

    }

    fun getNewToken() {
        disposable =
            ApiRepo.getToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        var sharedPref = activity?.getSharedPreferences("token", Context.MODE_PRIVATE)
                        with(sharedPref!!.edit()) {
                            putString("token", result.token)
                            commit()
                        }
                        token = result.token
                    }, { error -> println("Error:" + error.message) }
                )
    }


    fun setQuestion(token: String, category: String, difficulty: String, type: String) {
        btn_roll.isEnabled = false
        btn_roll.setBackgroundColor(Color.LTGRAY)
        pb_question.visibility = View.VISIBLE
        cv_answer1.visibility = View.INVISIBLE
        cv_answer2.visibility = View.INVISIBLE
        cv_answer3.visibility = View.INVISIBLE
        cv_answer4.visibility = View.INVISIBLE
        tv_question.visibility = View.INVISIBLE
        tv_difficulty.visibility = View.INVISIBLE
        cv_answer1.setCardBackgroundColor(Color.WHITE)
        cv_answer2.setCardBackgroundColor(Color.WHITE)
        cv_answer3.setCardBackgroundColor(Color.WHITE)
        cv_answer4.setCardBackgroundColor(Color.WHITE)

        disposable =
            ApiRepo.getQuestion(token, category, difficulty, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        if (result.responseCode == 0) {

                            var selectedQuestion = result.results[0]
                            var type = selectedQuestion.type
                            var difficulty = selectedQuestion.difficulty
                            var question = Html.fromHtml(selectedQuestion.question).toString()
                            correctAnswer = Html.fromHtml(selectedQuestion.correctAnswer).toString()
                            var tempIncorrectAnswer = selectedQuestion.incorrectAnswer

                            var incorrectAnswer = listOf<String>()

                            tempIncorrectAnswer.forEach {
                                incorrectAnswer += Html.fromHtml(it).toString()
                            }

                            tv_question.text = question
                            tv_difficulty.text = difficulty

                            val drawable = tv_difficulty.getBackground() as GradientDrawable

                            if (difficulty.equals("easy")) {
                                drawable.setColor(Color.GREEN)
                            } else if (difficulty.equals("medium")) {
                                drawable.setColor(Color.parseColor("#FFA500"))
                            } else {
                                drawable.setColor(Color.RED)
                            }

                            if (type.equals("boolean")) {
                                cv_answer1.visibility = View.VISIBLE
                                cv_answer2.visibility = View.VISIBLE

                                var tempPosition = listOf(correctAnswer, incorrectAnswer[0])

                                var randomNo1 = 0
                                var randomNo2 = 0
                                do {
                                    randomNo1 = (0..1).random()
                                    randomNo2 = (0..1).random()
                                } while (randomNo1 == randomNo2)

                                tv_answer1.text = tempPosition[randomNo1].toString()
                                tv_answer2.text = tempPosition[randomNo2].toString()

                            } else {
                                cv_answer1.visibility = View.VISIBLE
                                cv_answer2.visibility = View.VISIBLE
                                cv_answer3.visibility = View.VISIBLE
                                cv_answer4.visibility = View.VISIBLE

                                var tempPosition =
                                    listOf(correctAnswer, incorrectAnswer[0], incorrectAnswer[1], incorrectAnswer[2])

                                var randomNo1 = 0
                                var randomNo2 = 0
                                var randomNo3 = 0
                                var randomNo4 = 0

                                do {
                                    randomNo1 = (0..3).random()
                                    randomNo2 = (0..3).random()
                                    randomNo3 = (0..3).random()
                                    randomNo4 = (0..3).random()
                                } while (randomNo1 == randomNo2
                                    || randomNo1 == randomNo3
                                    || randomNo1 == randomNo4
                                    || randomNo2 == randomNo3
                                    || randomNo2 == randomNo4
                                    || randomNo3 == randomNo4
                                )

                                tv_answer1.text = tempPosition[randomNo1].toString()
                                tv_answer2.text = tempPosition[randomNo2].toString()
                                tv_answer3.text = tempPosition[randomNo3].toString()
                                tv_answer4.text = tempPosition[randomNo4].toString()

                            }

                            pb_question.visibility = View.INVISIBLE
                            btn_roll.isEnabled = true
                            btn_roll.setBackgroundColor(Color.parseColor("#D81B60"))
                            tv_question.visibility = View.VISIBLE
                            tv_difficulty.visibility = View.VISIBLE
                        } else if (result.responseCode == 1) {
                            Snackbar
                                .make(
                                    getView()!!,
                                    "No result found, please refine criteria",
                                    Snackbar.LENGTH_INDEFINITE
                                )
                                .setAction("Back") {
                                    view?.findNavController()?.navigate(R.id.action_back)
                                }.show()
                            pb_question.visibility = View.INVISIBLE
                        } else if (result.responseCode == 3) {
                            getNewToken()
                            setQuestion(token, category, difficulty, type)
                        } else if (result.responseCode == 4) {

                            disposable =
                                ApiRepo.resetToken(token)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                        { result ->
                                        }, { error -> println("Error:" + error.message) }
                                    )
                            setQuestion(token, category, difficulty, type)
                        }
                    },
                    { error -> println("Error:" + error.message) }
                )
    }

}