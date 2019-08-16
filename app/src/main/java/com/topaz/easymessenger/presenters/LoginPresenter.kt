package com.topaz.easymessenger.presenters

import com.topaz.easymessenger.contracts.LoginContract
import com.topaz.easymessenger.models.LoginModel

class LoginPresenter(private val view: LoginContract.View) : LoginContract.Presenter, LoginContract.OnLoginListener {
    private val model = LoginModel(this)
    override fun onSuccess() {
        view.onSuccess()
    }

    override fun onFailure(message: String) {
        view.onFailure(message)
    }

    override fun performLogin(email: String, password: String) {
        model.loginWithEmailAndPassword(email, password)
    }
}