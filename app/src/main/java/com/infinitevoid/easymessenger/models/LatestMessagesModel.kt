package com.infinitevoid.easymessenger.models

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.infinitevoid.easymessenger.contracts.LatestMessagesContract
import com.infinitevoid.easymessenger.data.ChatMessage
import com.infinitevoid.easymessenger.data.User
import java.lang.Exception

class LatestMessagesModel(private val onDataReady: LatestMessagesContract.OnDataReady) :
    LatestMessagesContract.Model, ChildEventListener {

    companion object {
        val firebaseInstance: FirebaseDatabase by lazy {
            FirebaseDatabase.getInstance().apply { setPersistenceEnabled(true) }
        }
    }

    private var ref: DatabaseReference? = null

    override fun setMessageRead(message: ChatMessage) {
        val refLatestFromTo =
            firebaseInstance
                .getReference("/latest-messages/${message.fromId}/${message.toId}/read")
        val refLatestToFrom =
            firebaseInstance
                .getReference("/latest-messages/${message.toId}/${message.fromId}/read")
        val refUserMesToFrom =
            firebaseInstance
                .getReference("/user-messages/${message.toId}/${message.fromId}/${message.id}/read")
        val refUserMesFromTo =
            firebaseInstance
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
        val ref = firebaseInstance.getReference("/users/$uid")
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
        ref = firebaseInstance.getReference("/latest-messages/$fromId")
        ref?.addChildEventListener(this)
    }

    override fun onCancelled(p0: DatabaseError) {
    }

    override fun onChildMoved(p0: DataSnapshot, p1: String?) {
    }

    override fun onChildRemoved(p0: DataSnapshot) {
    }

    override fun onChildChanged(p0: DataSnapshot, p1: String?) {
        val message = p0.getValue(ChatMessage::class.java) ?: return
        onDataReady.onLatestChanged(message, p0.key!!)
    }

    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
        val message = p0.getValue(ChatMessage::class.java) ?: return
        onDataReady.onLatestAdded(message, p0.key!!)
    }
}