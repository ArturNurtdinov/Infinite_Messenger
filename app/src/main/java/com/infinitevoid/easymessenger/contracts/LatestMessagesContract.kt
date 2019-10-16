package com.infinitevoid.easymessenger.contracts

import com.infinitevoid.easymessenger.data.ChatMessage
import com.infinitevoid.easymessenger.data.User

interface LatestMessagesContract {
    interface Model {
        fun setListenerForLatest()
        fun getUid(): String?
        fun verifyIsLogged(): Boolean
        fun signOut()
        fun setMessageRead(message: ChatMessage)
        fun fetchUser()
    }

    interface Presenter {
        fun setListenerForLatest()
        fun getUid(): String
        fun verifyIsLogged()
        fun signOut()
        fun setMessageRead(message: ChatMessage)
        fun fetchUser()
    }

    interface View {
        fun onLatestChanged(chatMessage: ChatMessage, key: String)
        fun onLatestAdded(chatMessage: ChatMessage, key: String)
        fun isLogged()
        fun isNotLogged()
        fun onSignOut()
        fun initializeUser(user: User)
    }

    interface OnDataReady {
        fun onLatestChanged(chatMessage: ChatMessage, key: String)
        fun onLatestAdded(chatMessage: ChatMessage, key: String)
        fun onUserFetched(user: User)
    }
}