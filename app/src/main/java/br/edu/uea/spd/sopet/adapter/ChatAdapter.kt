package br.edu.uea.spd.sopet.adapter

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.uea.spd.sopet.R
import br.edu.uea.spd.sopet.data.model.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import java.lang.Exception
import java.util.*


class ChatAdapter(
    private val dataset: List<Chat>,
    private val imageUrl: String,
) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private lateinit var firebaseUser: FirebaseUser

    companion object {
        const val MSG_TYPE_LEFT = 0
        const val MSG_TYPE_RIGHT = 1
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val hTvTimestamp: TextView = view.findViewById(R.id.tv_timestamp)
        val hTvConfirmDelivered: TextView = view.findViewById(R.id.tv_confirm_delivered)
        val hTvMessage: TextView = view.findViewById(R.id.tv_message)
        val hIvProfile: ImageView = view.findViewById(R.id.iv_profile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = if (viewType == MSG_TYPE_RIGHT) {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_chat_right, parent, false)

        } else {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_chat_left, parent, false)

        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = dataset[position].message
        val timestamp = dataset[position].timestamp

        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = timestamp?.toLongOrNull()!!
        val datetime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString()

        holder.hTvMessage.text = message
        holder.hTvTimestamp.text = datetime

        try {
            Picasso.get().load(imageUrl).into(holder.hIvProfile)
        } catch (e: Exception) {

        }

        if (position == dataset.size-1) {
            if (dataset[position].isSeen) {
                holder.hTvConfirmDelivered.text = "Seen"
            } else {
                holder.hTvConfirmDelivered.text = "Delivered"
            }
        } else {
            holder.hTvConfirmDelivered.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = dataset.size

    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        return if (dataset[position].sender.equals(firebaseUser.uid)) {
            MSG_TYPE_RIGHT
        } else {
            MSG_TYPE_LEFT
        }
    }
}