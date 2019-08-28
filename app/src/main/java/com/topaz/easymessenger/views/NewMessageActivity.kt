package com.topaz.easymessenger.views

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import androidx.core.view.MenuItemCompat
import com.topaz.easymessenger.R
import com.topaz.easymessenger.adapters.UserItem
import com.topaz.easymessenger.contracts.NewMessageContract
import com.topaz.easymessenger.data.User
import com.topaz.easymessenger.presenters.NewMessagePresenter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import java.util.*

class NewMessageActivity : AppCompatActivity(), NewMessageContract.View {
    private val presenter = NewMessagePresenter(this)
    private val adapter = GroupAdapter<ViewHolder>()
    private val userList = LinkedList<User>()

    companion object {
        const val TAG = "NEW_MESSAGE"
        const val USER_KEY = "USER_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = getText(R.string.select_user)

        adapter.setOnItemClickListener { item, _ ->
            val intent = Intent(this, ChatLogActivity::class.java)
            intent.putExtra(USER_KEY, (item as UserItem).user)
            startActivity(intent)
            finish()
        }
        new_message_recycler.adapter = adapter
        presenter.fetchUsers()
    }

    override fun addUser(user: User) {
        adapter.add(UserItem(user))
        userList.add(user)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchView: SearchView =
            MenuItemCompat.getActionView(menu?.findItem(R.id.search)) as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.clear()
                userList.filter { it.username.contains(newText ?: return false, true) }.forEach {
                    adapter.add(UserItem(it))
                }
                return true
            }

        })

        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val component = ComponentName(this, NewMessageActivity::class.java)
        searchView.setSearchableInfo(manager.getSearchableInfo(component))
        return super.onCreateOptionsMenu(menu)
    }
}
