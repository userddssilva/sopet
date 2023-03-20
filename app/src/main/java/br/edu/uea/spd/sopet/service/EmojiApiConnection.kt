package br.edu.uea.spd.sopet.service

import android.util.Log
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EmojiApiConnection {
    companion object {
        private const val APIKEY = "9acdc8b6eb82a14595c8727faf884307f852a50f"

        private val TAG = EmojiApiConnection::class.java.simpleName

        private var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://emoji-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        private var myApiService: EmojiApiService = retrofit.create(EmojiApiService::class.java)

        fun setEmoji(tvEmoji: TextView, slug: String = "globe-showing-europe-africa") {
            myApiService.getEmoji(slug, APIKEY).enqueue(object : Callback<List<Emoji>> {
                override fun onResponse(
                    call: Call<List<Emoji>>, response: Response<List<Emoji>>
                ) {
                    val data = response.body()
                    tvEmoji.text = data?.get(0)?.character
                    Log.d(TAG, data.toString())
                }

                override fun onFailure(call: Call<List<Emoji>>, t: Throwable) {
                    Log.d(TAG, t.stackTraceToString())
                }
            })
        }
    }
}