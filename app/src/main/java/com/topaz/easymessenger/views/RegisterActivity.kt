package com.topaz.easymessenger.views

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.topaz.easymessenger.R
import com.topaz.easymessenger.data.User
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "Main"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        register.setOnClickListener {
            performRegister()
        }

        select_photo.setOnClickListener {
            Log.d(TAG, "Need to show photo selector")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        already_registered.setOnClickListener {
            Log.d(TAG, "Show login activity")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    var selectedPhotoUri: Uri? = null


    private fun performRegister() {
        val email = email.text.toString()
        val password = password.text.toString()
        val username = username.text.toString()

        if ((email.isEmpty()) || (password.isEmpty()) || (username.isEmpty())) {
            Toast.makeText(this@RegisterActivity, "Please fill all fields", Toast.LENGTH_LONG)
                .show()
        } else {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) {
                        Log.d(TAG, "Error performed ${it.exception?.message}")
                        Toast.makeText(
                            this@RegisterActivity,
                            "Registration failed: ${it.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                        return@addOnCompleteListener
                    }
                    Log.d(TAG, "Added user with uid ${it.result?.user?.uid}")
                    uploadImageToFirebaseStorage()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registration failed: ${it.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d(TAG, "Photo was selected")

            selectedPhotoUri = data.data
            val bitmap =
                MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            select_photo_image.setImageBitmap(bitmap)
            select_photo.alpha = 0f
        }
    }

    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) {
            saveUserToDatabase("")
        } else {
            val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

            ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d(TAG, "Photo loaded with name $filename")
                        saveUserToDatabase(it.toString())
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, it.message)
                }
        }
    }

    private fun saveUserToDatabase(profileImageURL: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, username.text.toString(), profileImageURL)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d(TAG, "Saved user to database")
            }
            .addOnFailureListener {
                Log.d(TAG, it.message)
            }
    }
}
