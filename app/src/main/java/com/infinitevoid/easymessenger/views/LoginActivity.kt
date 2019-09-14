package com.infinitevoid.easymessenger.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.infinitevoid.easymessenger.R
import com.infinitevoid.easymessenger.contracts.LoginContract
import com.infinitevoid.easymessenger.presenters.LoginPresenter
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginContract.View, LoginContract.OnLoginListener {

    companion object {
        const val TAG = "LOGIN"
    }

    private val presenter = LoginPresenter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login.isClickable = true
        login.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this@LoginActivity, "Please fill all fields", Toast.LENGTH_LONG)
                    .show()
            }
            Log.d(TAG, "Attempt to login with $email and $password")
            login.isClickable = false
            presenter.performLogin(email, password)
        }

        need_to_register.setOnClickListener {
            finish()
        }
    }

    override fun onSuccess() {
        Toast.makeText(this@LoginActivity, "Logged in successfully", Toast.LENGTH_LONG).show()
        val intent = Intent(this, LatestMessagesActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onFailure(message: String) {
        login.isClickable = true
        Toast.makeText(this@LoginActivity, "Login failed: $message", Toast.LENGTH_LONG).show()
    }
}
