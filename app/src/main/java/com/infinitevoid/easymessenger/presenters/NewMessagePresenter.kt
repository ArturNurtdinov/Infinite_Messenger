package com.infinitevoid.easymessenger.presenters

import com.infinitevoid.easymessenger.contracts.NewMessageContract
import com.infinitevoid.easymessenger.data.User
import com.infinitevoid.easymessenger.models.NewMessageModel

class NewMessagePresenter(private val view: NewMessageContract.View) : NewMessageContract.Presenter,
    NewMessageContract.ChangeListener {
    private val model = NewMessageModel(this)
    override fun fetchUsers() {
        model.fetchUsers()
    }

    override fun onDestroy() {
        model.onDestroy()
    }

    override fun getChange(user: User) {
        view.addUser(user)
    }
}