package br.edu.uea.spd.sopet.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.edu.uea.spd.sopet.R
import com.google.android.material.button.MaterialButton

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnLogin = findViewById<MaterialButton>(R.id.btn_acessar)

        btnLogin.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}