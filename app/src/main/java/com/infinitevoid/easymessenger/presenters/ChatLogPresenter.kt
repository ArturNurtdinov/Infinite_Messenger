package com.infinitevoid.easymessenger.presenters

import com.infinitevoid.easymessenger.contracts.ChatLogContract
import com.infinitevoid.easymessenger.data.ChatMessage
import com.infinitevoid.easymessenger.models.ChatLogModel

class ChatLogPresenter(private val view: ChatLogContract.View) : ChatLogContract.Presenter,
    ChatLogContract.ChangeListener {
    private val model = ChatLogModel(this)
    override fun setListenerForMessages(toId: String) {
        model.setListenerForMessages(toId)
    }

    override fun sendMessage(text: String, toId: String) {
        model.sendMessage(text, toId)
    }

    override fun showMessage(message: ChatMessage, key: Boolean) {
        if (key) {
            view.showMessageTo(message)
        } else {
            view.showMessageFrom(message)
        }
    }
}