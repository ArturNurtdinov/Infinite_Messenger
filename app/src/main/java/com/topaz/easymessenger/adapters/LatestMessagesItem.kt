package com.topaz.easymessenger.adapters

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.topaz.easymessenger.R
import com.topaz.easymessenger.data.ChatMessage
import com.topaz.easymessenger.data.User
import com.topaz.easymessenger.utils.Constants
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.latest_messages_row.view.*
import java.text.SimpleDateFormat
import java.util.*

class LatestMessagesItem(private val chatMessage: ChatMessage) :
    Item<ViewHolder>() {
    var userPartner: User? = null

    override fun getLayout(): Int {
        return R.layout.latest_messages_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.message.text = chatMessage.message
        val date = Date(chatMessage.timestamp * 1000)
        val dateFormat =
            if (date.day == Date(System.currentTimeMillis()).day) {
                SimpleDateFormat("HH:mm", Locale.getDefault())
            } else {
                SimpleDateFormat("MMM d", Locale.getDefault())
            }
        viewHolder.itemView.date.text = dateFormat.format(date)

        val chatPartnerId = if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
            chatMessage.toId
        } else {
            chatMessage.fromId
        }

        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)
                userPartner = user
                if (user != null) {
                    viewHolder.itemView.username.text = user.username

                    if (user.profileImageURL != "") {
                        Picasso.get().load(user.profileImageURL)
                            .into(viewHolder.itemView.profile_picture)
                    } else {
                        Picasso.get().load(Constants.DEFAULT_AVATAR_URL)
                            .into(viewHolder.itemView.profile_picture)
                    }
                }
            }

        })
    }

}