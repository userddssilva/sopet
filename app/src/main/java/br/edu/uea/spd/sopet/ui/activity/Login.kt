package br.edu.uea.spd.sopet.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.uea.spd.sopet.R
import br.edu.uea.spd.sopet.utils.LOGIN_TAG_DEBUG
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var btnLogin: MaterialButton
    private lateinit var btnSignup: MaterialButton
    private lateinit var itLoginEmail: TextInputEditText
    private lateinit var itLoginPassword: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
        btnLogin = findViewById(R.id.btn_login)
        btnSignup = findViewById(R.id.btn_signup)
        itLoginEmail = findViewById(R.id.it_login_email)
        itLoginPassword = findViewById(R.id.it_login_password)

        btnLogin.setOnClickListener {
            loginUser(itLoginEmail.text.toString(), itLoginPassword.text.toString())
        }

        btnSignup.setOnClickListener {
//            startActivity(Intent(this, MainActivity::class.java))
            createAccount(itLoginEmail.text.toString(), itLoginPassword.text.toString())
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
//            reload();
            Log.d(LOGIN_TAG_DEBUG, "createUserWithEmail:isLogged")
        } else {
            Log.d(LOGIN_TAG_DEBUG, "createUserWithEmail:isNotLogin")
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(LOGIN_TAG_DEBUG, "createUserWithEmail:success")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
//                    val user = auth.currentUser
                    Toast.makeText(baseContext,"Authentication Success.", Toast.LENGTH_SHORT).show()
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(LOGIN_TAG_DEBUG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
//                    updateUI(null)
                }
            }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(LOGIN_TAG_DEBUG, "signInWithEmail:success")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
//                    val user = auth.currentUser
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(LOGIN_TAG_DEBUG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
//                    updateUI(null)
                }
            }
    }
}