package com.topaz.easymessenger.contracts

import com.topaz.easymessenger.data.ChatMessage
import com.topaz.easymessenger.data.User

interface LatestMessagesContract {
    interface Model {
        fun setListenerForLatest()
        fun fetchCurrentUser()
        fun verifyIsLogged(): Boolean
        fun signOut()
        fun setMessageRead(message: ChatMessage)
    }

    interface Presenter {
        fun setListenerForLatest()
        fun fetchCurrentUser()
        fun verifyIsLogged()
        fun signOut()
        fun setMessageRead(message: ChatMessage)
    }

    interface View {
        fun onLatestChanged(chatMessage: ChatMessage, key: String)
        fun onLatestAdded(chatMessage: ChatMessage, key: String)
        fun isLogged()
        fun isNotLogged()
        fun onSignOut()
        fun initializeUser(user: User?)
    }

    interface OnDataReady {
        fun onLatestChanged(chatMessage: ChatMessage, key: String)
        fun onLatestAdded(chatMessage: ChatMessage, key: String)
        fun sendUser(user: User?)
    }
}