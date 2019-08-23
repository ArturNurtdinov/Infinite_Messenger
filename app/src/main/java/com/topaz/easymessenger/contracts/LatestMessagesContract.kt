package com.topaz.easymessenger.contracts

import com.topaz.easymessenger.data.User

interface LatestMessagesContract {
    interface Model {
        fun fetchCurrentUser()
        fun verifyIsLogged(): Boolean
        fun signOut()
    }

    interface Presenter {
        fun fetchCurrentUser()
        fun verifyIsLogged()
        fun signOut()
    }

    interface View {
        fun isLogged()
        fun isNotLogged()
        fun onSignOut()
        fun initializeUser(user: User?)
    }

    interface Fetcher {
        fun fetch(user: User?)
    }
}