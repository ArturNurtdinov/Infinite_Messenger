package com.infinitevoid.easymessenger.contracts

import com.infinitevoid.easymessenger.data.ChatMessage

interface ChatLogContract {
    interface Model {
        fun setListenerForMessages(toId: String)
        fun sendMessage(text: String, toId: String)
    }

    interface Presenter {
        fun setListenerForMessages(toId: String)
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