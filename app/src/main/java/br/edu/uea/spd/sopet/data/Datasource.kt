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

        fun loadComments(): List<Comment> {
            return listOf(
                Comment(id = 1, time = "2w", text = R.string.placeholder_text_post.toString(), User("1234", "Roberto Justo", "uri")),
                Comment(id = 2, time = "3h", text = R.string.placeholder_text_post.toString(), User("1234", "Neymar junior", "uri")),
                Comment(id = 3, time = "3d", text = R.string.placeholder_text_post.toString(), User("1234", "Jorge Aragão", "uri")),
                Comment(id = 4, time = "7h", text = R.string.placeholder_text_post.toString(), User("1234", "Seu Jorge", "uri")),
                Comment(id = 5, time = "12h", text = R.string.placeholder_text_post.toString(), User("1234", "Mr. Robot", "uri")),
                Comment(id = 6, time = "2h", text = R.string.placeholder_text_post.toString(), User("1234", "Dj Alok", "uri")),
                Comment(id = 7, time = "7W", text = R.string.placeholder_text_post.toString(), User("1234", "Mariana Barbosa", "uri")),
                Comment(id = 8, time = "9W", text = R.string.placeholder_text_post.toString(), User("1234", "Helena de Jesus", "uri")),
                Comment(id = 9, time = "5d", text = R.string.placeholder_text_post.toString(), User("1234", "Caio Android", "uri")),
                Comment(id = 10, time = "1h", text = R.string.placeholder_text_post.toString(), User("1234", "Bob Marley", "uri")),
            )
        }
    }
}