package br.edu.uea.spd.sopet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.uea.spd.sopet.R
import br.edu.uea.spd.sopet.adapter.DetailsCommentAdapter.ViewHolder
import br.edu.uea.spd.sopet.data.Comment


class DetailsCommentAdapter(
    private val dataset: List<Comment>,
    private val listener: (Comment) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int = dataset.size

    interface OnItemClickListener {
        fun onItemClick(item: Comment)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
}