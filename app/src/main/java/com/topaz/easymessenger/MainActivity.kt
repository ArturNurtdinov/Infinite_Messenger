package com.topaz.easymessenger

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "Main"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        register.setOnClickListener {

            val email = email.text.toString()
            val password = password.text.toString()
            val username = username.text.toString()

            Log.d(TAG, "Email is $email")
            Log.d(TAG, "password is $password")

            if ((email.isEmpty()) || (password.isEmpty()) || (username.isEmpty())) {
                Toast.makeText(this@MainActivity, "Please fill all fields", Toast.LENGTH_LONG)
                    .show()
            } else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (!it.isSuccessful) {
                            Log.d(TAG, "Error performed ${it.exception?.message}")
                            Toast.makeText(
                                this@MainActivity,
                                "Registration failed",
                                Toast.LENGTH_LONG
                            ).show()
                            return@addOnCompleteListener
                        }
                        Toast.makeText(this@MainActivity, "Registration failed", Toast.LENGTH_LONG)
                            .show()
                        Log.d(TAG, "Added user with uid ${it.result?.user?.uid}")
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            this@MainActivity,
                            "Registration failed: ${it.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }
        }

        already_registered.setOnClickListener {
            Log.d(TAG, "Show login activity")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)


        }
    }
}