package br.edu.uea.spd.sopet.data.model

data class Comment (
    val id: Int,
    val time: String,
    val text: String,
    val user: User,
)