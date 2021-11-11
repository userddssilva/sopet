package br.edu.uea.spd.sopet.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.edu.uea.spd.sopet.R

class PetList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_pet_list)
    }
}