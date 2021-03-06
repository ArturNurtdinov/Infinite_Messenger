package com.infinitevoid.easymessenger.adapters

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.infinitevoid.easymessenger.R
import com.infinitevoid.easymessenger.data.ChatMessage
import com.infinitevoid.easymessenger.data.User
import com.infinitevoid.easymessenger.utils.Constants
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.latest_messages_row.view.*
import java.text.SimpleDateFormat
import java.util.*

class LatestMessagesItem(val chatMessage: ChatMessage, private val context: Context) :
    Item<ViewHolder>() {
    var userPartner: User? = null

    override fun getLayout(): Int {
        return R.layout.latest_messages_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val date = Date(chatMessage.timestamp * 1000)
        val dateFormat =
            if (date.day == Date(System.currentTimeMillis()).day) {
                SimpleDateFormat("HH:mm", Locale.getDefault())
            } else {
                SimpleDateFormat("MMM d", Locale.getDefault())
            }
        viewHolder.itemView.date.text = dateFormat.format(date)

        val currentUserId = FirebaseAuth.getInstance().uid
        val chatPartnerId = if (chatMessage.fromId == currentUserId) {
            chatMessage.toId
        } else {
            chatMessage.fromId
        }

        if ((chatMessage.fromId != currentUserId) && (chatMessage.read == "false")) {
            viewHolder.itemView.read_mark.visibility = View.VISIBLE
        }

        if (chatMessage.fromId == currentUserId) {
            viewHolder.itemView.message.text =
                context.getString(R.string.placeholder_you, chatMessage.message)
        } else {
            viewHolder.itemView.message.text = chatMessage.message
        }

        if (chatMessage.message.isEmpty() && chatMessage.imageURL.isNotEmpty()) {
            viewHolder.itemView.message.text =
                viewHolder.itemView.message.text.toString().plus(" photo")
        }

        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.keepSynced(true)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)
                userPartner = user
                if (user != null) {
                    viewHolder.itemView.username.text = user.username

                    if (user.profileImageURL != "") {
                        Glide.with(context)
                            .load(user.profileImageURL)
                            .into(viewHolder.itemView.profile_picture)
                    } else {
                        Glide.with(context)
                            .load(Constants.DEFAULT_AVATAR_URL)
                            .into(viewHolder.itemView.profile_picture)
                    }
                }
            }
        })
    }
}