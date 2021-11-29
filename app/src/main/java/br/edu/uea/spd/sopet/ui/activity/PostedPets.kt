package br.edu.uea.spd.sopet.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.uea.spd.sopet.R
import br.edu.uea.spd.sopet.adapter.PetAdapter
import br.edu.uea.spd.sopet.data.Datasource
import br.edu.uea.spd.sopet.data.Pet

class PostedPets : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posted_pets)
        title = ""

        val recycleView: RecyclerView = findViewById(R.id.rc_pet_list)
        val dataSet: List<Pet> = Datasource.loadPets()
        val adapter = PetAdapter(dataSet)
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.adapter = adapter
    }
}