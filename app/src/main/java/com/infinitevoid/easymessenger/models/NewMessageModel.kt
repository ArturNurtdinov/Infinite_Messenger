package com.infinitevoid.easymessenger.models

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.infinitevoid.easymessenger.contracts.NewMessageContract
import com.infinitevoid.easymessenger.data.User
import com.infinitevoid.easymessenger.views.NewMessageActivity

class NewMessageModel(private val changeListener: NewMessageContract.ChangeListener) :
    NewMessageContract.Model, ValueEventListener {
    private var ref : DatabaseReference? = null
    override fun fetchUsers() {
        ref = FirebaseDatabase.getInstance().getReference("/users")
        ref?.keepSynced(false)
        ref?.addValueEventListener(this)
    }

    override fun onDestroy() {
        ref?.removeEventListener(this)
    }

    override fun onCancelled(p0: DatabaseError) {

    }

    override fun onDataChange(p0: DataSnapshot) {
        val matchingUid = FirebaseAuth.getInstance().uid
        p0.children.forEach {
            val user = it.getValue(User::class.java)
            Log.d(NewMessageActivity.TAG, "User with uid: ${user?.uid} fetched")
            if ((user != null) && (user.uid != matchingUid)) {
                changeListener.getChange(user)
            }
        }
    }

}