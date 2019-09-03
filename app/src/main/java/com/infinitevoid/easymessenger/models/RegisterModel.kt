package com.infinitevoid.easymessenger.models

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.infinitevoid.easymessenger.contracts.RegisterContract
import com.infinitevoid.easymessenger.data.User
import com.infinitevoid.easymessenger.views.RegisterActivity
import java.util.*

class RegisterModel(private val listener: RegisterContract.OnRegisterListener) :
    RegisterContract.Model {
    override fun registerWithEmailAndPassword(
        email: String,
        password: String,
        username: String,
        uri: Uri?
    ) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Log.d(RegisterActivity.TAG, "Added user with uid ${it.user?.uid}")
                if (uri != null) {
                    saveProfilePicture(username, uri)
                } else {
                    saveUserToDatabase(username, "")
                }
            }
            .addOnFailureListener {
                Log.d(RegisterActivity.TAG, "Registration failed: ${it.message}")
                listener.onFailure(it.message.toString())
            }
        auth.currentUser?.sendEmailVerification()
    }

    private fun saveProfilePicture(username: String, uri: Uri) {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(uri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    Log.d(RegisterActivity.TAG, "Photo loaded with name $filename")
                    saveUserToDatabase(username, it.toString())
                }
            }
            .addOnFailureListener {
                Log.d(RegisterActivity.TAG, it.message)
                listener.onFailure(it.message.toString())
            }
    }

    private fun saveUserToDatabase(username: String, profileImageURL: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, username, profileImageURL)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d(RegisterActivity.TAG, "Saved user to database")
                listener.onSuccess()
            }
            .addOnFailureListener {
                Log.d(RegisterActivity.TAG, it.message)
                listener.onFailure(it.message.toString())
            }
    }

}