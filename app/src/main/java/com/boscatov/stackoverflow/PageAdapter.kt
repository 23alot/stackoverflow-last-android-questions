package com.boscatov.stackoverflow

import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.question.view.*

class PageAdapter(private var pageList: ArrayList<Question>) : RecyclerView.Adapter<PageAdapter.PageViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.question, parent, false)
        return PageViewHolder(view)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.bind(pageList.elementAt(position))
    }

    override fun getItemCount(): Int {
        return pageList.size
    }

    fun setItems(page: ArrayList<Question>) {
        pageList.clear()
        pageList.addAll(page)
    }

    class PageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(question: Question) {
            val color = if(question.answered) {
                itemView.answer.setBackgroundResource(R.drawable.ans)
                itemView.context.resources.getColor(R.color.white)
            } else {
                itemView.answer.setBackgroundResource(R.drawable.unans)
                itemView.context.resources.getColor(R.color.answered)
            }
            itemView.answer_count.setTextColor(color)
            itemView.answer_text.setTextColor(color)
            itemView.answer_count.text = "${question.answerCount}"
            itemView.score.text = "${question.score}"
            itemView.title.text = Html.fromHtml(question.title).toString()
        }
    }

}