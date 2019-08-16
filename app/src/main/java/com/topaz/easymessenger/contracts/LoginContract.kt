package com.topaz.easymessenger.contracts

interface LoginContract {
    interface Model {
        fun loginWithEmailAndPassword(email: String, password: String)
    }

    interface Presenter {
        fun performLogin(email: String, password: String)
    }

    interface View {
        fun onSuccess()
        fun onFailure(message: String)
    }

    interface OnLoginListener{
        fun onSuccess()
        fun onFailure(message: String)
    }
}