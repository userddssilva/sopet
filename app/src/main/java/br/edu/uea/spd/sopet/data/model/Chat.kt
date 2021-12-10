package br.edu.uea.spd.sopet.data.model

data class Chat(
    val message: String? = "",
    val receiver: String? = "",
    val sender: String? = "",
    val timestamp: String? = "",
    val isSeen: Boolean = false,
)