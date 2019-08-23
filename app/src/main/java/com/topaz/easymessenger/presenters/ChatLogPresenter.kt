package com.topaz.easymessenger.presenters

import com.topaz.easymessenger.contracts.ChatLogContract
import com.topaz.easymessenger.data.ChatMessage
import com.topaz.easymessenger.models.ChatLogModel

class ChatLogPresenter(private val view: ChatLogContract.View) : ChatLogContract.Presenter,
    ChatLogContract.ChangeListener {
    private val model = ChatLogModel(this)
    override fun setListenerForMessages() {
        model.setListenerForMessages()
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