package br.edu.uea.spd.sopet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import br.edu.uea.spd.sopet.R
import br.edu.uea.spd.sopet.data.model.Comment

class CommentAdapter(
    private val dataset: List<Comment>,
    private val listener: (Comment) -> Unit,
) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val profileName: TextView = view.findViewById(R.id.tv_comment_user_name)
        val llItemsPost: LinearLayout = view.findViewById(R.id.ll_items_posts)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataset[position]
//        Log.d("User name", item.user.userName)
//        holder.profileName.text =  item.user.userName
//        holder.itemView.setOnClickListener { listener(item) }
        holder.llItemsPost.setOnClickListener {

        }
    }

    override fun getItemCount(): Int = dataset.size
}