package com.topaz.easymessenger.presenters

import android.net.Uri
import com.topaz.easymessenger.contracts.RegisterContract
import com.topaz.easymessenger.models.RegisterModel

class RegisterPresenter(private val view: RegisterContract.View) : RegisterContract.Presenter,
    RegisterContract.OnRegisterListener {

    private val model = RegisterModel(this)
    override fun performRegister(email: String, password: String, username: String, uri: Uri?) {
        model.registerWithEmailAndPassword(email, password, username, uri)
    }

    override fun onSuccess() {
        view.onSuccess()
    }

    override fun onFailure(message: String) {
        view.onFailure(message)
    }
}