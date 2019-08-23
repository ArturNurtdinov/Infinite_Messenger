package com.topaz.easymessenger.models

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.topaz.easymessenger.contracts.LatestMessagesContract
import com.topaz.easymessenger.data.User

class LatestMessagesModel(private val fetcher: LatestMessagesContract.Fetcher) :
    LatestMessagesContract.Model {
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
}