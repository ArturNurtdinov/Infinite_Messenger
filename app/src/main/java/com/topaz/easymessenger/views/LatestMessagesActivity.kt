package com.topaz.easymessenger.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.topaz.easymessenger.R
import com.topaz.easymessenger.contracts.LatestMessagesContract
import com.topaz.easymessenger.data.ChatMessage
import com.topaz.easymessenger.data.User
import com.topaz.easymessenger.presenters.LatestMessagesPresenter
import com.topaz.easymessenger.adapters.LatestMessagesItem
import com.topaz.easymessenger.views.NewMessageActivity.Companion.USER_KEY
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_messages.*

class LatestMessagesActivity : AppCompatActivity(), LatestMessagesContract.View {
    companion object {
        var currentUser: User? = null
    }

    private val presenter = LatestMessagesPresenter(this)
    private val adapter = GroupAdapter<ViewHolder>()
    private val latestMessagesMap = HashMap<String, ChatMessage>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)

        adapter.setOnItemClickListener { item, _ ->
            val intent = Intent(this, ChatLogActivity::class.java)
            intent.putExtra(USER_KEY, (item as LatestMessagesItem).user)
            startActivity(intent)
        }
        latest_messages_recycler.adapter = adapter
        presenter.verifyIsLogged()
        presenter.fetchCurrentUser()
        presenter.setListenerForLatest()
    }

    override fun onLatestChanged(chatMessage: ChatMessage, key: String) {
        latestMessagesMap[key] = chatMessage
        adapter.clear()
        latestMessagesMap.values.forEach {
            adapter.add(LatestMessagesItem(it))
        }
    }

    override fun onLatestAdded(chatMessage: ChatMessage, key: String) {
        latestMessagesMap[key] = chatMessage
        adapter.clear()
        latestMessagesMap.values.forEach {
            adapter.add(LatestMessagesItem(it))
        }
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
