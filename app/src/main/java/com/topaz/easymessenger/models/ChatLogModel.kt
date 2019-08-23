package com.topaz.easymessenger.models

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.topaz.easymessenger.contracts.ChatLogContract
import com.topaz.easymessenger.data.ChatMessage

class ChatLogModel(private val listener: ChatLogContract.ChangeListener) : ChatLogContract.Model {
    override fun setListenerForMessages() {
        val ref = FirebaseDatabase.getInstance().getReference("/messages")

        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                val key = chatMessage.fromId == FirebaseAuth.getInstance().uid
                listener.showMessage(chatMessage, key)
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

    override fun sendMessage(text: String, toId: String) {
        val ref = FirebaseDatabase.getInstance().getReference("/messages").push()
        val fromId = FirebaseAuth.getInstance().uid ?: return
        val chatMessage =
            ChatMessage(
                ref.key ?: return,
                text,
                fromId,
                toId,
                System.currentTimeMillis() / 1000
            )
        ref.setValue(chatMessage)
    }
}