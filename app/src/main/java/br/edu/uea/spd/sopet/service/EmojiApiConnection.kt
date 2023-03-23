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

        private var retrofit: Retrofit = Retrofit.Builder().baseUrl("http://192.168.100.7:5000/")
            .addConverterFactory(GsonConverterFactory.create()).build()

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

        fun sendMessage(
            message: String,
            tvEmoji1: TextView,
            tvEmoji2: TextView,
            tvEmoji3: TextView,
            tvEmoji4: TextView,
            tvEmoji5: TextView,
            tvEmoji6: TextView,
        ) {
            val body = BodyRequisition()
            body.texto = message
            myApiService.sendMessage(body).enqueue(object : Callback<Emoji2> {
                override fun onResponse(call: Call<Emoji2>, response: Response<Emoji2>) {
                    val data = response.body()
                    tvEmoji1.text = data?.emojis?.get(0)
                    tvEmoji2.text = data?.emojis?.get(1)
                    tvEmoji3.text = data?.emojis?.get(2)
                    tvEmoji4.text = data?.emojis?.get(3)
                    tvEmoji5.text = data?.emojis?.get(4)
                    tvEmoji6.text = data?.emojis?.get(5)
                    Log.d(TAG, data.toString())
                }

                override fun onFailure(call: Call<Emoji2>, t: Throwable) {
                    Log.d(TAG, t.stackTraceToString())
                }
            })
        }
    }
}