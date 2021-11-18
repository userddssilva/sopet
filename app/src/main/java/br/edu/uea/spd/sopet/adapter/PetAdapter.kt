package br.edu.uea.spd.sopet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.uea.spd.sopet.R
import br.edu.uea.spd.sopet.data.Pet

class PetAdapter(private val dataSet: List<Pet>) :
    RecyclerView.Adapter<PetAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pet, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataSet[position]
        holder.profileName.text = item.profileName
        holder.profileImg.setImageResource(item.profileImg)
    }

    override fun getItemCount(): Int = dataSet.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profileName: TextView = view.findViewById(R.id.item_profile_name)
        val profileImg: ImageView = view.findViewById(R.id.item_img_pet)
    }
}