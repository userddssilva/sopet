package br.edu.uea.spd.sopet.data

import br.edu.uea.spd.sopet.R

class Datasource() {
    companion object {
        fun loadPets(): List<Pet> {
            return listOf(
                Pet(id = 1, profileName = "Cachorro 1", profileImg = R.drawable.img_pet_photo),
                Pet(id = 2, profileName = "Cachorro 2", profileImg = R.drawable.img_pet_photo),
                Pet(id = 3, profileName = "Cachorro 3", profileImg = R.drawable.img_pet_photo),
                Pet(id = 4, profileName = "Cachorro 4", profileImg = R.drawable.img_pet_photo),
                Pet(id = 5, profileName = "Cachorro 5", profileImg = R.drawable.img_pet_photo),
                Pet(id = 6, profileName = "Cachorro 6", profileImg = R.drawable.img_pet_photo),
                Pet(id = 7, profileName = "Cachorro 7", profileImg = R.drawable.img_pet_photo),
                Pet(id = 8, profileName = "Cachorro 8", profileImg = R.drawable.img_pet_photo),
                Pet(id = 9, profileName = "Cachorro 9", profileImg = R.drawable.img_pet_photo),
                Pet(id = 10, profileName = "Cachorro 10", profileImg = R.drawable.img_pet_photo)
            )
        }
    }
}