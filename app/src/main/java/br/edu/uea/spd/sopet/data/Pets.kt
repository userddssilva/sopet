package br.edu.uea.spd.sopet.data

import android.content.res.Resources
import br.edu.uea.spd.sopet.R

fun pets(resources: Resources) : List<Pet> {
    return listOf(
        Pet(id = 1, profileName = "Cachorro 1", profileImg = resources.getStrin(R.drawable.img_pet_photo),
        Pet(id = 2, profileName = "Cachorro 2", profileImg = resources.getStrin(R.drawable.img_pet_photo)
    )
}