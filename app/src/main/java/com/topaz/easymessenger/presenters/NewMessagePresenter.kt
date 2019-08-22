package com.topaz.easymessenger.presenters

import com.topaz.easymessenger.contracts.NewMessageContract
import com.topaz.easymessenger.data.User
import com.topaz.easymessenger.models.NewMessageModel

class NewMessagePresenter(private val view: NewMessageContract.View) : NewMessageContract.Presenter,
    NewMessageContract.ChangeListener {
    private val model = NewMessageModel(this)
    override fun fetchUsers() {
        model.fetchUsers()
    }

    override fun getChange(user: User) {
        view.addUser(user)
    }
}