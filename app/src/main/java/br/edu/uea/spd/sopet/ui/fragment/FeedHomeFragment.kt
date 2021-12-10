package br.edu.uea.spd.sopet.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.uea.spd.sopet.ui.activity.PostDetailsActivity
import br.edu.uea.spd.sopet.R
import br.edu.uea.spd.sopet.adapter.PetAdapter
import br.edu.uea.spd.sopet.data.model.Datasource
import br.edu.uea.spd.sopet.data.model.Pet

class FeedHomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_feed_home, container, false)

        val recycleView: RecyclerView = view.findViewById(R.id.rc_pet_list)
        val dataSet: List<Pet> = Datasource.loadPets()
        val adapter = PetAdapter(dataSet) { openDetailClassWhenClickItem(it) }
        recycleView.layoutManager = LinearLayoutManager(context)
        recycleView.adapter = adapter

        return view
    }

    private fun openDetailClassWhenClickItem(pet: Pet) {
        startActivity(Intent(context, PostDetailsActivity::class.java))

    }
}