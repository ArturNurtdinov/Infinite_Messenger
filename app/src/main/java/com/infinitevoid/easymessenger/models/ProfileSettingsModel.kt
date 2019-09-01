package com.infinitevoid.easymessenger.models

import android.net.Uri
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.infinitevoid.easymessenger.contracts.ProfileSettingsContract
import com.infinitevoid.easymessenger.data.User
import java.util.*

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

    override fun setNewAvatar(uri: Uri?) {
        val uid = FirebaseAuth.getInstance().uid
        val refFetchUser = FirebaseDatabase.getInstance().getReference("/users/$uid")
        refFetchUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                changeAvatar(uri, p0.getValue(User::class.java) ?: return)
            }
        })
    }

    override fun setNewUsername(username: String) {
        val uid = FirebaseAuth.getInstance().uid
        val refUser = FirebaseDatabase.getInstance().getReference("/users/$uid/username")
        refUser.setValue(username)
    }

    override fun resetPassword() {
        val auth = FirebaseAuth.getInstance()
        auth.sendPasswordResetEmail(auth.currentUser?.email.toString())
        auth.signOut()
    }

    private fun changeAvatar(uri: Uri?, user: User) {
        //remove old
        if (user.profileImageURL != "") {
            val refOldImage = FirebaseStorage.getInstance()
                .getReferenceFromUrl(user.profileImageURL)
            refOldImage.delete()
        }
        //set new
        val filename = UUID.randomUUID().toString()
        val refImage = FirebaseStorage.getInstance().getReference("/images/$filename")
        refImage.putFile(uri ?: return).addOnSuccessListener {
            refImage.downloadUrl.addOnSuccessListener {
                val refUser =
                    FirebaseDatabase.getInstance()
                        .getReference("/users/${user.uid}/profileImageURL")
                refUser.setValue(it.toString())
            }
        }
    }
}