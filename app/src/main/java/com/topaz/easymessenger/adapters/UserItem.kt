package com.topaz.easymessenger.adapters

import com.squareup.picasso.Picasso
import com.topaz.easymessenger.R
import com.topaz.easymessenger.data.User
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.user_row.view.*

class UserItem(val user: User) : Item<ViewHolder>() {
    companion object {
        private const val DEFAULT_AVATAR_URL =
            "https://cdn4.iconfinder.com/data/icons/social-messaging-ui-color-and-shapes-3/177800/129-512.png"
    }

    override fun getLayout(): Int {
        return R.layout.user_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.username.text = user.username

        if (user.profileImageURL != "")
            Picasso.get().load(user.profileImageURL).into(viewHolder.itemView.profile_picture)
        else
            Picasso.get().load(DEFAULT_AVATAR_URL).into(viewHolder.itemView.profile_picture)
    }
}