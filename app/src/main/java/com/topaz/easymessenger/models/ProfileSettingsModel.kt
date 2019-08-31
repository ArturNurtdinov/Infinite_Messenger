package com.topaz.easymessenger.models

import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.topaz.easymessenger.contracts.ProfileSettingsContract
import com.topaz.easymessenger.data.User

class ProfileSettingsModel(private val onDataReady: ProfileSettingsContract.OnDataReady) :
    ProfileSettingsContract.Model {
    override fun fetchUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                onDataReady.onUserFetched(p0.getValue(User::class.java) ?: return)
            }
        })
    }

    override fun loadPic(view: ImageView, uri: String?) {
        Picasso.get().load(uri).into(view)
    }
}