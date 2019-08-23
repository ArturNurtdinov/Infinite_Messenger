package com.topaz.easymessenger.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.topaz.easymessenger.R
import com.topaz.easymessenger.utils.ChatFromItem
import com.topaz.easymessenger.utils.ChatToItem
import com.topaz.easymessenger.data.User
import com.topaz.easymessenger.data.ChatMessage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {

    companion object {
        const val TAG = "CHAT_LOG"
    }

    private var toUser: User? = null
    private val adapter = GroupAdapter<ViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        toUser = intent.getParcelableExtra(NewMessageActivity.USER_KEY)
        supportActionBar?.title = toUser?.username

        chat_log_recycler.adapter = adapter

        setListenerForMessages()

        send.setOnClickListener {
            performSendMessage()
        }
    }

    private fun setListenerForMessages() {
        val ref = FirebaseDatabase.getInstance().getReference("/messages")

        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                    val currentUser = LatestMessagesActivity.currentUser ?: return
                    adapter.add(ChatFromItem(chatMessage.message, currentUser))
                } else {
                    adapter.add(ChatToItem(chatMessage.message, toUser!!))
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }

    private fun performSendMessage() {
        val ref = FirebaseDatabase.getInstance().getReference("/messages").push()
        val text = chat_log.text.toString()
        val fromId = FirebaseAuth.getInstance().uid ?: return
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId = user.uid
        val chatMessage =
            ChatMessage(
                ref.key ?: return,
                text,
                fromId,
                toId,
                System.currentTimeMillis() / 1000
            )

        ref.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG, "Saved message to database with id: ${ref.key!!}")
            }
    }
}
