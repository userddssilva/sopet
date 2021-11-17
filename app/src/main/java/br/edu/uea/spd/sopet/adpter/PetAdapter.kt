package br.edu.uea.spd.sopet.adpter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.uea.spd.sopet.R

class PetAdapter(private val dataSet: Array<String>) :
    RecyclerView.Adapter<PetAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val profileName: TextView = view.findViewById(R.id.item_profile_name)
        val districtName: TextView = view.findViewById(R.id.item_district_profile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pet, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.profileName.text = dataSet[position]
        holder.districtName.text = dataSet[position]
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}