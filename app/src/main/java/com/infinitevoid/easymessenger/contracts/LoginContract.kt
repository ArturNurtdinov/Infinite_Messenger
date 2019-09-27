package com.infinitevoid.easymessenger.contracts

interface LoginContract {
    interface Model {
        fun loginWithEmailAndPassword(email: String, password: String)
        fun sendPasswordResetMail(email: String)
    }

    interface Presenter {
        fun performLogin(email: String, password: String)
        fun sendPasswordResetMail(email: String)
    }

    interface View {
        fun onSuccess()
        fun onFailure(message: String)
        fun onMailSent()
    }

    interface OnLoginListener{
        fun onMailSent()
        fun onSuccess()
        fun onFailure(message: String)
    }
}