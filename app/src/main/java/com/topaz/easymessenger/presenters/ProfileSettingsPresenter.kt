package com.topaz.easymessenger.presenters

import android.widget.ImageView
import com.topaz.easymessenger.contracts.ProfileSettingsContract
import com.topaz.easymessenger.data.User
import com.topaz.easymessenger.models.ProfileSettingsModel

class ProfileSettingsPresenter(private val view: ProfileSettingsContract.View) :
    ProfileSettingsContract.Presenter, ProfileSettingsContract.OnDataReady {

    private val model = ProfileSettingsModel(this)
    override fun fetchUser() {
        model.fetchUser()
    }

    override fun loadPic(view: ImageView, uri: String?) {
        this.view.showProgressBar()
        model.loadPic(view, uri)
    }


    override fun onUserFetched(user: User) {
        view.initializeViewWithUser(user)
        view.hideProgressBar()
    }
}