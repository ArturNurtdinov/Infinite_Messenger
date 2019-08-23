package com.topaz.easymessenger.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.topaz.easymessenger.R
import com.topaz.easymessenger.contracts.ChatLogContract
import com.topaz.easymessenger.utils.ChatFromItem
import com.topaz.easymessenger.utils.ChatToItem
import com.topaz.easymessenger.data.User
import com.topaz.easymessenger.data.ChatMessage
import com.topaz.easymessenger.presenters.ChatLogPresenter
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

        toUser = intent.getParcelableExtra(NewMessageActivity.USER_KEY)
        supportActionBar?.title = toUser.username

        chat_log_recycler.adapter = adapter

        presenter.setListenerForMessages()

        send.setOnClickListener {
            presenter.sendMessage(chat_log.text.toString(), toUser.uid)
            chat_log.setText("", TextView.BufferType.EDITABLE)
        }
    }

    override fun showMessageFrom(message: ChatMessage) {
        adapter.add(ChatFromItem(message.message, toUser))
        adapter.notifyDataSetChanged()
    }

    override fun showMessageTo(message: ChatMessage) {
        val currentUser = LatestMessagesActivity.currentUser ?: return
        adapter.add(ChatToItem(message.message, currentUser))
        adapter.notifyDataSetChanged()
    }
}
