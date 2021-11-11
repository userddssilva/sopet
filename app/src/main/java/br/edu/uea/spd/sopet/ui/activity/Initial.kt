package br.edu.uea.spd.sopet.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import br.edu.uea.spd.sopet.R

class Initial : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)

        Handler().postDelayed({
            startActivity(Intent(this, Login::class.java))
        }, 2000)
    }
}