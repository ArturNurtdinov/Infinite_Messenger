package com.infinitevoid.easymessenger.contracts

import android.net.Uri
import android.widget.ImageView
import com.infinitevoid.easymessenger.data.User

interface ProfileSettingsContract {
    interface View {
        fun initializeViewWithUser(user: User)
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface Presenter {
        fun fetchUser()
        fun loadPic(view: ImageView, uri: String?)
        fun setNewAvatar(uri: Uri?)
        fun setNewUsername(username: String)
        fun resetPassword()
    }

    interface Model {
        fun fetchUser()
        fun loadPic(view: ImageView, uri: String?)
        fun setNewAvatar(uri: Uri?)
        fun setNewUsername(username: String)
        fun resetPassword()
    }

    interface OnDataReady {
        fun onUserFetched(user: User)
    }
}