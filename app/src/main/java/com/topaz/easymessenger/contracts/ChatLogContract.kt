package com.topaz.easymessenger.contracts

import com.topaz.easymessenger.data.ChatMessage

interface ChatLogContract {
    interface Model {
        fun setListenerForMessages()
        fun sendMessage(text: String, toId: String)
    }

    interface Presenter {
        fun setListenerForMessages()
        fun sendMessage(text: String, toId: String)
    }

    interface View {
        fun showMessageFrom(message: ChatMessage)
        fun showMessageTo(message: ChatMessage)
    }

    interface ChangeListener {
        fun showMessage(message: ChatMessage, key: Boolean)
    }
}