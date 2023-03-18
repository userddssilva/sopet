package br.edu.uea.spd.sopet.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface EmojiApiService {
    @GET("emojis/{emoji-slug}")
    fun getEmoji(
        @Path("emoji-slug") slug: String,
        @Query("access_key") accessKey: String
    ): Call<List<Emoji>>
}