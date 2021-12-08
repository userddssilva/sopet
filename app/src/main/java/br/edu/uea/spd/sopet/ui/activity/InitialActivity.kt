package br.edu.uea.spd.sopet.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.edu.uea.spd.sopet.R
import br.edu.uea.spd.sopet.utils.LOGIN_TAG_DEBUG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class InitialActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)
        auth = Firebase.auth
        Handler().postDelayed({ startActivity() }, 2000)
    }

    private fun startActivity() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.d(LOGIN_TAG_DEBUG, "createUserWithEmail:isLogged")
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Log.d(LOGIN_TAG_DEBUG, "createUserWithEmail:isNotLogin")
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}