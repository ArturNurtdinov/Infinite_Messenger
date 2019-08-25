package com.topaz.easymessenger.presenters

import com.topaz.easymessenger.contracts.LatestMessagesContract
import com.topaz.easymessenger.data.ChatMessage
import com.topaz.easymessenger.data.User
import com.topaz.easymessenger.models.LatestMessagesModel

class LatestMessagesPresenter(private val view: LatestMessagesContract.View) :
    LatestMessagesContract.Presenter, LatestMessagesContract.Fetcher {
    private val model = LatestMessagesModel(this)
    override fun setListenerForLatest() {
        model.setListenerForLatest()
    }

    override fun onLatestChanged(chatMessage: ChatMessage, user: User) {
        view.onLatestChanged(chatMessage, user)
    }

    override fun onLatestAdded(chatMessage: ChatMessage, user: User) {
        view.onLatestAdded(chatMessage, user)
    }

    override fun fetchCurrentUser() {
        model.fetchCurrentUser()
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

    override fun fetch(user: User?) {
        view.initializeUser(user)
    }
}