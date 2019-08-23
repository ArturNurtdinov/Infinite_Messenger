package com.topaz.easymessenger.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.topaz.easymessenger.R
import com.topaz.easymessenger.contracts.LatestMessagesContract
import com.topaz.easymessenger.data.User
import com.topaz.easymessenger.presenters.LatestMessagesPresenter

class LatestMessagesActivity : AppCompatActivity(), LatestMessagesContract.View {
    companion object {
        var currentUser: User? = null
    }

    private val presenter = LatestMessagesPresenter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)

        presenter.verifyIsLogged()
        presenter.fetchCurrentUser()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_new_message -> {
                val intent = Intent(this, NewMessageActivity::class.java)
                startActivity(intent)
            }

            R.id.menu_sign_out -> {
                presenter.signOut()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun isLogged() {

    }

    override fun initializeUser(user: User?) {
        currentUser = user
    }

    override fun isNotLogged() {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onSignOut() {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}
