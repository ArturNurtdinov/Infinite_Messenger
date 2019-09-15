package com.infinitevoid.easymessenger.models

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.infinitevoid.easymessenger.contracts.NewMessageContract
import com.infinitevoid.easymessenger.data.User
import com.infinitevoid.easymessenger.views.NewMessageActivity

class NewMessageModel(private val changeListener: NewMessageContract.ChangeListener) :
    NewMessageContract.Model, ChildEventListener {

    private var ref: DatabaseReference? = null
    override fun fetchUsers() {
        ref = FirebaseDatabase.getInstance().getReference("/users")
        ref?.keepSynced(false)
        ref?.addChildEventListener(this)
    }

    override fun onDestroy() {
        ref?.removeEventListener(this)
    }

    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
        val matchingUid = FirebaseAuth.getInstance().uid
        val user = p0.getValue(User::class.java)
        Log.d(NewMessageActivity.TAG, "User with uid: ${user?.uid} fetched")
        if ((user != null) && (user.uid != matchingUid)) {
            changeListener.getChange(user)
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
}