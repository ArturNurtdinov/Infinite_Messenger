package com.topaz.easymessenger.models

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.topaz.easymessenger.contracts.NewMessageContract
import com.topaz.easymessenger.data.User
import com.topaz.easymessenger.views.NewMessageActivity

class NewMessageModel(private val changeListener: NewMessageContract.ChangeListener) :
    NewMessageContract.Model {
    override fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d(NewMessageActivity.TAG, "Fetching users cancelled")
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

        })
    }

}