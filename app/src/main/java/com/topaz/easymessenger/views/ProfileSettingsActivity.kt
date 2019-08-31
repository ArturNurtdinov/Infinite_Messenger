package com.topaz.easymessenger.views

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.topaz.easymessenger.R
import com.topaz.easymessenger.contracts.ProfileSettingsContract
import com.topaz.easymessenger.data.User
import com.topaz.easymessenger.presenters.ProfileSettingsPresenter
import com.topaz.easymessenger.utils.Constants
import kotlinx.android.synthetic.main.activity_profile_settings.*

class ProfileSettingsActivity : AppCompatActivity(), ProfileSettingsContract.View {

    lateinit var user: User
    private val presenter = ProfileSettingsPresenter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_settings)

        supportActionBar?.title = "Settings"
        presenter.fetchUser()

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        edit_profile_username.setOnClickListener {
            profile_username.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
            imm.showSoftInput(profile_username, InputMethodManager.SHOW_IMPLICIT)
            profile_username.requestFocus()
            profile_username.setSelection(profile_username.text.length)
        }

        //implement another edit button
    }

    override fun initializeViewWithUser(user: User) {
        profile_username.setText(user.username, TextView.BufferType.EDITABLE)
        if (user.profileImageURL.isEmpty()) {
            presenter.loadPic(profile_picture, Constants.DEFAULT_AVATAR_URL)
        } else {
            presenter.loadPic(profile_picture, user.profileImageURL)
        }
    }

    override fun showProgressBar() {
        progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progress_bar.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.perform_changes -> {
                //Perform all changes
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
