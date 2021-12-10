package br.edu.uea.spd.sopet.data.model

import androidx.annotation.DrawableRes

data class Pet(
    val id: Int,
    val profileName: String,
    @DrawableRes val profileImg: Int
)