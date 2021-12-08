package br.edu.uea.spd.sopet.ui.activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.uea.spd.sopet.R
import br.edu.uea.spd.sopet.utils.LOGIN_TAG_DEBUG
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private val RC_SIGN_IN: Int = 100
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var btnLogin: MaterialButton
    private lateinit var btnSignup: MaterialButton
    private lateinit var btnGoogleLogin: SignInButton
    private lateinit var btnForgotPassword: MaterialButton
    private lateinit var itLoginEmail: TextInputEditText
    private lateinit var itlLoginEmail: TextInputLayout
    private lateinit var itLoginPassword: TextInputEditText
    private lateinit var itlLoginPassword: TextInputLayout
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        progressDialog = ProgressDialog(this)

        btnLogin = findViewById(R.id.btn_login)
        btnSignup = findViewById(R.id.btn_signup)
        btnGoogleLogin = findViewById(R.id.btn_google_login)
        btnForgotPassword = findViewById(R.id.btn_forgot_password)
        itLoginEmail = findViewById(R.id.it_login_email)
        itlLoginEmail = findViewById(R.id.itl_login_email)
        itLoginPassword = findViewById(R.id.it_login_password)
        itlLoginPassword = findViewById(R.id.itl_login_password)


        btnLogin.setOnClickListener {
            if (isInputsCredentialsRight()) {
                loginUser(itLoginEmail.text.toString(), itLoginPassword.text.toString())
            }

        }

        btnSignup.setOnClickListener {
            if (isInputsCredentialsRight()) {
                createAccount(itLoginEmail.text.toString(), itLoginPassword.text.toString())
            }
        }

        btnGoogleLogin.setOnClickListener {
            loginUserGoogle()
        }

        btnForgotPassword.setOnClickListener {
            showRecoveryPasswordDialog()
        }
    }

    private fun showRecoveryPasswordDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Recover Password")

        val linearLayout = LinearLayout(this)
        val editText = EditText(this)
        editText.hint = "Recover Email"
        editText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        editText.minEms = 10

        linearLayout.addView(editText)
        linearLayout.setPadding(10, 10, 10, 10)

        builder.setView(linearLayout)

        builder.setPositiveButton("Recover") { _, _ ->
            val email = editText.text.toString()
            beginRecovery(email)
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun beginRecovery(email: String) {
        progressDialog.setMessage(getString(R.string.dialog_sending_email))
        progressDialog.show()
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                progressDialog.dismiss()
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        getString(R.string.email_sent),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.toast_dont_sent_email),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { exception ->
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun isInputsCredentialsRight(): Boolean {
        val email: String = itLoginEmail.text.toString()
        val password: String = itLoginPassword.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            itlLoginEmail.error = "Invalid Email"
            return false
        }
        if (password.length < 6) {
            itlLoginPassword.error = "The pass must be length > 6"
            return false
        }
        return true
    }

    private fun createAccount(email: String, password: String) {
        progressDialog.setMessage(getString(R.string.dialog_creating_account))
        progressDialog.show()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressDialog.dismiss()
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(LOGIN_TAG_DEBUG, "createUserWithEmail:success")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(LOGIN_TAG_DEBUG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(this, getString(R.string.toast_autenticaton_failed), Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun loginUser(email: String, password: String) {
        progressDialog.setMessage(getString(R.string.dialog_logging_in))
        progressDialog.show()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressDialog.dismiss()
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(LOGIN_TAG_DEBUG, "signInWithEmail:success")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(LOGIN_TAG_DEBUG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(this, getString(R.string.toast_autenticaton_failed), Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun loginUserGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("Login Google", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("Login Google", "Google sign in failed", e)
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Login Google", "signInWithCredential:success")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Login Google", "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, getString(R.string.toast_autenticaton_failed), Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

}