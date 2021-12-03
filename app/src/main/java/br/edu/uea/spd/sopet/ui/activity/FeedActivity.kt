package br.edu.uea.spd.sopet.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.uea.spd.sopet.R
import br.edu.uea.spd.sopet.adapter.PetAdapter
import br.edu.uea.spd.sopet.data.Datasource
import br.edu.uea.spd.sopet.data.Pet

class FeedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home)
        title = "Pets"

        val recycleView: RecyclerView = findViewById(R.id.rc_pet_list)
        val dataSet: List<Pet> = Datasource.loadPets()
        val adapter = PetAdapter(dataSet) { openDetailClassWhenClickItem(it) }
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.adapter = adapter
    }

    private fun openDetailClassWhenClickItem(pet: Pet) {
        startActivity(Intent(this, CardDetailsActivity::class.java))
    }
}