package com.infinitevoid.easymessenger.presenters

import com.infinitevoid.easymessenger.contracts.LoginContract
import com.infinitevoid.easymessenger.models.LoginModel

class LoginPresenter(private val view: LoginContract.View) : LoginContract.Presenter, LoginContract.OnLoginListener {
    private val model = LoginModel(this)
    override fun onSuccess() {
        view.onSuccess()
    }

    override fun onFailure(message: String) {
        view.onFailure(message)
    }

    override fun onMailSent() {
        view.onMailSent()
    }

    override fun performLogin(email: String, password: String) {
        model.loginWithEmailAndPassword(email, password)
    }

    override fun sendPasswordResetMail(email: String) {
        model.sendPasswordResetMail(email)
    }
}