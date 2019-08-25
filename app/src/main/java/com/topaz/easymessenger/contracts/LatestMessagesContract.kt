package com.topaz.easymessenger.contracts

import com.topaz.easymessenger.data.ChatMessage
import com.topaz.easymessenger.data.User

interface LatestMessagesContract {
    interface Model {
        fun setListenerForLatest()
        fun fetchCurrentUser()
        fun verifyIsLogged(): Boolean
        fun signOut()
    }

    interface Presenter {
        fun setListenerForLatest()
        fun fetchCurrentUser()
        fun verifyIsLogged()
        fun signOut()
    }

    interface View {
        fun onLatestChanged(chatMessage: ChatMessage, user: User)
        fun onLatestAdded(chatMessage: ChatMessage, user: User)
        fun isLogged()
        fun isNotLogged()
        fun onSignOut()
        fun initializeUser(user: User?)
    }

    interface Fetcher {
        fun onLatestChanged(chatMessage: ChatMessage, user: User)
        fun onLatestAdded(chatMessage: ChatMessage, user: User)
        fun fetch(user: User?)
    }
}