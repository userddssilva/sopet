package br.edu.uea.spd.sopet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import br.edu.uea.spd.sopet.R
import br.edu.uea.spd.sopet.data.model.Pet

class PetAdapter(
    private val dataset: List<Pet>,
    private val listener: (Pet) -> Unit
) : RecyclerView.Adapter<PetAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val profileName: TextView = view.findViewById(R.id.item_profile_name)
//        val profileImg: ImageView = view.findViewById(R.id.item_img_pet)
        //        val profileName: TextView = view.findViewById(R.id.item_profile_name)
        val mllItemsPost: LinearLayout = view.findViewById(R.id.ll_items_posts)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post_details_feed, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataset[position]
//        holder.profileName.text = item.profileName
//        holder.profileImg.setImageResource(item.profileImg)
//        holder.itemView.setOnClickListener { listener(item) }
        holder.mllItemsPost.setOnClickListener {
            listener(item)
        }
    }

    override fun getItemCount(): Int = dataset.size

    interface OnItemClickListener {
        fun onItemClick(item: Pet?)
    }
}