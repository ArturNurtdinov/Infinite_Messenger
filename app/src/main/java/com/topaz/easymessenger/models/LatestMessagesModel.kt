package com.topaz.easymessenger.models

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.topaz.easymessenger.contracts.LatestMessagesContract
import com.topaz.easymessenger.data.ChatMessage
import com.topaz.easymessenger.data.User

class LatestMessagesModel(private val fetcher: LatestMessagesContract.Fetcher) :
    LatestMessagesContract.Model {

    override fun fetchUserAndSetNotification(chatMessage: ChatMessage) {
        val ref = FirebaseDatabase.getInstance().getReference("/users/${chatMessage.fromId}")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                fetcher.createNotification(chatMessage, p0.getValue(User::class.java) ?: return)
            }

        })
    }

    override fun verifyIsLogged(): Boolean {
        return FirebaseAuth.getInstance().uid != null
    }

    override fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }

    override fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                fetcher.fetch(p0.getValue(User::class.java))
            }
        })
    }

    override fun setListenerForLatest() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                fetcher.onLatestChanged(p0.getValue(ChatMessage::class.java) ?: return, p0.key!!)
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                fetcher.onLatestAdded(p0.getValue(ChatMessage::class.java) ?: return, p0.key!!)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }
}