package com.topaz.easymessenger.presenters

import com.topaz.easymessenger.contracts.LatestMessagesContract
import com.topaz.easymessenger.data.ChatMessage
import com.topaz.easymessenger.data.User
import com.topaz.easymessenger.models.LatestMessagesModel

class LatestMessagesPresenter(private val view: LatestMessagesContract.View) :
    LatestMessagesContract.Presenter, LatestMessagesContract.Fetcher {
    private val model = LatestMessagesModel(this)

    override fun setMessageRead(chatMessage: ChatMessage) {
        model.setMessageRead(chatMessage)
    }

    override fun setNotificationWithFetchingUser(chatMessage: ChatMessage) {
        model.fetchUserAndSetNotification(chatMessage)
    }

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

    override fun createNotification(chatMessage: ChatMessage, user: User) {
        view.createNotification(chatMessage, user)
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