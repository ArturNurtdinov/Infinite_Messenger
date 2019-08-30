package com.topaz.easymessenger.presenters

import com.topaz.easymessenger.contracts.LatestMessagesContract
import com.topaz.easymessenger.data.ChatMessage
import com.topaz.easymessenger.data.User
import com.topaz.easymessenger.models.LatestMessagesModel

class LatestMessagesPresenter(private val view: LatestMessagesContract.View) :
    LatestMessagesContract.Presenter, LatestMessagesContract.OnDataReady {
    private val model = LatestMessagesModel(this)

    override fun fetchCurrentUser() {
        model.fetchCurrentUser()
    }

    override fun setListenerForLatest() {
        model.setListenerForLatest()
    }

    override fun onLatestChanged(chatMessage: ChatMessage, key: String) {
        view.onLatestChanged(chatMessage, key)
    }

    override fun onLatestAdded(chatMessage: ChatMessage, key: String) {
        view.onLatestAdded(chatMessage, key)
    }

    override fun verifyIsLogged() {
        if (model.verifyIsLogged()) {
            view.isLogged()
        } else {
            view.isNotLogged()
        }
    }

    override fun signOut() {
        model.signOut()
        view.onSignOut()
    }

    override fun sendUser(user: User?) {
        view.initializeUser(user)
    }
}