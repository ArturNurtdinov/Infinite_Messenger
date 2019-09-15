package com.infinitevoid.easymessenger.contracts

import com.infinitevoid.easymessenger.data.User

interface NewMessageContract {
    interface Model {
        fun fetchUsers()
        fun onDestroy()
    }

    interface Presenter {
        fun fetchUsers()
        fun onDestroy()
    }

    interface View {
        fun addUser(user: User)
    }

    interface ChangeListener{
        fun getChange(user: User)
    }
}