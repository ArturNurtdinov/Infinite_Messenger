package com.infinitevoid.easymessenger.contracts

import android.net.Uri
import com.infinitevoid.easymessenger.data.ChatMessage

interface ChatLogContract {
    interface Model {
        fun setListenerForMessages(toId: String)
        fun sendMessage(text: String, toId: String, uri: Uri?)
        fun onDestroy()
    }

    interface Presenter {
        fun setListenerForMessages(toId: String)
        fun sendMessage(text: String, toId: String, uri: Uri?)
        fun onDestroy()
    }

    interface View {
        fun showMessageFrom(message: ChatMessage)
        fun showMessageTo(message: ChatMessage)
    }

    interface ChangeListener {
        fun showMessage(message: ChatMessage, key: Boolean)
    }
}