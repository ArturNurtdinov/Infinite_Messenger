package com.infinitevoid.easymessenger.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.infinitevoid.easymessenger.R
import com.infinitevoid.easymessenger.contracts.LoginContract
import com.infinitevoid.easymessenger.presenters.LoginPresenter
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.email
import kotlinx.android.synthetic.main.email_to_reset_password.view.*

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
                Toast.makeText(
                    this@LoginActivity,
                    getString(R.string.fill_all_fields),
                    Toast.LENGTH_LONG
                )
                    .show()
            }
            Log.d(TAG, "Attempt to login with $email and $password")
            if (email.isNotEmpty() && password.isNotEmpty()) {
                login.isClickable = false
                presenter.performLogin(email, password)
            } else {
                Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_LONG).show()
            }
        }

        need_to_register.setOnClickListener {
            finish()
        }

        forget_password.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
                .setTitle(getString(R.string.forgot_password))
                .setMessage(getString(R.string.do_you_want_to_reset))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    val view =
                        LayoutInflater.from(this).inflate(R.layout.email_to_reset_password, null)
                    val emailInputDialog = AlertDialog.Builder(this)
                        .setView(view)
                        .setTitle(getString(R.string.enter_email))
                        .setPositiveButton(getString(R.string.reset_password)) { dialog, _ ->
                            presenter.sendPasswordResetMail(view.email.text.toString())
                            email.setText(view.email.text.toString(), TextView.BufferType.EDITABLE)
                            dialog.dismiss()
                        }
                        .setNeutralButton(getString(R.string.cancel)) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                    emailInputDialog.show()
                }
                .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                    dialog.cancel()
                }
                .setNeutralButton(getString(R.string.cancel)) { dialog, _ ->
                    dialog.cancel()
                }
                .create()
            dialog.show()
        }
    }

    override fun onSuccess() {
        Toast.makeText(
            this@LoginActivity,
            getString(R.string.logged_successfully),
            Toast.LENGTH_LONG
        ).show()
        val intent = Intent(this, LatestMessagesActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onFailure(message: String) {
        login.isClickable = true
        Toast.makeText(this@LoginActivity, "Failed: $message", Toast.LENGTH_LONG).show()
    }

    override fun onMailSent() {
        Toast.makeText(
            this@LoginActivity,
            getString(R.string.password_reset_mail_was_sent),
            Toast.LENGTH_LONG
        ).show()
    }
}
