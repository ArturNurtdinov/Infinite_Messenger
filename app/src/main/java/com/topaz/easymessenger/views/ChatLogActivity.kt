package com.topaz.easymessenger.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.topaz.easymessenger.R
import com.topaz.easymessenger.utils.ChatFromItem
import com.topaz.easymessenger.utils.ChatToItem
import com.topaz.easymessenger.data.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)

        supportActionBar?.title = user.username

        val adapter = GroupAdapter<ViewHolder>()

        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        chat_log_recycler.adapter = adapter
    }
}
