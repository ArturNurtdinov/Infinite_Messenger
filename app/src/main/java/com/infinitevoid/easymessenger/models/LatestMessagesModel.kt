package com.infinitevoid.easymessenger.models

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.infinitevoid.easymessenger.contracts.LatestMessagesContract
import com.infinitevoid.easymessenger.data.ChatMessage
import com.infinitevoid.easymessenger.data.User

class LatestMessagesModel(private val onDataReady: LatestMessagesContract.OnDataReady) :
    LatestMessagesContract.Model {

    override fun setMessageRead(message: ChatMessage) {
        val refLatestFromTo =
            FirebaseDatabase.getInstance()
                .getReference("/latest-messages/${message.fromId}/${message.toId}/read")
        val refLatestToFrom =
            FirebaseDatabase.getInstance()
                .getReference("/latest-messages/${message.toId}/${message.fromId}/read")
        val refUserMesToFrom =
            FirebaseDatabase.getInstance()
                .getReference("/user-messages/${message.toId}/${message.fromId}/${message.id}/read")
        val refUserMesFromTo =
            FirebaseDatabase.getInstance()
                .getReference("/user-messages/${message.fromId}/${message.toId}/${message.id}/read")
        message.read = "true"

        refLatestFromTo.setValue("true")
        refLatestToFrom.setValue("true")
        refUserMesFromTo.setValue("true")
        refUserMesToFrom.setValue("true")
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
                onDataReady.sendUser(p0.getValue(User::class.java))
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
                onDataReady.onLatestChanged(
                    p0.getValue(ChatMessage::class.java) ?: return,
                    p0.key!!
                )
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                onDataReady.onLatestAdded(p0.getValue(ChatMessage::class.java) ?: return, p0.key!!)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }
}