package com.topaz.easymessenger.views

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.topaz.easymessenger.R
import com.topaz.easymessenger.contracts.LoginContract
import com.topaz.easymessenger.presenters.LoginPresenter
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginContract.View, LoginContract.OnLoginListener {

    companion object {
        const val TAG = "LOGIN"
    }

    private val presenter = LoginPresenter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this@LoginActivity, "Please fill all fields", Toast.LENGTH_LONG)
                    .show()
            }
            Log.d(TAG, "Attempt to login with $email and $password")
            presenter.performLogin(email, password)
        }

        need_to_register.setOnClickListener {
            finish()
        }
    }

    override fun onSuccess() {
        Toast.makeText(this@LoginActivity, "Logged in successfully", Toast.LENGTH_LONG).show()
    }

    override fun onFailure(message: String) {
        Toast.makeText(this@LoginActivity, "Login failed: $message", Toast.LENGTH_LONG).show()
    }
}
