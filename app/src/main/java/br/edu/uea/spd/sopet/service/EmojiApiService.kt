package br.edu.uea.spd.sopet.service

import retrofit2.Call
import retrofit2.http.*


interface EmojiApiService {
    @GET("emojis/{emoji-slug}")
    fun getEmoji(
        @Path("emoji-slug") slug: String, @Query("access_key") accessKey: String
    ): Call<List<Emoji>>

    @POST("receber_dados")
    fun sendMessage(@Body body: BodyRequisition): Call<Emoji2>
}