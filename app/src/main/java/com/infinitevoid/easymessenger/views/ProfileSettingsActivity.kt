package com.infinitevoid.easymessenger.views

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.infinitevoid.easymessenger.R
import com.infinitevoid.easymessenger.contracts.ProfileSettingsContract
import com.infinitevoid.easymessenger.data.User
import com.infinitevoid.easymessenger.presenters.ProfileSettingsPresenter
import com.infinitevoid.easymessenger.utils.Constants
import kotlinx.android.synthetic.main.activity_profile_settings.*

class ProfileSettingsActivity : AppCompatActivity(), ProfileSettingsContract.View {

    lateinit var user: User
    private var selectedPhotoUri: Uri? = null
    private val presenter = ProfileSettingsPresenter(this)
    private var isNameChanged = false
    private var isAvatarChanged = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_settings)

        supportActionBar?.title = getString(R.string.settings)
        presenter.fetchUser()

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        edit_profile_username.setOnClickListener {
            imm.showSoftInput(profile_username, InputMethodManager.SHOW_IMPLICIT)
            profile_username.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
            profile_username.requestFocus()
            profile_username.setSelection(profile_username.text.length)
            isNameChanged = true
        }

        edit_profile_picture.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        reset_password.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
                .setTitle(getString(R.string.reset_password))
                .setMessage(getString(R.string.do_you_really_want_to_reset_password))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    presenter.resetPassword()
                    Toast.makeText(
                        this,
                        getString(R.string.password_change_mail_sent),
                        Toast.LENGTH_LONG
                    ).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
                .setNegativeButton(getString(R.string.no)) { _, _ -> }
                .setNeutralButton(getString(R.string.cancel)) { _, _ -> }
                .setCancelable(true)
                .create()
            dialog.show()
        }
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
                if (isNameChanged) {
                    val newUsername = profile_username.text.toString()
                    if (newUsername.isEmpty() || newUsername.length > Constants.MAX_USERNAME_LENGTH) {
                        profile_username.requestFocus()
                        profile_username.setSelection(profile_username.text.length)
                        Toast.makeText(this, getString(R.string.wrong_username), Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        presenter.setNewUsername(profile_username.text.toString())
                    }
                }
                if (isAvatarChanged) {
                    presenter.setNewAvatar(selectedPhotoUri)
                }
                if (isNameChanged || isAvatarChanged) {
                    Toast.makeText(this, getString(R.string.changes_performed), Toast.LENGTH_SHORT)
                        .show()
                }
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            val bitmap =
                MediaStore.Images.Media.getBitmap(contentResolver, data.data)
            profile_picture.setImageBitmap(bitmap)
            selectedPhotoUri = data.data
            isAvatarChanged = true
        }
    }
}
