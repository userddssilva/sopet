package br.edu.uea.spd.sopet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.uea.spd.sopet.R
import br.edu.uea.spd.sopet.data.model.Post
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class FeedAdapter(
    private val dataset: ArrayList<HashMap<String, String>>,
    private val listener: (Post) -> Unit
) : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firebaseUser: FirebaseUser = firebaseAuth.currentUser!!
    init {
        dataset.reverse()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mCIvProfile: CircleImageView = view.findViewById(R.id.feed_item_post_civ_profile_pic)
        val mTvUserName: TextView = view.findViewById(R.id.feed_item_post_tv_user_name)
        val mTvDescription: TextView = view.findViewById(R.id.feed_item_post_tv_description_post)
        val mIvPost: ImageView = view.findViewById(R.id.feed_item_post_iv_post)
        val mBtnFollow: MaterialButton = view.findViewById(R.id.feed_item_post_btn_follow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post_details_feed, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemPost = dataset[position]

        // set text of publication item
        holder.mTvDescription.text = itemPost["pDescr"]
        holder.mTvUserName.text = itemPost["uName"]

        // hide button follow to logged user
        if (firebaseUser.uid == itemPost["uid"]) {
            holder.mBtnFollow.visibility = View.GONE
        }

        //  load images of item publication
        try {
            // If image is received then set
            Picasso.get().load(itemPost["pImage"]).into(holder.mIvPost)
        } catch (e: Exception) {
            // If there is any exception hide image
            holder.mIvPost.visibility = View.GONE
        }

        //  load images of item publication
        try {
            // If image is received then set
            Picasso.get().load(itemPost["uDp"]).into(holder.mCIvProfile)
        } catch (e: Exception) {
            // If there is any exception hide image
//            holder.mIvPost.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = dataset.size


}