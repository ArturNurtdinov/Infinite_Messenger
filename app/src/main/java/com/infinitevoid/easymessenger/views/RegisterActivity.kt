package com.infinitevoid.easymessenger.views

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.infinitevoid.easymessenger.R
import com.infinitevoid.easymessenger.contracts.RegisterContract
import com.infinitevoid.easymessenger.presenters.RegisterPresenter
import kotlinx.android.synthetic.main.activity_main.*

class RegisterActivity : AppCompatActivity(), RegisterContract.View {
    companion object {
        const val TAG = "Main"
    }

    private val presenter = RegisterPresenter(this)
    private var selectedPhotoUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        register.isClickable = true
        already_registered.isClickable = true
        register.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            val username = username.text.toString()

            if ((email.isEmpty()) || (password.isEmpty()) || (username.isEmpty())) {
                Toast.makeText(this@RegisterActivity, "Please fill all fields", Toast.LENGTH_LONG)
                    .show()
            } else {
                presenter.performRegister(email, password, username, selectedPhotoUri)
                register.isClickable = false
                already_registered.isClickable = false
            }
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

    override fun onSuccess() {
        Toast.makeText(this, getString(R.string.registration_success), Toast.LENGTH_LONG).show()
        val intent = Intent(this, LatestMessagesActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onFailure(message: String) {
        val failText = getString(R.string.registration_failed)
        Toast.makeText(this, "$failText $message", Toast.LENGTH_LONG).show()
        register.isClickable = true
        already_registered.isClickable = true
    }
}
