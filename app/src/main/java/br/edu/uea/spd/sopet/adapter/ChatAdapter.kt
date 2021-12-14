package br.edu.uea.spd.sopet.adapter

import android.text.format.DateFormat
import android.util.Log
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
import java.util.*


class ChatAdapter(
    private val dataset: List<Chat>,
    private val imageUrl: String,
) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private val TAG: String? = ChatAdapter::class.java.simpleName

    private lateinit var firebaseUser: FirebaseUser

    companion object {
        const val MSG_TYPE_LEFT = 0
        const val MSG_TYPE_RIGHT = 1
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mTvTimestamp: TextView = view.findViewById(R.id.tv_timestamp)
        val mTvConfirmDelivered: TextView = view.findViewById(R.id.tv_confirm_delivered)
        val mTvMessage: TextView = view.findViewById(R.id.tv_message)
        val mIvProfile: ImageView = view.findViewById(R.id.iv_profile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = if (viewType == MSG_TYPE_RIGHT) {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_right, parent, false)

        } else {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_left, parent, false)

        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val datetime: String?
        val message = dataset[position].message
        val timestamp = dataset[position].timestamp
        val cal = Calendar.getInstance(Locale.ENGLISH)

        cal.timeInMillis = (timestamp?.toLongOrNull() ?: 0)
        datetime = DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString()

        holder.mTvMessage.text = message
        holder.mTvTimestamp.text = datetime

        try {
            Log.d(TAG, "Error when load a image to activity")
            Picasso.get().load(imageUrl).into(holder.mIvProfile)
        } catch (e: Exception) {
            Log.d(TAG, "Error when load a image to activity")
        }

        if (position == dataset.size - 1) {
            if (dataset[position].isSeen) {
                holder.mTvConfirmDelivered.text = "Seen"
            } else {
                holder.mTvConfirmDelivered.text = "Delivered"
            }
        } else {
            holder.mTvConfirmDelivered.visibility = View.GONE
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