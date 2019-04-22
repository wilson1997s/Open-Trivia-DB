package com.example.opentriviadbapp.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.opentriviadbapp.R
import com.example.opentriviadbapp.model.Category
import kotlinx.android.synthetic.main.item_quiz_count.view.*

class QuestionCountAdapter(val context: Context, val responseList: ArrayList<Category>) :
    RecyclerView.Adapter<QuestionCountAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_quiz_count, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return responseList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val questionCount = responseList[position]
        holder.setData(questionCount, position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setData(response: Category?, position: Int) {
            itemView.tv_category_name.text = response!!.name
            itemView.tv_category_easy.text = "Easy: " + response!!.totalEasy.toString()
            itemView.tv_category_medium.text = "Medium: " + response!!.totalMedium.toString()
            itemView.tv_category_hard.text = "Hard: " + response!!.totalHard.toString()
            itemView.tv_total_count.text = response!!.totalQuestion.toString()
        }
    }


}