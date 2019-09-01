package com.infinitevoid.easymessenger.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.infinitevoid.easymessenger.R
import com.infinitevoid.easymessenger.contracts.ChatLogContract
import com.infinitevoid.easymessenger.adapters.ChatFromItem
import com.infinitevoid.easymessenger.adapters.ChatToItem
import com.infinitevoid.easymessenger.data.User
import com.infinitevoid.easymessenger.data.ChatMessage
import com.infinitevoid.easymessenger.presenters.ChatLogPresenter
import com.infinitevoid.easymessenger.utils.Constants
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity(), ChatLogContract.View {
    private lateinit var toUser: User
    private val adapter = GroupAdapter<ViewHolder>()
    private val presenter = ChatLogPresenter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        toUser = intent.getParcelableExtra(Constants.USER_KEY)
        supportActionBar?.title = toUser.username

        chat_log_recycler.adapter = adapter

        presenter.setListenerForMessages(toUser.uid)

        send.setOnClickListener {
            presenter.sendMessage(chat_log.text.toString(), toUser.uid)
            chat_log.text.clear()
            chat_log_recycler.scrollToPosition(adapter.itemCount - 1)
        }
    }

    override fun showMessageFrom(message: ChatMessage) {
        adapter.add(ChatFromItem(message, toUser))
        adapter.notifyDataSetChanged()
        chat_log_recycler.scrollToPosition(adapter.itemCount - 1)
    }

    override fun showMessageTo(message: ChatMessage) {
        val currentUser = LatestMessagesActivity.currentUser ?: return
        adapter.add(ChatToItem(message, currentUser))
        adapter.notifyDataSetChanged()
        chat_log_recycler.scrollToPosition(adapter.itemCount - 1)
    }
}
