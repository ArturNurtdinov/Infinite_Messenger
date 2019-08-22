package com.topaz.easymessenger.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.topaz.easymessenger.R
import com.topaz.easymessenger.adapters.UserItem
import com.topaz.easymessenger.contracts.NewMessageContract
import com.topaz.easymessenger.data.User
import com.topaz.easymessenger.presenters.NewMessagePresenter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*

class NewMessageActivity : AppCompatActivity(), NewMessageContract.View {
    private val presenter = NewMessagePresenter(this)
    private val adapter = GroupAdapter<ViewHolder>()
    companion object {
        const val TAG = "NEW_MESSAGE"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select User"

        new_message_recycler.adapter = adapter
        presenter.fetchUsers()
    }
    override fun addUser(user: User) {
        adapter.add(UserItem(user))
        adapter.notifyDataSetChanged()
    }
}
