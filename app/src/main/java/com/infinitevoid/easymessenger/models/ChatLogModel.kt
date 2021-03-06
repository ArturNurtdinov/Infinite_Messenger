package com.infinitevoid.easymessenger.models

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.infinitevoid.easymessenger.contracts.ChatLogContract
import com.infinitevoid.easymessenger.data.ChatMessage
import java.util.*

class ChatLogModel(private val listener: ChatLogContract.ChangeListener) : ChatLogContract.Model,
    ChildEventListener {
    private var ref: DatabaseReference? = null
    override fun setListenerForMessages(toId: String) {
        val fromId = FirebaseAuth.getInstance().uid ?: return
        ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")
        ref?.keepSynced(true)
        ref?.addChildEventListener(this)
    }

    override fun sendMessage(text: String, toId: String, uri: Uri?) {
        if (uri == null) {
            sendMessageWithImageURL(text, toId, "")
        } else {
            val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("message_images/$filename")
            ref.putFile(uri)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        sendMessageWithImageURL(text, toId, it.toString())
                    }
                }
        }
    }

    private fun sendMessageWithImageURL(text: String, toId: String, imageURL: String) {
        val fromId = FirebaseAuth.getInstance().uid ?: return

        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

        val toRef =
            FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId/${ref.key}")

        val latestMessageRef =
            FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")

        val fromRef =
            FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")

        val chatMessage =
            ChatMessage(
                ref.key ?: return,
                text,
                fromId,
                toId,
                System.currentTimeMillis() / 1000,
                imageURL,
                "false"
            )
        ref.setValue(chatMessage)
        toRef.setValue(chatMessage)
        latestMessageRef.setValue(chatMessage)
        fromRef.setValue(chatMessage)
    }

    override fun onDestroy() {
        ref?.removeEventListener(this)
    }

    override fun onCancelled(p0: DatabaseError) {
    }

    override fun onChildMoved(p0: DataSnapshot, p1: String?) {
    }

    override fun onChildChanged(p0: DataSnapshot, p1: String?) {
    }

    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
        val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
        val key = chatMessage.fromId == FirebaseAuth.getInstance().uid
        if (chatMessage.message.isNotEmpty() || chatMessage.imageURL.isNotEmpty()) {
            listener.showMessage(chatMessage, key)
        }
    }

    override fun onChildRemoved(p0: DataSnapshot) {
    }
}