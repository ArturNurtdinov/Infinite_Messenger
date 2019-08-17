package com.topaz.easymessenger.contracts

import android.net.Uri

interface RegisterContract {
    interface Model {
        fun registerWithEmailAndPassword(
            email: String,
            password: String,
            username: String,
            uri: Uri?
        )
    }

    interface Presenter {
        fun performRegister(email: String, password: String, username: String, uri: Uri?)
    }

    interface View {
        fun onSuccess()
        fun onFailure(message: String)
    }

    interface OnRegisterListener {
        fun onSuccess()
        fun onFailure(message: String)
    }
}