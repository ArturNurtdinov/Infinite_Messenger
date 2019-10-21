package com.infinitevoid.easymessenger.presenters

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.infinitevoid.easymessenger.contracts.ProfileSettingsContract
import com.infinitevoid.easymessenger.data.User
import com.infinitevoid.easymessenger.models.ProfileSettingsModel

class ProfileSettingsPresenter(private val view: ProfileSettingsContract.View) :
    ProfileSettingsContract.Presenter, ProfileSettingsContract.OnDataReady {

    private val model = ProfileSettingsModel(this)
    override fun fetchUser() {
        model.fetchUser()
    }

    override fun loadPic(view: ImageView, uri: String?, context: Context) {
        this.view.showProgressBar()
        model.loadPic(view, uri, context)
    }

    override fun setNewAvatar(uri: Uri?) {
        model.setNewAvatar(uri)
    }

    override fun setNewUsername(username: String) {
        model.setNewUsername(username)
    }

    override fun resetPassword() {
        model.resetPassword()
    }

    override fun onUserFetched(user: User) {
        view.initializeViewWithUser(user)
        view.hideProgressBar()
    }
}