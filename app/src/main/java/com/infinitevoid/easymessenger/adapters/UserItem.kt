package com.infinitevoid.easymessenger.adapters

import com.squareup.picasso.Picasso
import com.infinitevoid.easymessenger.R
import com.infinitevoid.easymessenger.data.User
import com.infinitevoid.easymessenger.utils.Constants
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.user_row.view.*

class UserItem(val user: User) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.user_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.username.text = user.username

        if (user.profileImageURL != "")
            Picasso.get().load(user.profileImageURL).into(viewHolder.itemView.profile_picture)
        else
            Picasso.get().load(Constants.DEFAULT_AVATAR_URL).into(viewHolder.itemView.profile_picture)
    }
}