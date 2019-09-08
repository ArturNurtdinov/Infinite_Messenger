package com.infinitevoid.easymessenger.views

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.infinitevoid.easymessenger.R
import com.infinitevoid.easymessenger.adapters.UserItem
import com.infinitevoid.easymessenger.contracts.NewMessageContract
import com.infinitevoid.easymessenger.data.User
import com.infinitevoid.easymessenger.presenters.NewMessagePresenter
import com.infinitevoid.easymessenger.utils.Constants
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = getText(R.string.select_user)

        adapter.setOnItemClickListener { item, _ ->
            val intent = Intent(this, ChatLogActivity::class.java)
            intent.putExtra(Constants.USER_KEY, (item as UserItem).user)
            startActivity(intent)
            finish()
        }
        new_message_recycler.adapter = adapter
        new_message_recycler.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        presenter.fetchUsers()
    }

    override fun addUser(user: User) {
        progress_bar.visibility = View.GONE
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
