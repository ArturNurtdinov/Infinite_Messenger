package com.topaz.easymessenger

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LOGIN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()

            Log.d(TAG, "Attemp to login with $email and $password")
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) {
                        Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_LONG).show()
                        return@addOnCompleteListener
                    }
                    Log.d(TAG, "Successfully logged in with $email and $password")
                }
                .addOnFailureListener {
                    Toast.makeText(this@LoginActivity, "Login failed: ${it.message}", Toast.LENGTH_LONG).show()
                }
        }
        need_to_register.setOnClickListener {
            finish()
        }
    }
}
