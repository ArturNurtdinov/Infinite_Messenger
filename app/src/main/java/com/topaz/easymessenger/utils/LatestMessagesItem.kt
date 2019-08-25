package com.topaz.easymessenger.utils

import com.squareup.picasso.Picasso
import com.topaz.easymessenger.R
import com.topaz.easymessenger.data.ChatMessage
import com.topaz.easymessenger.data.User
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.latest_messages_row.view.*

class LatestMessagesItem(private val chatMessage: ChatMessage, val user: User) :
    Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.latest_messages_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.username.text = user.username
        viewHolder.itemView.message.text = chatMessage.message
        if (user.profileImageURL != "") {
            Picasso.get().load(user.profileImageURL).into(viewHolder.itemView.profile_picture)
        } else {

            Picasso.get().load(UserItem.DEFAULT_AVATAR_URL)
                .into(viewHolder.itemView.profile_picture)
        }
    }

}