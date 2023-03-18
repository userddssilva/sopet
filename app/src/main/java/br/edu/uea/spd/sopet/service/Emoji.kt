package br.edu.uea.spd.sopet.service

data class Emoji(
    val slug: String,
    val character: String,
    val unicodeName: String,
    val codePoint: String,
    val group: String,
    val subGroup: String
)