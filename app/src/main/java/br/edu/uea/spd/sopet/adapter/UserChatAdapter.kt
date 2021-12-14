package br.edu.uea.spd.sopet.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.uea.spd.sopet.R
import br.edu.uea.spd.sopet.data.model.User
import br.edu.uea.spd.sopet.ui.activity.ChatActivity
import com.squareup.picasso.Picasso
import java.lang.Exception

class UserChatAdapter(
    private val dataset: List<User>,
//    private val listener: (User) -> Unit,
) : RecyclerView.Adapter<UserChatAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val hIvAvatar: ImageView = view.findViewById(R.id.iv_avatar)
        val hTvName: TextView = view.findViewById(R.id.tv_user_name)
        val hTvEmail: TextView = view.findViewById(R.id.tv_email)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_chat, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userUid = dataset[position].uid
        val userImage = dataset[position].image
        val userName = dataset[position].name
        val userEmail = dataset[position].email

        holder.hTvName.text = userName
        holder.hTvEmail.text = userEmail

        try {
            Picasso.get().load(userImage)
                .placeholder(R.drawable.ic_default_img_while)
                .into(holder.hIvAvatar)
        } catch (e: Exception) {

        }

        holder.itemView.setOnClickListener {
//            Toast.makeText(context, userEmail, Toast.LENGTH_SHORT).show()
            val intent = Intent(holder.itemView.context, ChatActivity::class.java)
            intent.putExtra("userID", userUid)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = dataset.size
}