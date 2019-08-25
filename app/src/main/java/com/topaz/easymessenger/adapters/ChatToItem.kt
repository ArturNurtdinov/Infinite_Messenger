package com.topaz.easymessenger.adapters

import com.squareup.picasso.Picasso
import com.topaz.easymessenger.R
import com.topaz.easymessenger.data.User
import com.topaz.easymessenger.utils.Constants
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatToItem(private val message: String, private val user: User) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.message.text = message
        if (user.profileImageURL != "") {
            Picasso.get().load(user.profileImageURL).into(viewHolder.itemView.profile_picture)
        } else {
            Picasso.get().load(Constants.DEFAULT_AVATAR_URL)
                .into(viewHolder.itemView.profile_picture)
        }
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}