package com.topaz.easymessenger.models

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.topaz.easymessenger.contracts.LoginContract

class LoginModel(private val onLoginListener: LoginContract.OnLoginListener) : LoginContract.Model {
    override fun loginWithEmailAndPassword(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onLoginListener.onSuccess()
            }
            .addOnFailureListener {
                Log.d("LOGIN", it.message)
                onLoginListener.onFailure(it.message.toString())
            }
    }

}