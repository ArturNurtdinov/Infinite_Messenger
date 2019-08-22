package com.topaz.easymessenger.contracts

import com.topaz.easymessenger.data.User

interface NewMessageContract {
    interface Model {
        fun fetchUsers()
    }

    interface Presenter {
        fun fetchUsers()
    }

    interface View {
        fun addUser(user: User)
    }

    interface ChangeListener{
        fun getChange(user: User)
    }
}